package com.settlement.batch.adapter.`in`.batch

import com.settlement.batch.application.port.`in`.*
import com.settlement.core.domain.model.Settlement
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.support.IteratorItemReader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import java.time.LocalDate

@Configuration
class SettlementBatchConfig(
    private val jobRepository              : JobRepository,
    private val transactionManager         : PlatformTransactionManager,
    private val findTargetUseCase          : FindSettlementTargetUseCase,
    private val calculateUseCase           : CalculateSettlementUseCase,
    private val saveUseCase                : SaveSettlementUseCase,
    private val findDailySettlementsUseCase: FindDailySettlementsUseCase,
    private val sendReportEventUseCase     : SendReportEventUseCase,
) {

    @Bean
    fun settlementJob(): Job {
        return JobBuilder("dailySettlementJob", jobRepository)
            .incrementer(RunIdIncrementer())
            .start(settlementStep()) // Step 1: 정산 & 저장
            .next(reportStep())      // Step 2: 리포트 발행
            .build()
    }

    @Bean
    fun settlementStep(): Step {
        return StepBuilder("settlementStep", jobRepository)
            .chunk<Long, Settlement>(100, transactionManager)
            .reader(merchantIdReader())
            .processor(settlementProcessor())
            .writer(settlementWriter())
            .build()
    }

    @Bean
    @StepScope
    fun merchantIdReader(): IteratorItemReader<Long> {
        return IteratorItemReader(findTargetUseCase.findTargetMerchants(LocalDate.now()))
    }

    @Bean
    @StepScope
    fun settlementProcessor(): ItemProcessor<Long, Settlement> {
        return ItemProcessor { merchantId ->
            val targetDate = LocalDate.now().minusDays(7)
            calculateUseCase.calculateSettlement(merchantId, targetDate)
        }
    }

    @Bean
    @StepScope
    fun settlementWriter(): ItemWriter<Settlement> {
        return ItemWriter { chunk ->
            saveUseCase.saveSettlements(chunk.items.toList())
        }
    }

    @Bean
    fun reportStep(): Step {
        return StepBuilder("reportStep", jobRepository)
            .chunk<Settlement, Settlement>(100, transactionManager)
            .reader(reportReader())  // 오늘 저장된 정산 데이터 조회
            .writer(reportWriter())  // 이벤트 발행
            .build()
    }

    @Bean
    @StepScope
    fun reportReader(): IteratorItemReader<Settlement> {
        val today = LocalDate.now()
        // T-7일자 정산 데이터를 조회 (Step 1에서 저장한 데이터)
        val targetDate = today.minusDays(7)
        val settlements = findDailySettlementsUseCase.findSettlementsByDate(targetDate)

        return IteratorItemReader(settlements)
    }

    @Bean
    @StepScope
    fun reportWriter(): ItemWriter<Settlement> {
        return ItemWriter { chunk ->
            // 이미 Reader에서 날짜 체크를 했으므로 바로 전송
            sendReportEventUseCase.sendReportEvent(chunk.items.toList())
        }
    }

}
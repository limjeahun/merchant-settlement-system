package com.settlement.mss.adapter.`in`.batch

import com.settlement.mss.application.usecase.SettlementService
import com.settlement.mss.domain.model.Settlement
import com.settlement.mss.domain.port.out.LoadMerchantPort
import com.settlement.mss.domain.port.out.PublishEventPort
import com.settlement.mss.domain.port.out.RecordSettlementPort
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
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
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val loadMerchantPort: LoadMerchantPort,
    private val settlementService: SettlementService,
    private val recordSettlementPort: RecordSettlementPort,
    private val publishEventPort: PublishEventPort,
) {

    @Bean
    fun settlementJob(): Job {
        return JobBuilder("dailySettlementJob", jobRepository)
            .start(settlementStep())
            .build()
    }

    @Bean
    fun settlementStep(): Step {
        return StepBuilder("settlementStep", jobRepository)
            .chunk<Long, Settlement>(100, transactionManager)
            .reader(merchantIdReader()) // Reader: 가맹점 ID만 읽음
            .processor(settlementProcessor()) // Processor: 정산 계산
            .writer(settlementWriter()) // Writer: 저장 및 이벤트 발행
            .build()
    }

    // Custom Reader: 오늘 정산해야 할 가맹점 ID 목록 제공
    @Bean
    fun merchantIdReader(): IteratorItemReader<Long> {
        val today = LocalDate.now()
        val targetIds = loadMerchantPort.findSettlementDueMerchants(today)
        return IteratorItemReader(targetIds)
    }

    // Processor: ID -> SettlementService -> Settlement (Domain)
    @Bean
    fun settlementProcessor(): ItemProcessor<Long, Settlement> {
        return ItemProcessor { merchantId ->
            // T-7 정책
            val targetDate = LocalDate.now().minusDays(7)
            settlementService.calculateForMerchant(merchantId, targetDate)
        }
    }

    // Writer: DB 저장 및 Kafka 발행
    @Bean
    fun settlementWriter(): ItemWriter<Settlement> {
        return ItemWriter { chunk ->
            // 1. DB 저장 (Adapter 호출)
            recordSettlementPort.saveAll(chunk.items.toList())

            // 2. Kafka 발행 (주간 리포트 등)
            // 간단하게: 저장된 도메인 객체를 기반으로 이벤트 발행
            chunk.items.forEach { settlement ->
                // 예시: 월요일이면 리포트 요청
                if (LocalDate.now().dayOfWeek == java.time.DayOfWeek.MONDAY) {
                    publishEventPort.sendReportEvent(
                        // TODO: 조회 로직 구현 필요
                        settlement.merchantId,
                        "MerchantName",
                        "2025-11-XX",
                        "2025-11-YY"
                    )
                }
            }
        }
    }

}
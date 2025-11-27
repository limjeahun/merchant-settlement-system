package com.settlement.mss.adapter.`in`.batch

import com.settlement.mss.application.port.`in`.CalculateSettlementUseCase
import com.settlement.mss.application.port.`in`.FindSettlementTargetUseCase
import com.settlement.mss.application.port.`in`.ProcessSettlementResultUseCase
import com.settlement.mss.domain.model.Settlement
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
    private val jobRepository     : JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val findUseCase       : FindSettlementTargetUseCase,
    private val calculateUseCase  : CalculateSettlementUseCase,
    private val processUseCase    : ProcessSettlementResultUseCase,
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
            .reader(merchantIdReader())
            .processor(settlementProcessor())
            .writer(settlementWriter())
            .build()
    }

    @Bean
    fun merchantIdReader(): IteratorItemReader<Long> {
        val today = LocalDate.now()
        val targetIds = findUseCase.findTargetMerchants(today)
        return IteratorItemReader(targetIds)
    }

    @Bean
    fun settlementProcessor(): ItemProcessor<Long, Settlement> {
        return ItemProcessor { merchantId ->
            val targetDate = LocalDate.now().minusDays(7)
            calculateUseCase.calculateSettlement(merchantId, targetDate)
        }
    }

    @Bean
    fun settlementWriter(): ItemWriter<Settlement> {
        return ItemWriter { chunk ->
            processUseCase.processSettlementResult(chunk.items.toList())
        }
    }


}
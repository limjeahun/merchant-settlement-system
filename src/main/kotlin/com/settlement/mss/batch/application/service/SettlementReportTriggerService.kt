package com.settlement.mss.batch.application.service

import com.settlement.mss.core.infrastructure.persistence.support.runInReadOnlyTransaction
import com.settlement.mss.batch.application.port.`in`.FindDailySettlementsUseCase
import com.settlement.mss.batch.application.port.`in`.SendReportEventUseCase
import com.settlement.mss.batch.application.port.out.LoadMerchantPort
import com.settlement.mss.batch.application.port.out.LoadSettlementPort
import com.settlement.mss.batch.application.port.out.PublishEventPort
import com.settlement.mss.common.extensions.getLogger
import com.settlement.mss.core.domain.model.Settlement
import org.springframework.stereotype.Service
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class SettlementReportTriggerService(
    private val loadSettlementPort: LoadSettlementPort,
    private val loadMerchantPort  : LoadMerchantPort,
    private val publishEventPort  : PublishEventPort,
    private val transactionManager: PlatformTransactionManager,
): FindDailySettlementsUseCase, SendReportEventUseCase {
    private val logger = getLogger()

    @Transactional(readOnly = true)
    override fun findSettlementsByDate(date: LocalDate): List<Settlement> {
        return loadSettlementPort.findSettlementsByTargetDate(date)
    }

    override fun sendReportEvent(settlements: List<Settlement>) {
        settlements.forEach { settlement ->
            // 가맬점 명 조회
            val merchantName = transactionManager.runInReadOnlyTransaction {
                loadMerchantPort.loadMerchant(settlement.merchantId).name
            } ?: ""
            logger.debug("리포트 작성 가매점명 : {}", merchantName)
            // Kafka 전송
            publishEventPort.sendReportEvent(
                merchantId = settlement.merchantId,
                merchantName = merchantName,
                startDate = settlement.targetDate.minusDays(6).toString(),
                endDate = settlement.targetDate.toString()
            )
        }
    }

}
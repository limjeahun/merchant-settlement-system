package com.settlement.mss.application.service

import com.settlement.mss.adapter.out.persistence.support.runInReadOnlyTransaction
import com.settlement.mss.application.port.`in`.FindDailySettlementsUseCase
import com.settlement.mss.application.port.`in`.SendReportEventUseCase
import com.settlement.mss.application.port.out.LoadMerchantPort
import com.settlement.mss.application.port.out.LoadSettlementPort
import com.settlement.mss.application.port.out.PublishEventPort
import com.settlement.mss.domain.model.Settlement
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.DayOfWeek
import java.time.LocalDate

@Service
class SettlementReportTriggerService(
    private val loadSettlementPort: LoadSettlementPort,
    private val loadMerchantPort: LoadMerchantPort,
    private val publishEventPort: PublishEventPort,
    private val transactionManager: DataSourceTransactionManager,
): FindDailySettlementsUseCase, SendReportEventUseCase {
    @Transactional(readOnly = true)
    override fun findSettlementsByDate(date: LocalDate): List<Settlement> {
        return loadSettlementPort.findSettlementsByTargetDate(date)
    }

    override fun sendReportEvent(settlements: List<Settlement>) {
        // 월요일 체크 로직
        if (LocalDate.now().dayOfWeek == DayOfWeek.MONDAY) {
            settlements.forEach { settlement ->
                // 가맬점 명 조회
                val merchantName = transactionManager.runInReadOnlyTransaction {
                    loadMerchantPort.loadMerchant(settlement.merchantId).name
                } ?: ""
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

}
package com.settlement.mss.application.service

import com.settlement.mss.application.port.`in`.FindDailySettlementsUseCase
import com.settlement.mss.application.port.`in`.SendReportEventUseCase
import com.settlement.mss.application.port.out.LoadSettlementPort
import com.settlement.mss.application.port.out.PublishEventPort
import com.settlement.mss.domain.model.Settlement
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalDate

@Service
class SettlementReportTriggerService(
    private val loadSettlementPort: LoadSettlementPort,
    private val publishEventPort  : PublishEventPort,
): FindDailySettlementsUseCase, SendReportEventUseCase {
    override fun findSettlementsByDate(date: LocalDate): List<Settlement> {
        return loadSettlementPort.findSettlementsByTargetDate(date)
    }

    override fun sendReportEvent(settlements: List<Settlement>) {
        // 월요일 체크 로직
        val today = LocalDate.now()
        if (today.dayOfWeek == DayOfWeek.MONDAY) {
            settlements.forEach { settlement ->
                publishEventPort.sendReportEvent(
                    merchantId = settlement.merchantId,
                    merchantName = "Merchant_${settlement.merchantId}",
                    startDate = settlement.targetDate.minusDays(6).toString(),
                    endDate = settlement.targetDate.toString()
                )
            }
        }
    }

}
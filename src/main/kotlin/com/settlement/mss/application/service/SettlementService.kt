package com.settlement.mss.application.service

import com.settlement.mss.application.port.`in`.CalculateSettlementUseCase
import com.settlement.mss.application.port.`in`.FindSettlementTargetUseCase
import com.settlement.mss.application.port.`in`.ProcessSettlementResultUseCase
import com.settlement.mss.application.port.out.LoadMerchantPort
import com.settlement.mss.application.port.out.LoadOrderPort
import com.settlement.mss.application.port.out.PublishEventPort
import com.settlement.mss.application.port.out.RecordSettlementPort
import com.settlement.mss.domain.model.Settlement
import com.settlement.mss.domain.policy.SettlementCalculator

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.DayOfWeek
import java.time.LocalDate

@Service
class SettlementService(
    private val loadMerchantPort    : LoadMerchantPort,
    private val loadOrderPort       : LoadOrderPort,
    private val recordSettlementPort: RecordSettlementPort,
    private val publishEventPort    : PublishEventPort,
    private val calculator          : SettlementCalculator,
) : FindSettlementTargetUseCase, CalculateSettlementUseCase, ProcessSettlementResultUseCase {

    @Transactional(readOnly = true)
    override fun findTargetMerchants(date: LocalDate): List<Long> {
        return loadMerchantPort.findSettlementDueMerchants(date)
    }

    @Transactional
    override fun calculateSettlement(merchantId: Long, targetDate: LocalDate): Settlement? {
        val merchant = loadMerchantPort.loadMerchant(merchantId)
        val orders = loadOrderPort.findOrdersByDate(merchantId, targetDate)

        if (orders.isEmpty()) return null
        return calculator.calculate(merchant, orders, targetDate)
    }

    @Transactional
    override fun processSettlementResult(settlements: List<Settlement>) {
        recordSettlementPort.saveAll(settlements)

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
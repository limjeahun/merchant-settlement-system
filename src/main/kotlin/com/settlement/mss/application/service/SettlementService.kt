package com.settlement.mss.application.service

import com.settlement.mss.domain.model.Settlement
import com.settlement.mss.domain.policy.SettlementCalculator
import com.settlement.mss.domain.port.out.LoadMerchantPort
import com.settlement.mss.domain.port.out.LoadOrderPort
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class SettlementService(
    private val loadMerchantPort: LoadMerchantPort,
    private val loadOrderPort   : LoadOrderPort,
    private val calculator      : SettlementCalculator,
) {
    fun calculateForMerchant(merchantId: Long, targetDate: LocalDate): Settlement? {
        val merchant = loadMerchantPort.loadMerchant(merchantId)
        val orders = loadOrderPort.findOrdersByDate(merchantId, targetDate)

        if (orders.isEmpty()) return null

        return calculator.calculate(merchant, orders, targetDate)
    }
}
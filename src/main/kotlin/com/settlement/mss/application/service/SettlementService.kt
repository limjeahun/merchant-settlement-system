package com.settlement.mss.application.service

import com.settlement.mss.application.port.`in`.CalculateSettlementUseCase
import com.settlement.mss.application.port.`in`.FindSettlementTargetUseCase
import com.settlement.mss.application.port.`in`.SaveSettlementUseCase
import com.settlement.mss.application.port.out.LoadMerchantPort
import com.settlement.mss.application.port.out.LoadOrderPort
import com.settlement.mss.application.port.out.RecordSettlementPort
import com.settlement.mss.domain.model.Settlement
import com.settlement.mss.domain.policy.SettlementCalculator

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class SettlementService(
    private val loadMerchantPort    : LoadMerchantPort,
    private val loadOrderPort       : LoadOrderPort,
    private val recordSettlementPort: RecordSettlementPort,
    private val calculator          : SettlementCalculator,
) : FindSettlementTargetUseCase, CalculateSettlementUseCase, SaveSettlementUseCase {

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

    override fun saveSettlements(settlements: List<Settlement>) {
        recordSettlementPort.saveAll(settlements)
    }

}
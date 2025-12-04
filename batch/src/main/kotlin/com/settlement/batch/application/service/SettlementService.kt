package com.settlement.mss.batch.application.service

import com.settlement.mss.batch.application.port.`in`.CalculateSettlementUseCase
import com.settlement.mss.batch.application.port.`in`.FindSettlementTargetUseCase
import com.settlement.mss.batch.application.port.`in`.SaveSettlementUseCase
import com.settlement.mss.batch.application.port.out.LoadMerchantPort
import com.settlement.mss.batch.application.port.out.LoadOrderPort
import com.settlement.mss.batch.application.port.out.RecordSettlementPort
import com.settlement.mss.common.extensions.getLogger
import com.settlement.mss.core.domain.model.Settlement
import com.settlement.mss.core.domain.policy.SettlementCalculator
import com.settlement.mss.core.domain.service.BusinessDayPolicy

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class SettlementService(
    private val loadMerchantPort    : LoadMerchantPort,
    private val loadOrderPort       : LoadOrderPort,
    private val recordSettlementPort: RecordSettlementPort,
    private val calculator          : SettlementCalculator,
    private val businessDayPolicy   : BusinessDayPolicy,
) : FindSettlementTargetUseCase, CalculateSettlementUseCase, SaveSettlementUseCase {
    private val logger = getLogger()

    @Transactional(readOnly = true)
    override fun findTargetMerchants(date: LocalDate): List<Long> {
        // 휴일에는 정산 대상 제외
        if (!businessDayPolicy.isBusinessDay(date)) {
            return emptyList()
        }

        return loadMerchantPort.findSettlementDueMerchants(date)
    }

    @Transactional(readOnly = true)
    override fun calculateSettlement(merchantId: Long, targetDate: LocalDate): Settlement? {
        val merchant = loadMerchantPort.loadMerchant(merchantId)
        // 정산 주기에 따른 주문 조회 범위 계산 (T+7 적용)
        val dateRange = merchant.settlementCycle.calculateDateRange(targetDate, businessDayPolicy)
            ?: return null

        val orders = loadOrderPort.findOrdersByDateRange(merchantId, dateRange.first, dateRange.second)
        if (orders.isEmpty()) return null
        logger.debug("merchant : {}", merchant.toString())
        return calculator.calculate(merchant, orders, targetDate)
    }

    @Transactional
    override fun saveSettlements(settlements: List<Settlement>) {
        recordSettlementPort.saveAll(settlements)
    }

}
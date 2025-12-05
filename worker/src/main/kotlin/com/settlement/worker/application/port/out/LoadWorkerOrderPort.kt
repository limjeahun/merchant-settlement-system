package com.settlement.worker.application.port.out

import com.settlement.core.domain.model.Order
import java.time.LocalDate

interface LoadWorkerOrderPort {
    /**
     * 가맹점 주문 날짜 기준 주문 조회
     */
    fun findOrdersByDateRange(
        merchantId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<Order>
}
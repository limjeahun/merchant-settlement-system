package com.settlement.batch.application.port.out

import com.settlement.core.domain.model.Order
import java.time.LocalDate

interface LoadOrderPort {
    fun findOrdersByDate(merchantId: Long, date: LocalDate): List<Order>
    fun findOrdersByDateRange(merchantId: Long, startDate: LocalDate, endDate: LocalDate): List<Order>
}
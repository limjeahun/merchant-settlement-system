package com.settlement.mss.application.port.out

import com.settlement.mss.domain.model.Order
import java.time.LocalDate

interface LoadOrderPort {
    fun findOrdersByDate(merchantId: Long, date: LocalDate): List<Order>
    fun findOrdersByDateRange(merchantId: Long, startDate: LocalDate, endDate: LocalDate): List<Order>
}
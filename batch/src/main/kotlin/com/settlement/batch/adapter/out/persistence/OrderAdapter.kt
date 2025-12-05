package com.settlement.batch.adapter.out.persistence

import com.settlement.core.infrastructure.persistence.jpa.mapper.OrderMapper
import com.settlement.core.infrastructure.persistence.jpa.repository.OrderJpaRepository
import com.settlement.batch.application.port.out.LoadOrderPort
import com.settlement.core.domain.model.Order
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class OrderAdapter(
    private val orderRepository: OrderJpaRepository,
    private val orderMapper: OrderMapper,
): LoadOrderPort {
    override fun findOrdersByDate(merchantId: Long, date: LocalDate): List<Order> {
        // 하루치 조회 (00:00 ~ 23:59)
        val start = date.atStartOfDay()
        val end = date.atTime(23, 59, 59)
        return orderRepository.findAllByMerchantIdAndOrderedAtBetween(merchantId, start, end)
            .map { orderMapper.toDomain(it) }
    }

    override fun findOrdersByDateRange(merchantId: Long, startDate: LocalDate, endDate: LocalDate): List<Order> {
        val start = startDate.atStartOfDay()
        val end = endDate.atTime(23, 59, 59)
        return orderRepository.findAllByMerchantIdAndOrderedAtBetween(merchantId, start, end).map { orderMapper.toDomain(it) }
    }

}
package com.settlement.worker.adapter.out.persistence

import com.settlement.core.domain.model.Order
import com.settlement.core.infrastructure.persistence.jpa.mapper.OrderMapper
import com.settlement.core.infrastructure.persistence.jpa.repository.OrderJpaRepository
import com.settlement.worker.application.port.out.LoadWorkerOrderPort
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class WorkerOrderAdapter(
    private val orderRepository: OrderJpaRepository,
    private val orderMapper    : OrderMapper,
): LoadWorkerOrderPort {
    override fun findOrdersByDateRange(merchantId: Long, startDate: LocalDate, endDate: LocalDate): List<Order> {
        val startDateTime = startDate.atStartOfDay()
        val endDateTime   = endDate.atTime(23, 59, 59)
        return orderRepository.findAllByMerchantIdAndOrderedAtBetween(merchantId, startDateTime, endDateTime)
            .map { orderMapper.toDomain(it) }
    }
}
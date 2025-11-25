package com.settlement.mss.adapter.out.persistence

import com.settlement.mss.adapter.out.persistence.mapper.OrderMapper
import com.settlement.mss.adapter.out.persistence.repository.OrderJpaRepository
import com.settlement.mss.domain.model.Order
import com.settlement.mss.domain.port.out.LoadOrderPort
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class OrderPersistenceAdapter(
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

}
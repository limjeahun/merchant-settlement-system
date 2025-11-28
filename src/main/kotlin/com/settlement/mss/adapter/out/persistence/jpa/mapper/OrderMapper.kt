package com.settlement.mss.adapter.out.persistence.jpa.mapper

import com.settlement.mss.adapter.out.persistence.jpa.entity.OrderEntity
import com.settlement.mss.domain.model.Order
import org.springframework.stereotype.Component

@Component
class OrderMapper {
    fun toDomain(entity: OrderEntity): Order {
        return Order(
            id = entity.id!!,
            merchantId = entity.merchantId,
            orderedAt = entity.orderedAt,
            amount = entity.amount,
            merchantDiscount = entity.merchantDiscount,
            platformDiscount = entity.platformDiscount
        )
    }
}
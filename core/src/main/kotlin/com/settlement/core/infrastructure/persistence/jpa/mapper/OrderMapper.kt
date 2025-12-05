package com.settlement.core.infrastructure.persistence.jpa.mapper

import com.settlement.core.infrastructure.persistence.jpa.entity.OrderEntity
import com.settlement.core.domain.model.Order
import com.settlement.core.domain.model.OrderStatus
import org.springframework.stereotype.Component

@Component
class OrderMapper {
    fun toDomain(entity: OrderEntity): Order {
        return Order(
            id               = entity.id!!,
            merchantId       = entity.merchantId,
            orderedAt        = entity.orderedAt,
            amount           = entity.amount,
            deliveryFee      = entity.deliveryFee,
            merchantDiscount = entity.merchantDiscount,
            platformDiscount = entity.platformDiscount,
            status           = OrderStatus.valueOf(entity.status)
        )
    }
}
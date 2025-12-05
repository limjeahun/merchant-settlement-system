package com.settlement.core.infrastructure.persistence.jpa.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
class OrderEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id              : Long? = null,
    val merchantId      : Long,
    val orderedAt       : LocalDateTime,
    val amount          : BigDecimal,
    val deliveryFee     : BigDecimal,   // 배송비
    val merchantDiscount: BigDecimal,   // 가맹점 부담 할인액
    val platformDiscount: BigDecimal,   // 플랫폼 부담 할인액
    val status          : String // ORDERED, CANCELLED 등
) {
}
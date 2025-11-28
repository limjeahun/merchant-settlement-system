package com.settlement.mss.adapter.out.persistence.jpa.entity

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
    val id: Long? = null,
    val merchantId: Long,
    val orderedAt: LocalDateTime,
    val amount: BigDecimal,
    val merchantDiscount: BigDecimal,
    val platformDiscount: BigDecimal,
    val status: String // ORDERED, CANCELLED 등
) {
}
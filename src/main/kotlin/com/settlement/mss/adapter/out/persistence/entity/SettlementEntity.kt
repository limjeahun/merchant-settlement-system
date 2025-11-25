package com.settlement.mss.adapter.out.persistence.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "settlements")
class SettlementEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val merchantId: Long,
    val targetDate: LocalDate,
    val totalOrderAmount: BigDecimal,
    val totalFeeAmount: BigDecimal,
    val totalDiscountShare: BigDecimal,
    val payoutAmount: BigDecimal,
    val status: String
) {
}
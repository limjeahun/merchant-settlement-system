package com.settlement.core.infrastructure.persistence.jpa.entity

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
    val totalOrderAmount    : BigDecimal,   // 상품 총액
    val totalDeliveryAmount : BigDecimal,   // 배송비 총액
    val totalRevenue        : BigDecimal,   // 총 매출 (상품 + 배송비)
    val totalDiscountShare  : BigDecimal,   // 가맹점 분담 할인액
    val platformFeeAmount   : BigDecimal,   // 플랫폼 수수료
    val pgFeeAmount         : BigDecimal,   // PG 수수료
    val vatAmount           : BigDecimal,   // 수수료에 대한 부가세 (10%)
    val withholdingTaxAmount: BigDecimal,   // 원천징수세 (개인사업자일 경우)
    val payoutAmount        : BigDecimal,   // 최종 지급액
    val status: String

) {
}
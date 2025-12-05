package com.settlement.core.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime

class Order(
    val id              : Long,
    val merchantId      : Long,
    val orderedAt       : LocalDateTime,
    val amount          : BigDecimal,   // 주문 금액 (상품 원가)
    val deliveryFee     : BigDecimal,   // 배송비
    val merchantDiscount: BigDecimal,   // 가맹점 부담 할인액
    val platformDiscount: BigDecimal,   // 플랫폼 부담 할인액
    val status          : OrderStatus,
) {
    /**
     * 실제 결제 금액 (PG 수수료 기준) = (상품 + 배송비) - 할인
     */
    fun getPaymentAmount(): BigDecimal {
        return (amount.add(deliveryFee))
            .subtract(merchantDiscount)
            .subtract(platformDiscount)
    }
}
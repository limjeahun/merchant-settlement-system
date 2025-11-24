package com.settlement.mss.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime

class Order(
    val id              : Long,
    val merchantId      : Long,
    val orderedAt       : LocalDateTime,
    val amount          : BigDecimal,    // 주문 금액 (상품 원가)
    val merchantDiscount: BigDecimal,    // 가맹점 부담 할인액
    val platformDiscount: BigDecimal     // 플랫폼 부담 할인액
) {
    fun getTotalDiscount() : BigDecimal {
        return merchantDiscount + platformDiscount
    }

    fun getPayAmount(): BigDecimal {
        return amount - getTotalDiscount()
    }
}
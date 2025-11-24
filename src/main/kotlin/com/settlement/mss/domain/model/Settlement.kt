package com.settlement.mss.domain.model

import java.math.BigDecimal
import java.time.LocalDate

data class Settlement(
    val id                : Long? = null,
    val merchantId        : Long,
    val targetDate        : LocalDate,      // 정산 기준일 (T-7)
    val totalOrderAmount  : BigDecimal,
    val totalFeeAmount    : BigDecimal,
    val totalDiscountShare: BigDecimal, // 가맹점이 부담한 총 할인액
    val payoutAmount      : BigDecimal,       // 최종 지급액
    val status            : SettlementStatus,
)

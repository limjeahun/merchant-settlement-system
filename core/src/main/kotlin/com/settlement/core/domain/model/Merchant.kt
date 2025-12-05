package com.settlement.core.domain.model

import java.math.BigDecimal

data class Merchant(
    val id             : Long,
    val name           : String,
    val settlementCycle: SettlementCycle,
    val businessType   : BusinessType,   // 사업자 유형
    val platformFeeRate: BigDecimal,     // 플랫폼 중개 수수료 (예: 0.05)
    val pgFeeRate      : BigDecimal,     // 결제 대행(PG) 수수료 (예: 0.03)
    val supportDelivery: Boolean         // 배송비 정산 대상 여부
)

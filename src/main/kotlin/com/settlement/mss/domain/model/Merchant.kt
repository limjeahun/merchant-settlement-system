package com.settlement.mss.domain.model

import java.math.BigDecimal

data class Merchant(
    val id             : Long,
    val name           : String,
    val settlementCycle: SettlementCycle,
    val feeRate        : BigDecimal,
)

package com.settlement.mss.domain.model

import java.time.LocalDateTime

data class SettlementReport(
    val id: String? = null, // MongoDB ID는 String
    val merchantId: Long,
    val merchantName: String,
    val totalSales: java.math.BigDecimal,
    val payoutAmount: java.math.BigDecimal,
    val reportContent: String, // AI가 생성한 텍스트
    val createdAt: LocalDateTime = LocalDateTime.now()
)

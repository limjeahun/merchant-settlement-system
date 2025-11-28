package com.settlement.mss.application.port.`in`

import java.math.BigDecimal

interface GenerateReportUseCase {
    fun generateAndSendReport(
        merchantId: Long,
        merchantName: String,
        totalSales: BigDecimal,
        payout: BigDecimal
    )
}
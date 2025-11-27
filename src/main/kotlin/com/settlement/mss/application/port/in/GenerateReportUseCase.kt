package com.settlement.mss.application.port.`in`

import java.math.BigDecimal

interface GenerateReportUseCase {
    fun generateAndSendReport(merchantName: String, totalSales: BigDecimal, payout: BigDecimal)
}
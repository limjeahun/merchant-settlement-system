package com.settlement.worker.application.port.`in`

import java.time.LocalDate

interface GenerateReportUseCase {
    fun generateAndSendReport(
        merchantId: Long,
        merchantName: String,
        startDate: LocalDate,
        endDate: LocalDate
    )
}
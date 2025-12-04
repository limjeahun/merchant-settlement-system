package com.settlement.mss.batch.application.port.out

interface PublishEventPort {
    fun sendReportEvent(
        merchantId: Long,
        merchantName: String,
        startDate: String,
        endDate: String
    )
}
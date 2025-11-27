package com.settlement.mss.application.port.out

interface PublishEventPort {
    fun sendReportEvent(
        merchantId: Long,
        merchantName: String,
        startDate: String,
        endDate: String
    )
}
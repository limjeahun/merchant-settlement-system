package com.settlement.mss.domain.port.out

interface PublishEventPort {
    fun sendReportEvent(merchantId: Long, merchantName: String, startDate: String, endDate: String)
}
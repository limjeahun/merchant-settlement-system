package com.settlement.common.event

class ReportRequestEvent(
    val merchantId  : Long,
    val merchantName: String,
    val startDate   : String,
    val endDate     : String,
) {
    /**
     * Jackson 역직렬화 호환성
     */
    constructor() : this(0L, "", "", "")
}
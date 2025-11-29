package com.settlement.mss.application.port.`in`

import com.settlement.mss.domain.model.Settlement

interface SendReportEventUseCase {
    fun sendReportEvent(settlements: List<Settlement>)
}
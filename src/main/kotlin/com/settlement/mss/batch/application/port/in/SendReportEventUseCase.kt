package com.settlement.mss.batch.application.port.`in`

import com.settlement.mss.core.domain.model.Settlement

interface SendReportEventUseCase {
    fun sendReportEvent(settlements: List<Settlement>)
}
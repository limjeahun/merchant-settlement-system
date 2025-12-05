package com.settlement.batch.application.port.`in`

import com.settlement.core.domain.model.Settlement

interface SendReportEventUseCase {
    fun sendReportEvent(settlements: List<Settlement>)
}
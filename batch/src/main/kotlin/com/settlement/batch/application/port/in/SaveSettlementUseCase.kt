package com.settlement.mss.batch.application.port.`in`

import com.settlement.mss.core.domain.model.Settlement

interface SaveSettlementUseCase {
    fun saveSettlements(settlements: List<Settlement>)
}
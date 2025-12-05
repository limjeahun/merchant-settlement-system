package com.settlement.batch.application.port.`in`

import com.settlement.core.domain.model.Settlement

interface SaveSettlementUseCase {
    fun saveSettlements(settlements: List<Settlement>)
}
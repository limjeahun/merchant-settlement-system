package com.settlement.mss.application.port.`in`

import com.settlement.mss.domain.model.Settlement

interface SaveSettlementUseCase {
    fun saveSettlements(settlements: List<Settlement>)
}
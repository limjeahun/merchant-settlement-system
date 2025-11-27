package com.settlement.mss.application.port.`in`

import com.settlement.mss.domain.model.Settlement

interface ProcessSettlementResultUseCase {
    fun processSettlementResult(settlements: List<Settlement>)
}
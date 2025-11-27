package com.settlement.mss.application.port.out

import com.settlement.mss.domain.model.Settlement

interface RecordSettlementPort {
    fun saveAll(settlements: List<Settlement>): List<Settlement>
}
package com.settlement.mss.domain.port.out

import com.settlement.mss.domain.model.Settlement
import com.settlement.mss.domain.model.SettlementStatus

interface RecordSettlementPort {
    /*
    fun save(settlement: Settlement): Settlement
    fun updateStatus(id: Long, status: SettlementStatus)
    */
    fun saveAll(settlements: List<Settlement>): List<Settlement>
}
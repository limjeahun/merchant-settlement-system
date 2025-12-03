package com.settlement.mss.application.port.out

import com.settlement.mss.domain.model.Settlement

interface RecordSettlementPort {
    /**
     * 정산 데이터 저장
     */
    fun saveAll(settlements: List<Settlement>): List<Settlement>
}
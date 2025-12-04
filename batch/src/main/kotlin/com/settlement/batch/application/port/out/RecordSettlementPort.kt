package com.settlement.mss.batch.application.port.out

import com.settlement.mss.core.domain.model.Settlement

interface RecordSettlementPort {
    /**
     * 정산 데이터 저장
     */
    fun saveAll(settlements: List<Settlement>): List<Settlement>
}
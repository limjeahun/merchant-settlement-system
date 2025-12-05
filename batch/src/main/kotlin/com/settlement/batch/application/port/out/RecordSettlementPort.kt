package com.settlement.batch.application.port.out

import com.settlement.core.domain.model.Settlement

interface RecordSettlementPort {
    /**
     * 정산 데이터 저장
     */
    fun saveAll(settlements: List<Settlement>): List<Settlement>
}
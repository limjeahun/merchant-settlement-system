package com.settlement.mss.application.port.out

import com.settlement.mss.domain.model.Settlement
import java.time.LocalDate

interface LoadSettlementPort {
    fun findSettlementsByTargetDate(targetDate: LocalDate): List<Settlement>

    /**
     * 특정 가맹점의 기간 내 정산 내역 모두 조회
     */
    fun findSettlementsByDateRange(
        merchantId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<Settlement>
}
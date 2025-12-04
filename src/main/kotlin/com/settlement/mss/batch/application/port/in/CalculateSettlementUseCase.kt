package com.settlement.mss.batch.application.port.`in`

import com.settlement.mss.core.domain.model.Settlement
import java.time.LocalDate

interface CalculateSettlementUseCase {
    /**
     * 가맹점 정산
     */
    fun calculateSettlement(merchantId: Long, targetDate: LocalDate): Settlement?
}
package com.settlement.mss.application.port.`in`

import com.settlement.mss.domain.model.Settlement
import java.time.LocalDate

interface CalculateSettlementUseCase {
    fun calculateSettlement(merchantId: Long, targetDate: LocalDate): Settlement?
}
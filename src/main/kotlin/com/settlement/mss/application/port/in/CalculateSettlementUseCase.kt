package com.settlement.mss.application.port.`in`

import com.settlement.mss.domain.model.Settlement
import java.time.LocalDate

interface CalculateSettlementUseCase {
    fun calculateForMerchant(merchantId: Long, targetDate: LocalDate): Settlement?
}
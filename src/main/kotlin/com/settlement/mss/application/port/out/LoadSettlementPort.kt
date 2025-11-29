package com.settlement.mss.application.port.out

import com.settlement.mss.domain.model.Settlement
import java.time.LocalDate

interface LoadSettlementPort {
    fun findSettlementsByTargetDate(targetDate: LocalDate): List<Settlement>
}
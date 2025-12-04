package com.settlement.mss.batch.application.port.out

import com.settlement.mss.core.domain.model.Settlement
import java.time.LocalDate

interface LoadSettlementPort {
    fun findSettlementsByTargetDate(targetDate: LocalDate): List<Settlement>
}
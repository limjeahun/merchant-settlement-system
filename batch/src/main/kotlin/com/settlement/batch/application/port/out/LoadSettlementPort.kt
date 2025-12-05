package com.settlement.batch.application.port.out

import com.settlement.core.domain.model.Settlement
import java.time.LocalDate

interface LoadSettlementPort {
    fun findSettlementsByTargetDate(targetDate: LocalDate): List<Settlement>
}
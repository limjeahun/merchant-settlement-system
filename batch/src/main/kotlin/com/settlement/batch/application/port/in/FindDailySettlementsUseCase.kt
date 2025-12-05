package com.settlement.batch.application.port.`in`

import com.settlement.core.domain.model.Settlement
import java.time.LocalDate

interface FindDailySettlementsUseCase {
    fun findSettlementsByDate(date: LocalDate): List<Settlement>
}
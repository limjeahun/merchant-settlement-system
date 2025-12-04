package com.settlement.mss.batch.application.port.`in`

import com.settlement.mss.core.domain.model.Settlement
import java.time.LocalDate

interface FindDailySettlementsUseCase {
    fun findSettlementsByDate(date: LocalDate): List<Settlement>
}
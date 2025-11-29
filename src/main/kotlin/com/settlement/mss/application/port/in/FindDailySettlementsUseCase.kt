package com.settlement.mss.application.port.`in`

import com.settlement.mss.domain.model.Settlement
import java.time.LocalDate

interface FindDailySettlementsUseCase {
    fun findSettlementsByDate(date: LocalDate): List<Settlement>
}
package com.settlement.mss.application.port.`in`

import java.time.LocalDate

interface FindSettlementTargetUseCase {
    fun findTargetMerchants(date: LocalDate): List<Long>
}
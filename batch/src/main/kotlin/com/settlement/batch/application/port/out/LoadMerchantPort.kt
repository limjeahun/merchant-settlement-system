package com.settlement.mss.batch.application.port.out

import com.settlement.mss.core.domain.model.Merchant
import java.time.LocalDate

interface LoadMerchantPort {
    fun loadMerchant(id: Long): Merchant
    fun findSettlementDueMerchants(date: LocalDate): List<Long>
}
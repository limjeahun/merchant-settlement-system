package com.settlement.batch.application.port.out

import com.settlement.core.domain.model.Merchant
import java.time.LocalDate

interface LoadMerchantPort {
    fun loadMerchant(id: Long): Merchant
    fun findSettlementDueMerchants(date: LocalDate): List<Long>
}
package com.settlement.mss.core.infrastructure.persistence.jpa.repository

import com.settlement.mss.core.infrastructure.persistence.jpa.entity.SettlementEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface SettlementJpaRepository: JpaRepository<SettlementEntity, Long> {

    fun findAllByTargetDate(targetDate: LocalDate): List<SettlementEntity>

    fun findAllByMerchantIdAndTargetDateBetween(
        merchantId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<SettlementEntity>
}
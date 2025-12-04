package com.settlement.mss.worker.adapter.out.persistence

import com.settlement.mss.core.infrastructure.persistence.jpa.mapper.SettlementMapper
import com.settlement.mss.core.infrastructure.persistence.jpa.repository.SettlementJpaRepository
import com.settlement.mss.core.domain.model.Settlement
import com.settlement.mss.worker.application.port.out.LoadSettlementByRangePort
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class WorkSettlementAdapter(
    private val settlementRepository: SettlementJpaRepository,
    private val settlementMapper: SettlementMapper,
): LoadSettlementByRangePort {
    override fun findSettlementsByDateRange(
        merchantId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<Settlement> {
        return settlementRepository.findAllByMerchantIdAndTargetDateBetween(merchantId, startDate, endDate)
            .map { settlementMapper.toDomain(it) }
    }
}
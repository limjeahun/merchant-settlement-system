package com.settlement.mss.adapter.out.persistence

import com.settlement.mss.adapter.out.persistence.jpa.mapper.SettlementMapper
import com.settlement.mss.adapter.out.persistence.jpa.repository.SettlementJpaRepository
import com.settlement.mss.application.port.out.RecordSettlementPort
import com.settlement.mss.domain.model.Settlement
import org.springframework.stereotype.Component

@Component
class SettlementPersistenceAdapter(
    private val settlementRepository: SettlementJpaRepository,
    private val settlementMapper: SettlementMapper,
): RecordSettlementPort {
    override fun saveAll(settlements: List<Settlement>): List<Settlement> {
        val entities = settlements.map { settlementMapper.toEntity(it) }
        val saved = settlementRepository.saveAll(entities)
        return saved.map { settlementMapper.toDomain(it) }
    }
}
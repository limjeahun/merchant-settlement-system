package com.settlement.mss.adapter.out.persistence

import com.settlement.mss.adapter.out.persistence.mapper.SettlementMapper
import com.settlement.mss.adapter.out.persistence.repository.SettlementJpaRepository
import com.settlement.mss.domain.model.Settlement
import com.settlement.mss.domain.port.out.RecordSettlementPort

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
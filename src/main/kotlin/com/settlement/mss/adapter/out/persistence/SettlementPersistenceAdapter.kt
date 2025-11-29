package com.settlement.mss.adapter.out.persistence

import com.settlement.mss.adapter.out.persistence.jpa.mapper.SettlementMapper
import com.settlement.mss.adapter.out.persistence.jpa.repository.SettlementJpaRepository
import com.settlement.mss.application.port.out.LoadSettlementPort
import com.settlement.mss.application.port.out.RecordSettlementPort
import com.settlement.mss.domain.model.Settlement
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class SettlementPersistenceAdapter(
    private val settlementRepository: SettlementJpaRepository,
    private val settlementMapper: SettlementMapper,
): RecordSettlementPort, LoadSettlementPort {
    override fun saveAll(settlements: List<Settlement>): List<Settlement> {
        val entities = settlements.map { settlementMapper.toEntity(it) }
        val saved = settlementRepository.saveAll(entities)
        return saved.map { settlementMapper.toDomain(it) }
    }

    override fun findSettlementsByTargetDate(targetDate: LocalDate): List<Settlement> {
        // 1. Repository를 통해 Entity 리스트 조회
        val entities = settlementRepository.findAllByTargetDate(targetDate)

        // 2. Entity -> Domain Model 변환 후 반환
        return entities.map { settlementMapper.toDomain(it) }
    }
}
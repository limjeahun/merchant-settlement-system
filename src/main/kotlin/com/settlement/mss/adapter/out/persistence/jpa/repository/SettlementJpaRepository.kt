package com.settlement.mss.adapter.out.persistence.jpa.repository

import com.settlement.mss.adapter.out.persistence.jpa.entity.SettlementEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface SettlementJpaRepository: JpaRepository<SettlementEntity, Long> {
    // targetDate 기준으로 조회하는 쿼리 메서드 자동 생성
    fun findAllByTargetDate(targetDate: LocalDate): List<SettlementEntity>
}
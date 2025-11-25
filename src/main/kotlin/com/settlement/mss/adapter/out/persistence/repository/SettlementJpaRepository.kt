package com.settlement.mss.adapter.out.persistence.repository

import com.settlement.mss.adapter.out.persistence.entity.SettlementEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SettlementJpaRepository: JpaRepository<SettlementEntity, Long> {

}
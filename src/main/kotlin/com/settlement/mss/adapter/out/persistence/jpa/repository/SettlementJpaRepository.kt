package com.settlement.mss.adapter.out.persistence.jpa.repository

import com.settlement.mss.adapter.out.persistence.jpa.entity.SettlementEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SettlementJpaRepository: JpaRepository<SettlementEntity, Long> {

}
package com.settlement.mss.core.infrastructure.persistence.jpa.repository

import com.settlement.mss.core.infrastructure.persistence.jpa.entity.MerchantEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface MerchantJpaRepository: JpaRepository<MerchantEntity, Long> {


    // [New] JPQL: 파라미터로 넘겨받은 Cycle 목록에 해당하는 가맹점 ID만 조회
    @Query("SELECT m.id " +
            "FROM MerchantEntity m " +
            "WHERE m.settlementCycle " +
            "IN :cycles"
    )
    fun findIdsBySettlementCycleIn(@Param("cycles") cycles: List<String>): List<Long>

}
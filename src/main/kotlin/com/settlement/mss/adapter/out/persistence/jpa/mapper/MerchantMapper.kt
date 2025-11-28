package com.settlement.mss.adapter.out.persistence.jpa.mapper

import com.settlement.mss.adapter.out.persistence.jpa.entity.MerchantEntity
import com.settlement.mss.domain.model.Merchant
import com.settlement.mss.domain.model.SettlementCycle
import org.springframework.stereotype.Component

@Component
class MerchantMapper {
    fun toDomain(entity: MerchantEntity): Merchant {
        return Merchant(
            id = entity.id!!,
            name = entity.name,
            settlementCycle = SettlementCycle.valueOf(entity.settlementCycle),
            feeRate = entity.feeRate
        )
    }
}
package com.settlement.mss.core.infrastructure.persistence.jpa.mapper

import com.settlement.mss.core.infrastructure.persistence.jpa.entity.MerchantEntity
import com.settlement.mss.core.domain.model.BusinessType
import com.settlement.mss.core.domain.model.Merchant
import com.settlement.mss.core.domain.model.SettlementCycle
import org.springframework.stereotype.Component

@Component
class MerchantMapper {
    fun toDomain(entity: MerchantEntity): Merchant {
        return Merchant(
            id = entity.id!!,
            name = entity.name,
            settlementCycle = SettlementCycle.valueOf(entity.settlementCycle),
            businessType    = BusinessType.valueOf(entity.businessType) ,   // 사업자 유형
            platformFeeRate = entity.platformFeeRate,     // 플랫폼 중개 수수료 (예: 0.05)
            pgFeeRate       = entity.pgFeeRate,     // 결제 대행(PG) 수수료 (예: 0.03)
            supportDelivery = entity.supportDelivery
        )
    }


}
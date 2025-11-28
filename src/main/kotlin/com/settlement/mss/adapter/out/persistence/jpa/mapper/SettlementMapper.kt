package com.settlement.mss.adapter.out.persistence.jpa.mapper

import com.settlement.mss.adapter.out.persistence.jpa.entity.SettlementEntity
import com.settlement.mss.domain.model.Settlement
import com.settlement.mss.domain.model.SettlementStatus
import org.springframework.stereotype.Component

@Component
class SettlementMapper {
    fun toEntity(domain: Settlement): SettlementEntity {
        return SettlementEntity(
            id = domain.id,
            merchantId = domain.merchantId,
            targetDate = domain.targetDate,
            totalOrderAmount = domain.totalOrderAmount,
            totalFeeAmount = domain.totalFeeAmount,
            totalDiscountShare = domain.totalDiscountShare,
            payoutAmount = domain.payoutAmount,
            status = domain.status.name
        )
    }

    fun toDomain(entity: SettlementEntity): Settlement {
        return Settlement(
            id = entity.id,
            merchantId = entity.merchantId,
            targetDate = entity.targetDate,
            totalOrderAmount = entity.totalOrderAmount,
            totalFeeAmount = entity.totalFeeAmount,
            totalDiscountShare = entity.totalDiscountShare,
            payoutAmount = entity.payoutAmount,
            status = SettlementStatus.valueOf(entity.status)
        )
    }
}
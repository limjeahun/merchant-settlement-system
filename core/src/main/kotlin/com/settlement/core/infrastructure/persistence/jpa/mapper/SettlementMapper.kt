package com.settlement.mss.core.infrastructure.persistence.jpa.mapper

import com.settlement.mss.core.infrastructure.persistence.jpa.entity.SettlementEntity
import com.settlement.mss.core.domain.model.Settlement
import com.settlement.mss.core.domain.model.SettlementStatus
import org.springframework.stereotype.Component

@Component
class SettlementMapper {
    fun toEntity(domain: Settlement): SettlementEntity {
        return SettlementEntity(
            id = domain.id,
            merchantId = domain.merchantId,
            targetDate = domain.targetDate,
            totalOrderAmount = domain.totalOrderAmount,

            totalDeliveryAmount = domain.totalDeliveryAmount,
            totalRevenue = domain.totalRevenue,
            totalDiscountShare = domain.totalDiscountShare,
            platformFeeAmount = domain.platformFeeAmount,
            pgFeeAmount = domain.pgFeeAmount,
            vatAmount = domain.vatAmount,
            withholdingTaxAmount = domain.withholdingTaxAmount,

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
            totalDeliveryAmount = entity.totalDeliveryAmount,
            totalRevenue = entity.totalRevenue,
            totalDiscountShare = entity.totalDiscountShare,
            platformFeeAmount = entity.platformFeeAmount,
            pgFeeAmount = entity.pgFeeAmount,
            vatAmount = entity.vatAmount,
            withholdingTaxAmount = entity.withholdingTaxAmount,
            payoutAmount = entity.payoutAmount,
            status = SettlementStatus.valueOf(entity.status)
        )
    }
}
package com.settlement.core.infrastructure.persistence.jpa.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "merchants")
class MerchantEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id             : Long? = null,
    val name           : String,
    val settlementCycle: String,
    val businessType   : String,   // 사업자 유형
    val platformFeeRate: BigDecimal,     // 플랫폼 중개 수수료 (예: 0.05)
    val pgFeeRate      : BigDecimal,     // 결제 대행(PG) 수수료 (예: 0.03)
    val supportDelivery: Boolean
)
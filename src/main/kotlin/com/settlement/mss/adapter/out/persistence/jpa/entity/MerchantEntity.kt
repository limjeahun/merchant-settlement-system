package com.settlement.mss.adapter.out.persistence.jpa.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "merchants")
class MerchantEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id             : Long? = null,
    val name           : String,
    @Enumerated(EnumType.STRING)
    val settlementCycle: String, // DB에는 String으로 저장
    val feeRate        : BigDecimal,
)
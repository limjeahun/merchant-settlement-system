package com.settlement.mss.adapter.out.persistence.jpa.repository

import com.settlement.mss.adapter.out.persistence.jpa.entity.OrderEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface OrderJpaRepository: JpaRepository<OrderEntity, Long> {

    fun findAllByMerchantIdAndOrderedAtBetween(
        merchantId: Long, start: LocalDateTime, end: LocalDateTime
    ): List<OrderEntity>
}
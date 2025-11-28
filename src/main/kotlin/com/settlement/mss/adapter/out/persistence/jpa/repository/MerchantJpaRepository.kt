package com.settlement.mss.adapter.out.persistence.jpa.repository

import com.settlement.mss.adapter.out.persistence.jpa.entity.MerchantEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MerchantJpaRepository: JpaRepository<MerchantEntity, Long> {

}
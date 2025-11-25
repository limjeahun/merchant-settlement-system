package com.settlement.mss.adapter.out.persistence.repository

import com.settlement.mss.adapter.out.persistence.entity.MerchantEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MerchantJpaRepository: JpaRepository<MerchantEntity, Long> {

}
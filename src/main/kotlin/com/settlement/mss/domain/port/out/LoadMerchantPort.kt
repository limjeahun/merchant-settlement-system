package com.settlement.mss.domain.port.out

import com.settlement.mss.domain.model.Merchant
import java.time.LocalDate

interface LoadMerchantPort {
    // 오늘 날짜 기준으로 정산 주기가 도래한 가맹점 ID 목록 조회
    fun findSettlementDueMerchants(date: LocalDate): List<Long>
    fun loadMerchant(id: Long): Merchant
}
package com.settlement.mss.domain.port.out

import com.settlement.mss.domain.model.Order
import java.time.LocalDate

interface LoadOrderPort {
    // 특정 가맹점의 T-7일자 정산 대상 주문 조회
    fun findOrdersByDate(merchantId: Long, date: LocalDate): List<Order>
}
package com.settlement.mss.worker.application.port.out

import java.math.BigDecimal

interface NotificationPort {
    /**
     * AI가 분석한 주간 리포트를 가맹점주에게 발송 (주로 이메일)
     */
    fun sendSettlementReport(
        merchantName: String,
        email: String,
        reportContent: String
    )

    /**
     * 대사(Reconciliation) 불일치 발생 시 운영팀에게 긴급 알림 (주로 슬랙/팀즈)
     */
    fun sendMismatchAlert(
        merchantId: Long,
        targetDate: String,
        expectedAmount: BigDecimal,
        actualAmount: BigDecimal
    )
}
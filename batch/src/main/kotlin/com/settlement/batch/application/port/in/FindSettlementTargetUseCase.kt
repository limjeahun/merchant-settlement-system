package com.settlement.batch.application.port.`in`

import java.time.LocalDate

interface FindSettlementTargetUseCase {
    /**
     * 정산 대상 가맹점 조회
     * @param date
     * @return 정산 대상 가맹점 아이디
     */
    fun findTargetMerchants(date: LocalDate): List<Long>
}
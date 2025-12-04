package com.settlement.mss.core.domain.model

import com.settlement.mss.core.domain.service.BusinessDayPolicy
import java.time.LocalDate

enum class SettlementCycle {
    DAILY {
        override fun calculateDateRange(
            targetDate: LocalDate,
            policy: BusinessDayPolicy
        ): Pair<LocalDate, LocalDate>? {
            // 영업일 정책을 이용해 누락일(Gap) 계산
            val processingDays = policy.getMissedDays(targetDate)
            // 공휴일 이면
            if (processingDays.isEmpty()) return null
            return Pair(
                processingDays.first().minusDays(7),
                processingDays.last().minusDays(7)
            )
        }
    },

    WEEKLY {
        override fun calculateDateRange(
            targetDate: LocalDate,
            policy: BusinessDayPolicy
        ): Pair<LocalDate, LocalDate>? {
            // 오늘(수) - 7일 = 지난주 수요일 (T+7 완료일)
            val end = targetDate.minusDays(7)
            val start = end.minusDays(6) // 1주일 전
            return Pair(start, end)
        }
    },

    MONTHLY {
        override fun calculateDateRange(
            targetDate: LocalDate,
            policy: BusinessDayPolicy
        ): Pair<LocalDate, LocalDate>? {
            // 전월 1일 ~ 말일
            val lastMonth = targetDate.minusMonths(1)
            val start = lastMonth.withDayOfMonth(1)
            val end = lastMonth.withDayOfMonth(lastMonth.lengthOfMonth())
            return Pair(start, end)
        }
    };

    abstract fun calculateDateRange(
        targetDate: LocalDate,
        policy: BusinessDayPolicy
    ): Pair<LocalDate, LocalDate>?
}
package com.settlement.mss.domain.service

import org.springframework.stereotype.Component
import java.time.DayOfWeek
import java.time.LocalDate

@Component
class BusinessDayPolicy {
    /**
     * 영업일 여부 판단 (핵심 도메인 규칙)
     * - 현재는 주말만 체크하지만, 추후 '공휴일 정책' 등 도메인 규칙이 추가될 수 있음
     */
    fun isBusinessDay(date: LocalDate): Boolean {
        val day = date.dayOfWeek
        return day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY
    }

    /**
     * 정산 누락일(Gap) 계산 로직
     */
    fun getMissedDays(today: LocalDate): List<LocalDate> {
        if (!isBusinessDay(today)) return emptyList()

        val missedDays = mutableListOf<LocalDate>()
        var current = today.minusDays(1)

        while (!isBusinessDay(current)) {
            missedDays.add(current)
            current = current.minusDays(1)
        }

        missedDays.reverse()
        missedDays.add(today)

        return missedDays
    }

}
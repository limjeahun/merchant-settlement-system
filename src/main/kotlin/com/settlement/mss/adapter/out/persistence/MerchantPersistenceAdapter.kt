package com.settlement.mss.adapter.out.persistence

import com.settlement.mss.adapter.out.persistence.jpa.mapper.MerchantMapper
import com.settlement.mss.adapter.out.persistence.jpa.repository.MerchantJpaRepository
import com.settlement.mss.application.port.out.LoadMerchantPort
import com.settlement.mss.domain.model.Merchant
import com.settlement.mss.domain.model.SettlementCycle
import org.springframework.stereotype.Component
import java.time.DayOfWeek
import java.time.LocalDate

@Component
class MerchantPersistenceAdapter(
    private val merchantRepository: MerchantJpaRepository,
    private val merchantMapper: MerchantMapper,
): LoadMerchantPort {

    override fun loadMerchant(id: Long): Merchant {
        val entity = merchantRepository.findById(id).orElseThrow()
        return merchantMapper.toDomain(entity)
    }

    override fun findSettlementDueMerchants(date: LocalDate): List<Long> {
        // 1. 오늘 날짜에 해당하는 정산 주기(Cycle) 목록 생성
        val targetCycles = mutableListOf<String>()
        // 매일 정산은 무조건 포함
        targetCycles.add(SettlementCycle.DAILY.name)
        // 매주 정산 (수요일인 경우 포함)
        if (date.dayOfWeek == DayOfWeek.WEDNESDAY) {
            targetCycles.add(SettlementCycle.WEEKLY.name)
        }
        // 매월 정산 (1일인 경우 포함)
        if (date.dayOfMonth == 1) {
            targetCycles.add(SettlementCycle.MONTHLY.name)
        }
        // 2. DB에서 해당 주기를 가진 가맹점 ID만 조회
        return merchantRepository.findIdsBySettlementCycleIn(targetCycles)
    }

    private fun isDue(cycle: SettlementCycle, date: LocalDate): Boolean {
        return when (cycle) {
            SettlementCycle.DAILY   -> true
            SettlementCycle.WEEKLY  -> date.dayOfWeek  == DayOfWeek.WEDNESDAY // 예: 수요일
            SettlementCycle.MONTHLY -> date.dayOfMonth == 1
        }
    }

}
package com.settlement.mss.adapter.out.persistence

import com.settlement.mss.adapter.out.persistence.mapper.MerchantMapper
import com.settlement.mss.adapter.out.persistence.repository.MerchantJpaRepository
import com.settlement.mss.domain.model.Merchant
import com.settlement.mss.domain.model.SettlementCycle
import com.settlement.mss.domain.port.out.LoadMerchantPort
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
        // QueryDSL이 없다면 모든 가맹점을 가져와서 필터링 (데이터 많으면 QueryDSL 필수)
        return merchantRepository.findAll().filter { entity ->
            val cycle = SettlementCycle.valueOf(entity.settlementCycle)
            isDue(cycle, date)
        }.map { it.id!! }
    }

    private fun isDue(cycle: SettlementCycle, date: LocalDate): Boolean {
        return when (cycle) {
            SettlementCycle.DAILY   -> true
            SettlementCycle.WEEKLY  -> date.dayOfWeek  == DayOfWeek.WEDNESDAY // 예: 수요일
            SettlementCycle.MONTHLY -> date.dayOfMonth == 1
        }
    }

}
package com.settlement.mss

import com.settlement.mss.adapter.out.persistence.jpa.entity.MerchantEntity
import com.settlement.mss.adapter.out.persistence.jpa.entity.OrderEntity
import com.settlement.mss.adapter.out.persistence.jpa.repository.MerchantJpaRepository
import com.settlement.mss.adapter.out.persistence.jpa.repository.OrderJpaRepository
import com.settlement.mss.domain.model.BusinessType
import com.settlement.mss.domain.model.OrderStatus
import com.settlement.mss.domain.model.SettlementCycle
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Commit
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.concurrent.ThreadLocalRandom

@SpringBootTest
class DataSeederTest {
    @Autowired lateinit var merchantRepository: MerchantJpaRepository
    @Autowired lateinit var orderRepository: OrderJpaRepository

    @Test
    @Commit // 테스트가 끝나도 롤백되지 않고 DB에 반영됨
    fun `대량의 테스트 데이터 생성하기`() {
        // 1. 가맹점 100개 생성
        val merchants = (1..100).map { i ->
            MerchantEntity(
                name = "테스트가맹점_$i",
                settlementCycle = getRandomCycle().name,
                businessType = if (i % 2 == 0) BusinessType.CORPORATE.name else BusinessType.INDIVIDUAL.name,
                platformFeeRate = BigDecimal("0.03"), // 플랫폼 3%
                pgFeeRate = BigDecimal("0.02"), // PG 2%
                supportDelivery = i % 3 == 0 // 3곳 중 1곳은 배송비 지원
            )
        }
        val savedMerchants = merchantRepository.saveAll(merchants)
        println("✅ 가맹점 ${savedMerchants.size}개 생성 완료")

        // 2. 가맹점 당 주문 50~100개씩 생성 (총 5,000 ~ 10,000건)
        val orders = mutableListOf<OrderEntity>()
        val startDate = LocalDateTime.now().minusDays(14) // 2주 전부터
        val endDate = LocalDateTime.now() // 오늘까지

        savedMerchants.forEach { merchant ->
            val orderCount = ThreadLocalRandom.current().nextInt(50, 101)

            repeat(orderCount) {
                val amount = BigDecimal(ThreadLocalRandom.current().nextInt(10000, 200000)) // 1만~20만원
                val hasDelivery = merchant.supportDelivery && ThreadLocalRandom.current().nextBoolean()

                orders.add(
                    OrderEntity(
                        merchantId = merchant.id!!,
                        orderedAt = randomDate(startDate, endDate),
                        amount = amount,
                        deliveryFee = if (hasDelivery) BigDecimal("3000") else BigDecimal.ZERO,
                        merchantDiscount = if (amount > BigDecimal(50000)) BigDecimal("2000") else BigDecimal.ZERO, // 5만원 이상 2천원 할인
                        platformDiscount = BigDecimal.ZERO,
                        status = if (ThreadLocalRandom.current().nextInt(10) == 0) OrderStatus.CANCELLED.name else OrderStatus.ORDERED.name // 10% 확률 취소
                    )
                )
            }
        }

        // 1000개씩 끊어서 저장 (Batch Insert 효과)
        orders.chunked(1000).forEach { batch ->
            orderRepository.saveAll(batch)
        }
        println("✅ 주문 ${orders.size}건 생성 완료")
    }

    // 랜덤 정산 주기
    private fun getRandomCycle(): SettlementCycle {
        val cycles = SettlementCycle.values()
        return cycles[ThreadLocalRandom.current().nextInt(cycles.size)]
    }

    // 랜덤 날짜 생성
    private fun randomDate(start: LocalDateTime, end: LocalDateTime): LocalDateTime {
        val startSeconds = start.toEpochSecond(java.time.ZoneOffset.UTC)
        val endSeconds = end.toEpochSecond(java.time.ZoneOffset.UTC)
        val randomSeconds = ThreadLocalRandom.current().nextLong(startSeconds, endSeconds)
        return LocalDateTime.ofEpochSecond(randomSeconds, 0, java.time.ZoneOffset.UTC)
    }


}
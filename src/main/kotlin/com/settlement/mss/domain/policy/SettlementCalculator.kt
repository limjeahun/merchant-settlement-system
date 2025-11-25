package com.settlement.mss.domain.policy

import com.settlement.mss.domain.model.Merchant
import com.settlement.mss.domain.model.Order
import com.settlement.mss.domain.model.Settlement
import com.settlement.mss.domain.model.SettlementStatus
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.LocalDate

@Component
class SettlementCalculator {
    fun calculate(
        merchant  : Merchant,
        orders    : List<Order>,
        targetDate: LocalDate,
    ): Settlement {
        // 1. 총 주문 금액 (상품 원가 총합)
        val totalOrderAmt = orders.fold(BigDecimal.ZERO) { acc, order -> acc.add(order.amount) }

        // 2. 가맹점 부담 할인 총액
        // (플랫폼 부담 할인액은 가맹점 매출에서 차감하지 않음 - 플랫폼이 메워주는 돈이므로)
        val totalMerchDisc = orders.fold(BigDecimal.ZERO) { acc, order -> acc.add(order.merchantDiscount) }

        // 3. 수수료 계산 (보통 (주문금액 - 가맹점할인) * 수수료율 로 계산하거나, 전체 금액 * 수수료율 등 정책에 따름)
        // (주문총액 - 가맹점할인) * 수수료율 적용 가정
        val revenueBase = totalOrderAmt.subtract(totalMerchDisc)
        val totalFee = revenueBase.multiply(merchant.feeRate)

        // 4. 최종 지급액 = 매출원가 - 가맹점부담할인 - 수수료
        val payout = totalOrderAmt
            .subtract(totalMerchDisc)
            .subtract(totalFee)

        return Settlement(
            merchantId = merchant.id,
            targetDate = targetDate,
            totalOrderAmount = totalOrderAmt,
            totalFeeAmount = totalFee,
            totalDiscountShare = totalMerchDisc,
            payoutAmount = payout,
            status = SettlementStatus.PENDING
        )
    }
}
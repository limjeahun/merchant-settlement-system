package com.settlement.mss.domain.policy

import com.settlement.mss.domain.model.*
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

@Component
class SettlementCalculator {
    fun calculate(
        merchant  : Merchant,
        orders    : List<Order>,
        targetDate: LocalDate,
    ): Settlement {
        // ==========================================================
        // 1. 매출 집계 (Revenue Aggregation)
        // ==========================================================
        // 상품 금액 합계
        val totalOrderAmt = orders.fold(BigDecimal.ZERO) { acc, order ->
            acc.add(order.amount)
        }
        // 배송비 합계 (가맹점이 배송비를 정산받는 경우)
        val totalDeliveryAmt = if (merchant.supportDelivery) {
            orders.fold(BigDecimal.ZERO) { acc, order ->
                acc.add(order.deliveryFee)
            }
        } else {
            BigDecimal.ZERO
        }
        // 총 매출 = 상품금액 + 배송비
        val totalRevenue = totalOrderAmt.add(totalDeliveryAmt)
        // ==========================================================
        // 2. 비용 공제 (Deductions)
        // ==========================================================
        // 가맹점 부담 할인액 합계
        val totalMerchDisc = orders.fold(BigDecimal.ZERO) { acc, order ->
            acc.add(order.merchantDiscount)
        }
        // [수수료 1] 플랫폼 수수료 (보통 '상품 판매금액' 기준으로 부과, 배송비 제외)
        // 공식: (상품금액 - 가맹점할인) * 플랫폼수수료율
        val platformFeeBase = totalOrderAmt.subtract(totalMerchDisc)
        val platformFee     = platformFeeBase.multiply(merchant.platformFeeRate).setScale(0, RoundingMode.DOWN) // 원 단위 절사
        // [수수료 2] PG 수수료 (실제 '결제 금액' 기준으로 부과)
        // 공식: 결제금액 합계 * PG수수료율
        val totalPaymentAmt = orders.fold(BigDecimal.ZERO) { acc, order -> acc.add(order.getPaymentAmount()) }
        val pgFee           = totalPaymentAmt.multiply(merchant.pgFeeRate).setScale(0, RoundingMode.DOWN)
        // [부가세] 수수료의 10% (플랫폼수수료 + PG수수료) * 0.1
        val totalFee  = platformFee.add(pgFee)
        val vatAmount = TaxType.VAT.calculate(totalFee)
        // ==========================================================
        // 3. 세금 처리 (Taxation)
        // ==========================================================
        // 정산 대상 금액 (세전) = 총 매출 - 가맹점할인 - (수수료 + 부가세)
        val preTaxPayout = totalRevenue
            .subtract(totalMerchDisc)
            .subtract(totalFee)
            .subtract(vatAmount)

        // 원천징수 (개인 사업자인 경우만 3.3% 차감)
        val withholdingTax = if (merchant.businessType == BusinessType.INDIVIDUAL) {
            TaxType.WITHHOLDING_TAX.calculate(preTaxPayout)
        } else {
            BigDecimal.ZERO // 법인은 0원 (세금계산서 처리)
        }
        // ==========================================================
        // 4. 최종 지급액 계산 (Final Payout)
        // ==========================================================
        val finalPayout = preTaxPayout.subtract(withholdingTax)

        return Settlement(
            merchantId           = merchant.id,
            targetDate           = targetDate,
            // 매출
            totalOrderAmount     = totalOrderAmt,
            totalDeliveryAmount  = totalDeliveryAmt,
            totalRevenue         = totalRevenue,
            // 공제
            totalDiscountShare   = totalMerchDisc,
            platformFeeAmount    = platformFee,
            pgFeeAmount          = pgFee,
            vatAmount            = vatAmount,
            // 세금
            withholdingTaxAmount = withholdingTax,
            // 결과
            payoutAmount         = finalPayout,
            status               = SettlementStatus.PENDING
        )
    }
}
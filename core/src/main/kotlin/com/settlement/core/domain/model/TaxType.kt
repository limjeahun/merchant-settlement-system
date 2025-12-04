package com.settlement.mss.core.domain.model

import java.math.BigDecimal
import java.math.RoundingMode

enum class TaxType(
    val description: String,
    val rate       : BigDecimal,
) {
    VAT("부가세", BigDecimal("0.1")) {
        override fun calculate(amount: BigDecimal): BigDecimal {
            // 부가세는 보통 원 단위 절사
            return amount.multiply(rate).setScale(0, RoundingMode.DOWN)
        }
    },

    WITHHOLDING_TAX("원천징수세", BigDecimal("0.033")) {
        override fun calculate(amount: BigDecimal): BigDecimal {
            // 원천징수세도 원 단위 절사 (필요시 반올림 정책 다르게 적용 가능)
            return amount.multiply(rate).setScale(0, RoundingMode.DOWN)
        }
    };

    abstract fun calculate(amount: BigDecimal): BigDecimal
}
package com.settlement.worker.application.service


import com.settlement.core.domain.model.Order
import com.settlement.core.domain.model.SettlementReport
import com.settlement.worker.application.port.`in`.GenerateReportUseCase
import com.settlement.worker.application.port.out.*


import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class ReportService(
    private val aiAnalysisPort     : AiAnalysisPort,
    private val notificationPort   : NotificationPort,
    private val saveReportPort     : SaveReportPort,
    private val loadSettlementPort : LoadSettlementByRangePort,
    private val loadWorkerOrderPort: LoadWorkerOrderPort,
): GenerateReportUseCase {

    @Transactional(readOnly = true)
    override fun generateAndSendReport(
        merchantId  : Long,
        merchantName: String,
        startDate   : LocalDate,
        endDate     : LocalDate,
    ) {
        // 1. 정산 데이터 조회
        val settlements = loadSettlementPort.findSettlementsByDateRange(merchantId, startDate, endDate)
        // 정산 내역이 없으면 리포트 스킵하거나 "내역 없음" 리포트 발송
        if (settlements.isEmpty()) {
            return
        }

        val orders = loadWorkerOrderPort.findOrdersByDateRange(merchantId, startDate, endDate)

        // 2. 데이터 합산
        val totalSales = settlements.fold(BigDecimal.ZERO) { acc, s -> acc.add(s.totalOrderAmount) }
        val payout     = settlements.fold(BigDecimal.ZERO) { acc, s -> acc.add(s.payoutAmount) }

        // 3. 일별 매출 분석 데이터 생성 (AI 제공용)
        val dailyStats = analyzeDailyStats(orders)

        // 4. 고도화된 프롬프트 구성
        val systemRole = """
            당신은 이커머스 데이터 분석가이자 친절한 정산 비서입니다.
            제공된 '일별 매출 데이터'를 분석하여 사장님에게 도움이 되는 인사이트를 제공해야 합니다.
        """.trimIndent()

        val prompt = """
            [보고서 대상]
            - 가맹점명: $merchantName
            - 기간: $startDate ~ $endDate
            - 총 매출: ${totalSales}원
            - 실 지급액: ${payout}원

            [일별 매출 상세 데이터]
            $dailyStats

            [작성 지시사항]
            1. 인삿말: 사장님의 노고를 격려하며 시작하세요.
            2. **핵심 분석(중요)**: 
               - 매출이 가장 높았던 날(Best Day)과 그 이유(주문 건수 등)를 언급하세요.
               - 매출이 저조했던 날이 있다면 원인을 추측하거나 격려의 말을 덧붙이세요.
               - 전체적인 매출 흐름(상승세/하락세/안정적)을 한 문장으로 요약하세요.
            3. 마무리: 다음 정산에 대한 기대감을 주며 정중하게 마무리하세요.
            4. 말투: 전문적이지만 딱딱하지 않고 따뜻하게 작성해주세요. (이모지 적절히 사용)
        """.trimIndent()

        // 5. 포트를 통해 AI 호출
        val aiComment = aiAnalysisPort.analyze(systemRole, prompt)

        val report = SettlementReport(
            merchantId = merchantId,
            merchantName = merchantName,
            totalSales = totalSales,
            payoutAmount = payout,
            reportContent = aiComment
        )
        saveReportPort.saveReport(report)

        // 3. 결과 발송
        //notificationPort.send(merchantName, aiComment)
    }

    /**
     * 주문 리스트를 날짜별로 묶어서 문자열로 변환하는 헬퍼 함수
     */
    private fun analyzeDailyStats(orders: List<Order>): String {
        if (orders.isEmpty()) return "주문 내역 없음"

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        // 날짜별로 그룹핑하여 (매출총액, 주문건수) 계산
        val statsMap = orders.groupBy { it.orderedAt.toLocalDate() }
            .mapValues { (_, dailyOrders) ->
                val dailyTotal = dailyOrders.fold(BigDecimal.ZERO) { acc, o -> acc.add(o.amount) }
                val count = dailyOrders.size
                Pair(dailyTotal, count)
            }
            .toSortedMap()

        // 문자열로 변환
        val sb = StringBuilder()
        statsMap.forEach { (date, stats) ->
            sb.append("- ${date.format(formatter)}: 매출 ${stats.first}원 (${stats.second}건)\n")
        }
        return sb.toString()
    }
}
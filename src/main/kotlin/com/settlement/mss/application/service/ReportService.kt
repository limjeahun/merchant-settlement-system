package com.settlement.mss.application.service

import com.settlement.mss.application.port.`in`.GenerateReportUseCase
import com.settlement.mss.application.port.out.AiAnalysisPort
import com.settlement.mss.application.port.out.NotificationPort
import com.settlement.mss.application.port.out.SaveReportPort
import com.settlement.mss.domain.model.SettlementReport
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ReportService(
    private val aiAnalysisPort  : AiAnalysisPort,
    private val notificationPort: NotificationPort,
    private val saveReportPort  : SaveReportPort,
): GenerateReportUseCase {
    override fun generateAndSendReport(
        merchantId: Long,
        merchantName: String,
        totalSales: BigDecimal,
        payout: BigDecimal)
    {
        // 1. 프롬프트 구성 (여기가 핵심 전략!)
        val systemRole = "당신은 10년 차 핀테크 정산 전문가이자 친절한 비서입니다."
        val prompt = """
            [상황]
            '$merchantName' 가맹점 사장님께 이번 주 정산 결과를 브리핑해야 합니다.
            
            [데이터]
            - 가맹점: $merchantName
            - 총 매출: ${totalSales}원
            - 실 지급액: ${payout}원
            
            [지시사항]
            1. 사장님이 기분 좋게 읽을 수 있도록 정중하고 격려하는 톤으로 작성하세요.
            2. 핵심 숫자를 명확히 언급하세요.
            3. 3줄 이내로 요약하세요.
        """.trimIndent()

        // 2. 포트를 통해 AI 호출 (인프라 기술 몰라도 됨)
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
}
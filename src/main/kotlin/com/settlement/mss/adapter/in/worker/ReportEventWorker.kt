package com.settlement.mss.adapter.`in`.worker

import com.settlement.mss.application.port.`in`.GenerateReportUseCase
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class ReportEventWorker(
    private val generateReportUseCase: GenerateReportUseCase
) {
    @KafkaListener(topics = ["settlement.report.request"], groupId = "report-worker-group")
    fun handleEvent(message: Map<String, Any>) {
        val merchantId = message["merchantId"] as Long
        // 메시지 파싱
        val merchantName = message["merchantName"] as String
        // 실제로는 DB조회나 message 안에 데이터가 더 있어야 함. 예시로 하드코딩.
        val totalSales = BigDecimal(1000000)
        val payout = BigDecimal(950000)
        // Application Service 호출
        generateReportUseCase.generateAndSendReport(merchantId, merchantName, totalSales, payout)
    }
}
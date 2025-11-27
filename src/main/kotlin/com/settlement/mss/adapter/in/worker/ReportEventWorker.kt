package com.settlement.mss.adapter.`in`.worker

import com.settlement.mss.application.service.ReportService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class ReportEventWorker(
    private val reportService: ReportService
) {
    @KafkaListener(topics = ["settlement.report.request"], groupId = "report-worker-group")
    fun handleEvent(message: Map<String, Any>) {
        // 메시지 파싱
        val merchantName = message["merchantName"] as String
        // 실제로는 DB조회나 message 안에 데이터가 더 있어야 함. 예시로 하드코딩.
        val totalSales = BigDecimal(1000000)
        val payout = BigDecimal(950000)

        // Application Service 호출
        reportService.generateAndSendReport(merchantName, totalSales, payout)
    }
}
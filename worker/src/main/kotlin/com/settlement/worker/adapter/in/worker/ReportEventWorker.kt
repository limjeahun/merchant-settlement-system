package com.settlement.worker.adapter.`in`.worker

import com.settlement.worker.application.port.`in`.GenerateReportUseCase
import com.settlement.common.event.ReportRequestEvent
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class ReportEventWorker(
    private val generateReportUseCase: GenerateReportUseCase
) {
    @KafkaListener(topics = ["settlement.report.request"], groupId = "report-worker-group")
    fun handleEvent(event: ReportRequestEvent) {
        // 가맹점 리포트 요청 정보
        val merchantId   = event.merchantId
        val merchantName = event.merchantName
        val startDate    = LocalDate.parse(event.startDate)
        val endDate      = LocalDate.parse(event.endDate)
        // API 호출 전 1초 강제 휴식
        Thread.sleep(5500)
        // Application Service 호출
        generateReportUseCase.generateAndSendReport(merchantId, merchantName, startDate, endDate)
    }
}
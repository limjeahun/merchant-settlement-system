package com.settlement.mss.batch.adapter.out.message

import com.settlement.mss.batch.application.port.out.PublishEventPort
import com.settlement.mss.common.event.ReportRequestEvent
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaAdapter(
    private val kafkaTemplate: KafkaTemplate<String, Any>
): PublishEventPort {
    override fun sendReportEvent(merchantId: Long, merchantName: String, startDate: String, endDate: String) {
        val event = ReportRequestEvent(
            merchantId   = merchantId,
            merchantName = merchantName,
            startDate    = startDate,
            endDate      = endDate
        )
        kafkaTemplate.send("settlement.report.request", merchantId.toString(), event)
    }
}
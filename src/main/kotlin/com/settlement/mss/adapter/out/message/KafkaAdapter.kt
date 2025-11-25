package com.settlement.mss.adapter.out.message

import com.settlement.mss.domain.port.out.PublishEventPort
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaAdapter(
    private val kafkaTemplate: KafkaTemplate<String, Any>
): PublishEventPort {
    override fun sendReportEvent(merchantId: Long, merchantName: String, startDate: String, endDate: String) {
        val event = mapOf(
            "merchantId" to merchantId,
            "merchantName" to merchantName,
            "period" to "$startDate ~ $endDate"
        )
        kafkaTemplate.send("settlement.report.request", merchantId.toString(), event)
    }
}
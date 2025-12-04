package com.settlement.mss.worker.adapter.out.persistence.mongo.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.LocalDateTime

@Document(collection = "settlement_reports")
class ReportDocument(
    @Id
    val id           : String? = null,
    val merchantId   : Long,
    val merchantName : String,
    val totalSales   : BigDecimal,
    val payoutAmount : BigDecimal,
    val reportContent: String,
    val createdAt    : LocalDateTime,
) {
}
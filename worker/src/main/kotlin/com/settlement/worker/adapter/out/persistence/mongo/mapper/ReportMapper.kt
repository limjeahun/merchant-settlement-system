package com.settlement.worker.adapter.out.persistence.mongo.mapper

import com.settlement.core.domain.model.SettlementReport
import com.settlement.mss.worker.adapter.out.persistence.mongo.document.ReportDocument
import org.springframework.stereotype.Component

@Component
class ReportMapper {
    fun toDocument(domain: SettlementReport): ReportDocument {
        return ReportDocument(
            id = domain.id,
            merchantId = domain.merchantId,
            merchantName = domain.merchantName,
            totalSales = domain.totalSales,
            payoutAmount = domain.payoutAmount,
            reportContent = domain.reportContent,
            createdAt = domain.createdAt
        )
    }

    fun toDomain(document: ReportDocument): SettlementReport {
        return SettlementReport(
            id = document.id,
            merchantId = document.merchantId,
            merchantName = document.merchantName,
            totalSales = document.totalSales,
            payoutAmount = document.payoutAmount,
            reportContent = document.reportContent,
            createdAt = document.createdAt
        )
    }
}
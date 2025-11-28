package com.settlement.mss.adapter.out.persistence

import com.settlement.mss.adapter.out.persistence.mongo.mapper.ReportMapper
import com.settlement.mss.adapter.out.persistence.mongo.repository.ReportMongoRepository
import com.settlement.mss.application.port.out.SaveReportPort
import com.settlement.mss.domain.model.SettlementReport
import org.springframework.stereotype.Component

@Component
class ReportPersistenceAdapter(
    private val reportRepository: ReportMongoRepository,
    private val reportMapper    : ReportMapper,
): SaveReportPort {
    override fun saveReport(report: SettlementReport): SettlementReport {
        val document = reportMapper.toDocument(report)
        val savedDocument = reportRepository.save(document)
        return reportMapper.toDomain(savedDocument)
    }

}
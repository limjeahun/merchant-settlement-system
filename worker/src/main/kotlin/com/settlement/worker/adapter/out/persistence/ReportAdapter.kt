package com.settlement.worker.adapter.out.persistence



import com.settlement.worker.application.port.out.SaveReportPort
import com.settlement.core.domain.model.SettlementReport
import com.settlement.mss.worker.adapter.out.persistence.mongo.mapper.ReportMapper
import com.settlement.mss.worker.adapter.out.persistence.mongo.repository.ReportMongoRepository
import org.springframework.stereotype.Component

@Component
class ReportAdapter(
    private val reportRepository: ReportMongoRepository,
    private val reportMapper    : ReportMapper,
): SaveReportPort {
    override fun saveReport(report: SettlementReport): SettlementReport {
        val document = reportMapper.toDocument(report)
        val savedDocument = reportRepository.save(document)
        return reportMapper.toDomain(savedDocument)
    }

}
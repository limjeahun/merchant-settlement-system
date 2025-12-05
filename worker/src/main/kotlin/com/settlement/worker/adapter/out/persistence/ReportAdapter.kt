package com.settlement.worker.adapter.out.persistence



import com.settlement.common.extensions.getLogger
import com.settlement.worker.application.port.out.SaveReportPort
import com.settlement.core.domain.model.SettlementReport
import com.settlement.worker.adapter.out.persistence.mongo.repository.ReportMongoRepository
import com.settlement.worker.adapter.out.persistence.mongo.mapper.ReportMapper
import org.springframework.stereotype.Component

@Component
class ReportAdapter(
    private val reportRepository: ReportMongoRepository,
    private val reportMapper    : ReportMapper,
): SaveReportPort {
    private val logger = getLogger()

    override fun saveReport(report: SettlementReport): SettlementReport {
        val document = reportMapper.toDocument(report)
        logger.info("레포트 저장 = {}", document.toString())
        val savedDocument = reportRepository.save(document)
        return reportMapper.toDomain(savedDocument)
    }

}
package com.settlement.mss.application.usecase

import com.settlement.mss.domain.port.out.AiAnalysisPort
import org.springframework.stereotype.Service

@Service
class ReportService(
    private val aiAnalysisPort: AiAnalysisPort,

) {
}
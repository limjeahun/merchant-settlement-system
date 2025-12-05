package com.settlement.worker.application.port.out

import com.settlement.core.domain.model.SettlementReport

interface SaveReportPort {
    fun saveReport(report: SettlementReport): SettlementReport
}
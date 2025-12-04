package com.settlement.mss.worker.application.port.out

import com.settlement.mss.core.domain.model.SettlementReport

interface SaveReportPort {
    fun saveReport(report: SettlementReport): SettlementReport
}
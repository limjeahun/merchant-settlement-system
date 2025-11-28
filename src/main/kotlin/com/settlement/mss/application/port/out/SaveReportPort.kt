package com.settlement.mss.application.port.out

import com.settlement.mss.domain.model.SettlementReport

interface SaveReportPort {
    fun saveReport(report: SettlementReport): SettlementReport
}
package com.settlement.mss.worker.application.port.out

interface AiAnalysisPort {
    /**
     * @param systemRole AI에게 부여할 역할
     * @param userPrompt 분석할 데이터와 질문
     * @return AI의 응답 텍스트
     */
    fun analyze(systemRole: String, userPrompt: String): String
}
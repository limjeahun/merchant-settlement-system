package com.settlement.mss.domain.port.out

interface AiAnalysisPort {
    /**
     * @param systemRole AI에게 부여할 역할 (예: 넌 금융 전문가야)
     * @param userPrompt 분석할 데이터와 질문
     * @return AI의 응답 텍스트
     */
    fun analyze(systemRole: String, userPrompt: String): String
}
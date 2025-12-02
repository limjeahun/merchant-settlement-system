package com.settlement.mss.adapter.out.external

import com.settlement.mss.adapter.out.external.dto.GeminiContent
import com.settlement.mss.adapter.out.external.dto.GeminiPart
import com.settlement.mss.adapter.out.external.dto.GeminiRequest
import com.settlement.mss.adapter.out.external.dto.GeminiResponse
import com.settlement.mss.application.port.out.AiAnalysisPort
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class GeminiAdapter(
    @Value("\${google.gemini.api-key}") private val apiKey: String
): AiAnalysisPort {
    private val restClient = RestClient.builder()
        .baseUrl("https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent")
        .build()

    override fun analyze(systemRole: String, userPrompt: String): String {
        // Gemini API 스펙에 맞춘 JSON 구조
        val requestBody = GeminiRequest(
            contents = listOf(
                GeminiContent(
                    parts = listOf(
                        GeminiPart(text = "$systemRole\n\n$userPrompt")
                    )
                )
            )
        )
        return parseResponse(requestBody)
    }

    private fun parseResponse(requestBody: GeminiRequest): String {
        return try {
            val response = restClient.post()
                .uri { it.queryParam("key", apiKey).build() }
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody) // 객체를 넣으면 알아서 JSON으로 변환됨
                .retrieve()
                .body(GeminiResponse::class.java) // DTO로 매핑
            extractText(response)
        }catch (e: Exception) {
            "AI 분석 실패: ${e.message}"
        }
    }

    private fun extractText(response: GeminiResponse?): String {
        return response?.candidates
            ?.firstOrNull()
            ?.content
            ?.parts
            ?.firstOrNull()
            ?.text
            ?: "분석 결과 없음"
    }
}
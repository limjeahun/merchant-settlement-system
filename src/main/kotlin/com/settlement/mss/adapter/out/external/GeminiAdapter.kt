package com.settlement.mss.adapter.out.external

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
        val requestBody = mapOf(
            "contents" to listOf(
                mapOf(
                    "parts" to listOf(
                        mapOf("text" to "$systemRole\n\n$userPrompt") // System Role을 프롬프트 앞단에 배치
                    )
                )
            )
        )

        val response = restClient.post()
            .uri { it.queryParam("key", apiKey).build() }
            .contentType(MediaType.APPLICATION_JSON)
            .body(requestBody)
            .retrieve()
            .body(String::class.java)

        return parseResponse(response)
    }

    private fun parseResponse(json: String?): String {
        // 간단한 파싱 로직 (실제로는 Jackson 등 사용 권장)
        // 응답 JSON 구조: candidates[0].content.parts[0].text
        return json?.substringAfter("\"text\": \"")?.substringBefore("\"")
            ?.replace("\\n", "\n") ?: "분석 실패"
    }
}
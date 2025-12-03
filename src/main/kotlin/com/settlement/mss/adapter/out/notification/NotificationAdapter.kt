package com.settlement.mss.adapter.out.notification

import com.settlement.mss.adapter.out.notification.dto.SlackMessageDto
import com.settlement.mss.application.port.out.NotificationPort
import com.settlement.mss.common.extensions.getLogger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import java.math.BigDecimal

@Component
class NotificationAdapter(
    private val mailSender: JavaMailSender, // Spring Boot Mail Starter 필요
    @Value("\${slack.webhook.url}") private val slackWebhookUrl: String,
    @Value("\${spring.mail.username}") private val fromEmail: String
): NotificationPort {
    private val logger = getLogger()
    private val restClient = RestClient.create()

    // 1. 주간 리포트 발송 (이메일)
    override fun sendSettlementReport(merchantName: String, email: String, reportContent: String) {
        try {
            val message = SimpleMailMessage().apply {
                from = fromEmail
                setTo(email)
                subject = "[정산 리포트] $merchantName 님, 주간 정산 분석 결과입니다."
                text = reportContent
            }

            mailSender.send(message)
            logger.info("📧 리포트 이메일 발송 성공: $merchantName ($email)")

        } catch (e: Exception) {
            logger.error("❌ 이메일 발송 실패: $email", e)
            // 실패 시 재시도 로직이나 DLQ 처리가 필요할 수 있음
        }
    }

    // 2. 대사 불일치 알림 (슬랙)
    override fun sendMismatchAlert(
        merchantId: Long,
        targetDate: String,
        expectedAmount: BigDecimal,
        actualAmount: BigDecimal
    ) {
        val alertMessage = """
            🚨 *[긴급] 정산 대사 불일치 발생!*
            - 가맹점 ID: `$merchantId`
            - 대상 일자: `$targetDate`
        """.trimIndent()

        val detailText = """
            - 예상 금액: ${expectedAmount}원
            - 실제 금액: ${actualAmount}원
            - 차액: ${expectedAmount.subtract(actualAmount)}원
        """.trimIndent()

        val payload = SlackMessageDto(
            text = alertMessage,
            attachments = listOf(
                SlackMessageDto.Attachment(
                    color = "#FF0000", // 빨간색 경고
                    title = "상세 정보",
                    text = detailText
                )
            )
        )

        try {
            restClient.post()
                .uri(slackWebhookUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve()
                .toBodilessEntity()

            logger.info("🚨 슬랙 알림 전송 완료: Merchant $merchantId")

        } catch (e: Exception) {
            logger.error("❌ 슬랙 알림 전송 실패", e)
        }
    }
}
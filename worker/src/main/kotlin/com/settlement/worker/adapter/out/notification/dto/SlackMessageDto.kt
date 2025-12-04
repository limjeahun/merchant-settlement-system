package com.settlement.mss.worker.adapter.out.notification.dto

data class SlackMessageDto(
    val text: String,
    val attachments: List<Attachment>? = null
) {
    data class Attachment(
        val color: String, // "#FF0000" (Red)
        val title: String,
        val text: String
    )
}

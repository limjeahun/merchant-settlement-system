package com.settlement.worker.config

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder

@Configuration
class KafkaTopicConfig {
    /**
     * 애플리케이션 시작 시 'settlement.report.request' 토픽을 자동으로 생성.
     */
    @Bean
    fun settlementReportTopic(): NewTopic {
        return TopicBuilder.name("settlement.report.request")
            .partitions(1) // 파티션 개수 (병렬 처리 개수)
            .replicas(1)   // 복제 개수(운영:3)
            .build()
    }
}
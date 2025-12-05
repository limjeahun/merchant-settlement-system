package com.settlement.batch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@ComponentScan(basePackages = ["com.settlement"])         // 1. 전체 컴포넌트 스캔 (Core + Batch)
@EnableJpaRepositories(basePackages = ["com.settlement"]) // 2. JPA 리포지토리 스캔 (Core)
@EntityScan(basePackages = ["com.settlement"])            // 3. JPA 엔티티 스캔 (Core)
class BatchApplication

fun main(args: Array<String>) {
    runApplication<BatchApplication>(*args)
}

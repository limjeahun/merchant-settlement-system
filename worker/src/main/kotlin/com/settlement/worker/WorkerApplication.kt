package com.settlement.worker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@ComponentScan(basePackages = ["com.settlement"])           // 1. 모든 컴포넌트(Service, Adapter) 스캔
@EnableJpaRepositories(basePackages = ["com.settlement"])   // 2. JPA 리포지토리 스캔 (Core 모듈)
@EntityScan(basePackages = ["com.settlement"])              // 3. JPA 엔티티 스캔 (Core 모듈)
@EnableMongoRepositories(basePackages = ["com.settlement"]) // 4. Mongo 리포지토리 스캔
class WorkerApplication

fun main(args: Array<String>) {
    runApplication<WorkerApplication>(*args)
}

package com.settlement.worker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["com.settlement"])
class WorkerApplication

fun main(args: Array<String>) {
    runApplication<WorkerApplication>(*args)
}

package com.settlement.batch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["com.settlement"])
class BatchApplication

fun main(args: Array<String>) {
    runApplication<BatchApplication>(*args)
}

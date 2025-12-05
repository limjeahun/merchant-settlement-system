package com.settlement.core.infrastructure.persistence.support

import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.support.DefaultTransactionDefinition
import org.springframework.transaction.support.TransactionTemplate

/**
 * TransactionTemplate 에서 PROPAGATION_REQUIRES_NEW 속성으로 블록을 실행하는 고차 확장 함수
 */
inline fun <T> TransactionTemplate.runInNewTransaction(crossinline block: () -> T): T? {
    val originalPropagation = this.propagationBehavior

    try {
        this.propagationBehavior = TransactionDefinition.PROPAGATION_REQUIRES_NEW
        return this.execute {
            block()
        }
    } finally {
        this.propagationBehavior = originalPropagation
    }
}

/**
 * 반환 값이 없는 (Unit) 함수
 */
inline fun TransactionTemplate.runInNewTransactionWithoutResult(crossinline block: () -> Unit) {
    val originalPropagation = this.propagationBehavior
    try {
        this.propagationBehavior = TransactionDefinition.PROPAGATION_REQUIRES_NEW
        this.executeWithoutResult {
            block()
        }
    } finally {
        this.propagationBehavior = originalPropagation
    }
}

/**
 * 별도의 읽기 전용 트랜잭션에서 실행 (REQUIRES_NEW + ReadOnly)
 * - DB 커넥션을 잠깐 쓰고 바로 반환해야 할 때 사용
 */
inline fun <T> PlatformTransactionManager.runInReadOnlyTransaction(crossinline block: () -> T): T? {
    // 매번 새로운 설정을 가진 템플릿을 생성하므로 스레드 안전함
    val definition = DefaultTransactionDefinition().apply {
        propagationBehavior = TransactionDefinition.PROPAGATION_REQUIRES_NEW
        isReadOnly = true // ✅ 읽기 전용 설정
    }

    return TransactionTemplate(this, definition).execute {
        block()
    }
}
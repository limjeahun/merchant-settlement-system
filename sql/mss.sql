-- ========================================================
-- 1. 가맹점 테이블 (merchants)
-- [참고 파일] src/main/kotlin/com/settlement/mss/adapter/out/persistence/jpa/entity/MerchantEntity.kt
-- ========================================================
CREATE TABLE merchants (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '가맹점 ID',
    name                VARCHAR(255) NOT NULL COMMENT '가맹점명',
    settlement_cycle    VARCHAR(50)  NOT NULL COMMENT '정산 주기 (DAILY, WEEKLY, MONTHLY)',
    business_type       VARCHAR(50)  NOT NULL COMMENT '사업자 유형 (CORPORATE, INDIVIDUAL)',
    platform_fee_rate   DECIMAL(19, 4) NOT NULL COMMENT '플랫폼 중개 수수료율',
    pg_fee_rate         DECIMAL(19, 4) NOT NULL COMMENT 'PG 결제 수수료율',
    support_delivery    TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '배송비 지원 여부 (1:True, 0:False)',

    -- [성능 최적화] 정산 배치 Reader에서 주기(Cycle)별 조회 시 사용
    INDEX idx_merchants_settlement_cycle (settlement_cycle)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ========================================================
-- 2. 주문 테이블 (orders)
-- [참고 파일] src/main/kotlin/com/settlement/mss/adapter/out/persistence/jpa/entity/OrderEntity.kt
-- ========================================================
CREATE TABLE orders (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '주문 ID',
    merchant_id         BIGINT NOT NULL COMMENT '가맹점 ID',
    ordered_at          DATETIME(6) NOT NULL COMMENT '주문 일시',
    amount              DECIMAL(19, 4) NOT NULL COMMENT '주문 총액 (상품 원가)',
    delivery_fee        DECIMAL(19, 4) NOT NULL DEFAULT 0 COMMENT '배송비',
    merchant_discount   DECIMAL(19, 4) NOT NULL DEFAULT 0 COMMENT '가맹점 부담 할인액',
    platform_discount   DECIMAL(19, 4) NOT NULL DEFAULT 0 COMMENT '플랫폼 부담 할인액',
    status              VARCHAR(50) NOT NULL COMMENT '주문 상태 (ORDERED, CANCELLED)',

    -- [성능 최적화] 정산 배치 Processor에서 특정 가맹점의 기간별 주문 조회 시 사용
    INDEX idx_orders_merchant_ordered_at (merchant_id, ordered_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ========================================================
-- 3. 정산 테이블 (settlements)
-- [참고 파일] src/main/kotlin/com/settlement/mss/adapter/out/persistence/jpa/entity/SettlementEntity.kt
-- ========================================================
CREATE TABLE settlements (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '정산 ID',
    merchant_id             BIGINT NOT NULL COMMENT '가맹점 ID',
    target_date             DATE NOT NULL COMMENT '정산 기준일 (주문일 기준 T-7)',

    -- 매출 관련
    total_order_amount      DECIMAL(19, 4) NOT NULL DEFAULT 0 COMMENT '총 주문 금액',
    total_delivery_amount   DECIMAL(19, 4) NOT NULL DEFAULT 0 COMMENT '총 배송비',
    total_revenue           DECIMAL(19, 4) NOT NULL DEFAULT 0 COMMENT '총 매출 (주문+배송)',

    -- 공제 관련
    total_discount_share    DECIMAL(19, 4) NOT NULL DEFAULT 0 COMMENT '가맹점 분담 할인 총액',
    platform_fee_amount     DECIMAL(19, 4) NOT NULL DEFAULT 0 COMMENT '플랫폼 수수료',
    pg_fee_amount           DECIMAL(19, 4) NOT NULL DEFAULT 0 COMMENT 'PG 수수료',
    vat_amount              DECIMAL(19, 4) NOT NULL DEFAULT 0 COMMENT '수수료에 대한 부가세(VAT)',

    -- 세금 및 결과
    withholding_tax_amount  DECIMAL(19, 4) NOT NULL DEFAULT 0 COMMENT '원천징수세 (개인사업자용)',
    payout_amount           DECIMAL(19, 4) NOT NULL DEFAULT 0 COMMENT '최종 지급액',
    status                  VARCHAR(50) NOT NULL COMMENT '정산 상태 (PENDING, COMPLETED, MISMATCH)',

    -- [성능 최적화] 리포트 발행 및 대사 작업 시 조회 속도 향상
    INDEX idx_settlements_target_date (target_date),
    INDEX idx_settlements_merchant_target (merchant_id, target_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
-- 1. 가맹점 테이블 (merchants)
CREATE TABLE merchants (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '가맹점 ID',
    name VARCHAR(255) NOT NULL COMMENT '가맹점명',
    settlement_cycle VARCHAR(50) NOT NULL COMMENT '정산 주기 (DAILY, WEEKLY, MONTHLY)',
    fee_rate DECIMAL(19, 4) NOT NULL COMMENT '수수료율',
    -- 성능 최적화를 위한 인덱스 (정산 배치에서 주기별 조회 시 사용)
    INDEX idx_merchants_settlement_cycle (settlement_cycle)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. 주문 테이블 (orders)
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '주문 ID',
    merchant_id BIGINT NOT NULL COMMENT '가맹점 ID',
    ordered_at DATETIME(6) NOT NULL COMMENT '주문 일시',
    amount DECIMAL(19, 4) NOT NULL COMMENT '주문 총액',
    merchant_discount DECIMAL(19, 4) NOT NULL COMMENT '가맹점 부담 할인액',
    platform_discount DECIMAL(19, 4) NOT NULL COMMENT '플랫폼 부담 할인액',
    status VARCHAR(50) NOT NULL COMMENT '주문 상태 (ORDERED, CANCELLED)',
    -- 성능 최적화를 위한 인덱스 (정산 배치에서 기간별 조회 시 사용)
    INDEX idx_orders_merchant_ordered_at (merchant_id, ordered_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. 정산 테이블 (settlements)
CREATE TABLE settlements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '정산 ID',
    merchant_id BIGINT NOT NULL COMMENT '가맹점 ID',
    target_date DATE NOT NULL COMMENT '정산 기준일 (T-7)',
    total_order_amount DECIMAL(19, 4) NOT NULL COMMENT '총 주문 금액',
    total_fee_amount DECIMAL(19, 4) NOT NULL COMMENT '총 수수료',
    total_discount_share DECIMAL(19, 4) NOT NULL COMMENT '가맹점 분담 할인 총액',
    payout_amount DECIMAL(19, 4) NOT NULL COMMENT '최종 지급액',
    status VARCHAR(50) NOT NULL COMMENT '정산 상태 (PENDING, COMPLETED, MISMATCH)',
    -- 성능 최적화를 위한 인덱스 (리포트 생성 및 대사 작업 시 조회)
    INDEX idx_settlements_target_date (target_date),
    INDEX idx_settlements_merchant_target (merchant_id, target_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
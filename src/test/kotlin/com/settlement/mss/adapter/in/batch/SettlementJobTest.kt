package com.settlement.mss.adapter.`in`.batch

import com.settlement.mss.application.port.`in`.*
import com.settlement.mss.domain.model.Settlement
import com.settlement.mss.domain.model.SettlementStatus
import com.settlement.mss.domain.service.BusinessDayPolicy
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.times
import org.mockito.BDDMockito.verify
import org.mockito.kotlin.any
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.Job
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.math.BigDecimal
import java.time.LocalDate

@SpringBatchTest // Spring Batch 테스트를 위한 어노테이션
@SpringBootTest  // 전체 컨텍스트 로드 (배치 설정 및 빈 등록)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class SettlementJobTest(
    private val jobLauncherTestUtils: JobLauncherTestUtils
) {
    @Autowired
    private lateinit var settlementJob: Job

    // 배치 설정에서 주입받는 의존성들을 Mocking 처리
    @MockitoBean private lateinit var businessDay: BusinessDayPolicy
    @MockitoBean private lateinit var findTargetUseCase: FindSettlementTargetUseCase
    @MockitoBean private lateinit var calculateUseCase: CalculateSettlementUseCase
    @MockitoBean private lateinit var saveUseCase: SaveSettlementUseCase
    @MockitoBean private lateinit var findDailySettlementsUseCase: FindDailySettlementsUseCase
    @MockitoBean private lateinit var sendReportEventUseCase: SendReportEventUseCase

    @Test
    @DisplayName("정산 배치가 정상적으로 실행되고 완료되어야 한다")
    fun settlementJob_success() {
        // Given
        val today = LocalDate.now()
        val targetDate = today.minusDays(7)
        val merchantIds = listOf(1L, 2L, 3L)

        // 1. 영업일 체크: 오늘은 영업일이라고 가정
        given(businessDay.isBusinessDay(today)).willReturn(true)
        // 2. Reader: 정산 대상 가맹점 ID 반환
        given(findTargetUseCase.findTargetMerchants(today)).willReturn(merchantIds)
        // 3. Processor : 정산계산
        val dummySettlement = createDummySettlement(1L, targetDate)
        given(calculateUseCase.calculateSettlement(1L, targetDate)).willReturn(dummySettlement)
        // 4. report
        given(findDailySettlementsUseCase.findSettlementsByDate(today)).willReturn(listOf(dummySettlement))
        // When
        val jobExecution = jobLauncherTestUtils.launchJob()
        // Then
        // 1. job 상태가 Complete 인지
        assertThat(jobExecution.status).isEqualTo(BatchStatus.COMPLETED)
        // 2. 각 Step 별 UseCase가 적절히 호출되었는지 확인
        verify(findTargetUseCase, times(1)).findTargetMerchants(today)
        // - 정산 계산은 가맹점 수(3개)만큼 호출되어야 함
        verify(calculateUseCase, times(3)).calculateSettlement(any(), any())
        // - 정산 저장은 Chunk Size 단위로 호출됨 (여기선 데이터가 적으니 1번)
        verify(saveUseCase, times(1)).saveSettlements(any())
    }


    private fun createDummySettlement(merchantId: Long, targetDate: LocalDate): Settlement {
        return Settlement(
            id = 1L,
            merchantId = merchantId,
            targetDate = targetDate,
            totalOrderAmount = BigDecimal("100000"),
            totalDeliveryAmount = BigDecimal("3000"),
            totalRevenue = BigDecimal("103000"),
            totalDiscountShare = BigDecimal("1000"),
            platformFeeAmount = BigDecimal("3000"),
            pgFeeAmount = BigDecimal("2000"),
            vatAmount = BigDecimal("500"),
            withholdingTaxAmount = BigDecimal("0"),
            payoutAmount = BigDecimal("96500"),
            status = SettlementStatus.PENDING
        )
    }


}
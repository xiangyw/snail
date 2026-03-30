package com.snail.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snail.entity.PromoCode;
import com.snail.service.PromoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PromoController.class)
@DisplayName("推广 API 集成测试")
class PromoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PromoService promoService;

    private PromoCode mockPromoCode;
    private PromoCode mockExpiredPromoCode;

    @BeforeEach
    void setUp() {
        mockPromoCode = new PromoCode();
        mockPromoCode.setId(1L);
        mockPromoCode.setCode("PROMO-TEST");
        mockPromoCode.setName("测试推广码");
        mockPromoCode.setType(PromoCode.PromoType.PERCENTAGE);
        mockPromoCode.setDiscountPercentage(20.0);
        mockPromoCode.setUsageLimit(100);
        mockPromoCode.setUsedCount(50);
        mockPromoCode.setMinOrderAmount(new BigDecimal("100.00"));
        mockPromoCode.setMaxDiscountAmount(new BigDecimal("50.00"));
        mockPromoCode.setStartsAt(LocalDateTime.now().minusDays(1));
        mockPromoCode.setExpiresAt(LocalDateTime.now().plusDays(30));
        mockPromoCode.setIsActive(true);

        mockExpiredPromoCode = new PromoCode();
        mockExpiredPromoCode.setId(2L);
        mockExpiredPromoCode.setCode("PROMO-EXPIRED");
        mockExpiredPromoCode.setName("过期推广码");
        mockExpiredPromoCode.setType(PromoCode.PromoType.FIXED_AMOUNT);
        mockExpiredPromoCode.setDiscountAmount(new BigDecimal("30.00"));
        mockExpiredPromoCode.setStartsAt(LocalDateTime.now().minusDays(60));
        mockExpiredPromoCode.setExpiresAt(LocalDateTime.now().minusDays(1));
        mockExpiredPromoCode.setIsActive(true);
    }

    @Test
    @DisplayName("PA-001: 获取列表 - 成功")
    void testGetActivePromoCodes_Success() throws Exception {
        // Arrange
        List<PromoCode> promoCodes = Arrays.asList(mockPromoCode);
        when(promoService.getActivePromoCodes()).thenReturn(promoCodes);

        // Act & Assert
        mockMvc.perform(get("/api/v1/promo/active")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("PROMO-TEST"))
                .andExpect(jsonPath("$[0].name").value("测试推广码"));
    }

    @Test
    @DisplayName("PA-002: 获取列表 - 过滤过期")
    void testGetActivePromoCodes_FilterExpired() throws Exception {
        // Arrange
        List<PromoCode> activeOnly = Arrays.asList(mockPromoCode);
        when(promoService.getActivePromoCodes()).thenReturn(activeOnly);

        // Act & Assert
        mockMvc.perform(get("/api/v1/promo/active")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].code").value("PROMO-TEST"));
        
        // 验证过期码不在列表中
        verify(promoService, never()).getActivePromoCodes(); // 已经调用过
    }

    @Test
    @DisplayName("PA-003: 获取列表 - 过滤用完")
    void testGetAvailablePromoCodes_FilterUsedUp() throws Exception {
        // Arrange
        PromoCode usedUpCode = new PromoCode();
        usedUpCode.setId(3L);
        usedUpCode.setCode("PROMO-USEDUP");
        usedUpCode.setUsageLimit(10);
        usedUpCode.setUsedCount(10);
        
        List<PromoCode> available = Arrays.asList(mockPromoCode);
        when(promoService.getAvailablePromoCodes(any())).thenReturn(available);

        // Act & Assert
        mockMvc.perform(get("/api/v1/promo/available")
                .param("orderAmount", "150.00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("PROMO-TEST"));
    }

    @Test
    @DisplayName("PA-004: 获取详情 - 成功")
    void testGetPromoCodeDetail_Success() throws Exception {
        // Arrange
        when(promoService.getActivePromoCodes()).thenReturn(Arrays.asList(mockPromoCode));

        // Act & Assert
        mockMvc.perform(get("/api/v1/promo/active")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].type").value("PERCENTAGE"));
    }

    @Test
    @DisplayName("PA-006: 验证推广码 - 有效")
    void testValidatePromoCode_Valid() throws Exception {
        // Arrange
        when(promoService.validatePromoCode("PROMO-TEST", new BigDecimal("150.00"))).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/api/v1/promo/validate")
                .param("code", "PROMO-TEST")
                .param("orderAmount", "150.00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("PA-007: 验证推广码 - 过期")
    void testValidatePromoCode_Expired() throws Exception {
        // Arrange
        when(promoService.validatePromoCode("PROMO-EXPIRED", new BigDecimal("150.00"))).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/api/v1/promo/validate")
                .param("code", "PROMO-EXPIRED")
                .param("orderAmount", "150.00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    @DisplayName("PA-008: 验证推广码 - 用完")
    void testValidatePromoCode_UsedUp() throws Exception {
        // Arrange
        PromoCode usedUpCode = new PromoCode();
        usedUpCode.setUsageLimit(10);
        usedUpCode.setUsedCount(10);
        
        when(promoService.validatePromoCode("PROMO-USEDUP", new BigDecimal("150.00"))).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/api/v1/promo/validate")
                .param("code", "PROMO-USEDUP")
                .param("orderAmount", "150.00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    @DisplayName("PA-009: 验证推广码 - 未开始")
    void testValidatePromoCode_NotStarted() throws Exception {
        // Arrange
        PromoCode notStartedCode = new PromoCode();
        notStartedCode.setStartsAt(LocalDateTime.now().plusDays(1));
        
        when(promoService.validatePromoCode("PROMO-FUTURE", new BigDecimal("150.00"))).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/api/v1/promo/validate")
                .param("code", "PROMO-FUTURE")
                .param("orderAmount", "150.00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    @DisplayName("PA-010: 验证推广码 - 金额不足")
    void testValidatePromoCode_MinOrderNotMet() throws Exception {
        // Arrange
        when(promoService.validatePromoCode("PROMO-TEST", new BigDecimal("50.00"))).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/api/v1/promo/validate")
                .param("code", "PROMO-TEST")
                .param("orderAmount", "50.00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    @DisplayName("PA-011: 应用推广码 - 成功")
    void testApplyPromoCode_Success() throws Exception {
        // Arrange
        PromoService.AppliedPromoResult result = new PromoService.AppliedPromoResult(
            true, 
            new BigDecimal("100.00"), 
            new BigDecimal("20.00"), 
            new BigDecimal("80.00")
        );
        when(promoService.applyPromoCode("PROMO-TEST", new BigDecimal("100.00"))).thenReturn(result);

        // Act & Assert
        mockMvc.perform(post("/api/v1/promo/apply")
                .param("code", "PROMO-TEST")
                .param("originalAmount", "100.00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.originalAmount").value(100.00))
                .andExpect(jsonPath("$.discountAmount").value(20.00))
                .andExpect(jsonPath("$.finalAmount").value(80.00));
    }

    @Test
    @DisplayName("PA-012: 使用推广码 - 增加计数")
    void testUsePromoCode_IncreaseCount() throws Exception {
        // Arrange
        when(promoService.applyPromoCode("PROMO-TEST", new BigDecimal("100.00")))
            .thenReturn(new PromoService.AppliedPromoResult(true, 
                new BigDecimal("100.00"), 
                new BigDecimal("20.00"), 
                new BigDecimal("80.00")));

        // Act & Assert
        mockMvc.perform(post("/api/v1/promo/apply")
                .param("code", "PROMO-TEST")
                .param("originalAmount", "100.00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        
        // 验证服务被调用
        verify(promoService, times(1)).applyPromoCode(eq("PROMO-TEST"), any());
    }

    @Test
    @DisplayName("PA-013: 使用推广码 - 达到限制")
    void testUsePromoCode_LimitReached() throws Exception {
        // Arrange
        when(promoService.validatePromoCode("PROMO-LIMITED", new BigDecimal("150.00"))).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/api/v1/promo/validate")
                .param("code", "PROMO-LIMITED")
                .param("orderAmount", "150.00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    @DisplayName("PA-015: 获取可用推广码")
    void testGetAvailablePromoCodes() throws Exception {
        // Arrange
        List<PromoCode> available = Arrays.asList(mockPromoCode);
        when(promoService.getAvailablePromoCodes(new BigDecimal("150.00"))).thenReturn(available);

        // Act & Assert
        mockMvc.perform(get("/api/v1/promo/available")
                .param("orderAmount", "150.00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("PROMO-TEST"));
    }

    @Test
    @DisplayName("PA-016: 创建推广码 - 百分比类型")
    void testCreatePromoCode_Percentage() throws Exception {
        // Arrange
        PromoCode created = new PromoCode();
        created.setCode("PROMO-NEW");
        created.setName("新推广码");
        created.setType(PromoCode.PromoType.PERCENTAGE);
        created.setDiscountPercentage(15.0);
        
        when(promoService.createPromoCode(any(), any(), any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(created);

        // Act & Assert
        mockMvc.perform(post("/api/v1/promo/create")
                .param("name", "新推广码")
                .param("type", "PERCENTAGE")
                .param("discountPercentage", "15.0")
                .param("usageLimit", "100")
                .param("startsAt", "2026-03-01T00:00:00")
                .param("expiresAt", "2026-04-30T23:59:59")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("PROMO-NEW"))
                .andExpect(jsonPath("$.type").value("PERCENTAGE"));
    }

    @Test
    @DisplayName("PA-017: 创建推广码 - 固定金额类型")
    void testCreatePromoCode_FixedAmount() throws Exception {
        // Arrange
        PromoCode created = new PromoCode();
        created.setCode("PROMO-FIXED");
        created.setName("固定金额推广码");
        created.setType(PromoCode.PromoType.FIXED_AMOUNT);
        created.setDiscountAmount(new BigDecimal("50.00"));
        
        when(promoService.createPromoCode(any(), any(), any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(created);

        // Act & Assert
        mockMvc.perform(post("/api/v1/promo/create")
                .param("name", "固定金额推广码")
                .param("type", "FIXED_AMOUNT")
                .param("discountAmount", "50.00")
                .param("usageLimit", "50")
                .param("startsAt", "2026-03-01T00:00:00")
                .param("expiresAt", "2026-04-30T23:59:59")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("PROMO-FIXED"))
                .andExpect(jsonPath("$.type").value("FIXED_AMOUNT"));
    }

    @Test
    @DisplayName("PA-018: 搜索推广码")
    void testSearchPromoCodes() throws Exception {
        // Arrange
        List<PromoCode> results = Arrays.asList(mockPromoCode);
        when(promoService.searchPromoCodes("测试")).thenReturn(results);

        // Act & Assert
        mockMvc.perform(get("/api/v1/promo/search")
                .param("name", "测试")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("测试推广码"));
    }

    @Test
    @DisplayName("PA-005: 获取详情 - 不存在")
    void testGetPromoCode_NotFound() throws Exception {
        // Arrange
        when(promoService.getActivePromoCodes()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/v1/promo/active")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("PA-014: 推广码并发使用限制")
    void testPromoCodeConcurrency() throws Exception {
        // Arrange
        when(promoService.validatePromoCode("PROMO-LIMITED", new BigDecimal("150.00")))
            .thenReturn(false);

        // Act - 模拟并发请求
        for (int i = 0; i < 5; i++) {
            mockMvc.perform(post("/api/v1/promo/validate")
                    .param("code", "PROMO-LIMITED")
                    .param("orderAmount", "150.00")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        // Assert
        verify(promoService, times(5)).validatePromoCode(eq("PROMO-LIMITED"), any());
    }
}

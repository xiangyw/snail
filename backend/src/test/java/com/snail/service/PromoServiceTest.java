package com.snail.service;

import com.snail.entity.PromoCode;
import com.snail.repository.PromoCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PromoServiceTest {
    
    @Mock
    private PromoCodeRepository promoCodeRepository;
    
    @InjectMocks
    private PromoService promoService;
    
    private static final String TEST_CODE = "PROMO-ABCD";
    private static final BigDecimal ORIGINAL_AMOUNT = new BigDecimal("100.00");
    
    @BeforeEach
    void setUp() {
    }
    
    @Test
    void testCreatePromoCode_Success() {
        // Arrange
        PromoCode expectedCode = new PromoCode();
        expectedCode.setCode(TEST_CODE);
        expectedCode.setName("Test Promo");
        expectedCode.setType(PromoCode.PromoType.PERCENTAGE);
        expectedCode.setDiscountPercentage(10.0);
        expectedCode.setIsActive(true);
        expectedCode.setStartsAt(LocalDateTime.now().minusDays(1));
        expectedCode.setExpiresAt(LocalDateTime.now().plusDays(30));
        
        when(promoCodeRepository.save(any(PromoCode.class))).thenReturn(expectedCode);
        
        // Act
        PromoCode result = promoService.createPromoCode(
                "Test Promo",
                PromoCode.PromoType.PERCENTAGE,
                null,
                10.0,
                100,
                null,
                null,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(30)
        );
        
        // Assert
        assertNotNull(result);
        assertEquals("Test Promo", result.getName());
        assertEquals(PromoCode.PromoType.PERCENTAGE, result.getType());
        verify(promoCodeRepository, times(1)).save(any(PromoCode.class));
    }
    
    @Test
    void testValidatePromoCode_CodeNotFound() {
        // Arrange
        when(promoCodeRepository.findByCode(TEST_CODE)).thenReturn(Optional.empty());
        
        // Act
        boolean result = promoService.validatePromoCode(TEST_CODE, ORIGINAL_AMOUNT);
        
        // Assert
        assertFalse(result);
        verify(promoCodeRepository, times(1)).findByCode(TEST_CODE);
    }
    
    @Test
    void testValidatePromoCode_NotActive() {
        // Arrange
        PromoCode inactiveCode = new PromoCode();
        inactiveCode.setIsActive(false);
        
        when(promoCodeRepository.findByCode(TEST_CODE)).thenReturn(Optional.of(inactiveCode));
        
        // Act
        boolean result = promoService.validatePromoCode(TEST_CODE, ORIGINAL_AMOUNT);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    void testValidatePromoCode_Expired() {
        // Arrange
        PromoCode expiredCode = new PromoCode();
        expiredCode.setIsActive(true);
        expiredCode.setStartsAt(LocalDateTime.now().minusDays(2));
        expiredCode.setExpiresAt(LocalDateTime.now().minusDays(1));
        
        when(promoCodeRepository.findByCode(TEST_CODE)).thenReturn(Optional.of(expiredCode));
        
        // Act
        boolean result = promoService.validatePromoCode(TEST_CODE, ORIGINAL_AMOUNT);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    void testValidatePromoCode_UsageLimitReached() {
        // Arrange
        PromoCode limitedCode = new PromoCode();
        limitedCode.setIsActive(true);
        limitedCode.setStartsAt(LocalDateTime.now().minusDays(1));
        limitedCode.setExpiresAt(LocalDateTime.now().plusDays(1));
        limitedCode.setUsageLimit(1);
        limitedCode.setUsedCount(1);
        
        when(promoCodeRepository.findByCode(TEST_CODE)).thenReturn(Optional.of(limitedCode));
        
        // Act
        boolean result = promoService.validatePromoCode(TEST_CODE, ORIGINAL_AMOUNT);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    void testValidatePromoCode_MinOrderAmountNotMet() {
        // Arrange
        PromoCode minOrderCode = new PromoCode();
        minOrderCode.setIsActive(true);
        minOrderCode.setStartsAt(LocalDateTime.now().minusDays(1));
        minOrderCode.setExpiresAt(LocalDateTime.now().plusDays(1));
        minOrderCode.setUsageLimit(0); // Unlimited
        minOrderCode.setMinOrderAmount(new BigDecimal("150.00")); // Higher than original amount
        
        when(promoCodeRepository.findByCode(TEST_CODE)).thenReturn(Optional.of(minOrderCode));
        
        // Act
        boolean result = promoService.validatePromoCode(TEST_CODE, ORIGINAL_AMOUNT);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    void testValidatePromoCode_ValidCode() {
        // Arrange
        PromoCode validCode = new PromoCode();
        validCode.setIsActive(true);
        validCode.setStartsAt(LocalDateTime.now().minusDays(1));
        validCode.setExpiresAt(LocalDateTime.now().plusDays(1));
        validCode.setUsageLimit(0); // Unlimited
        validCode.setMinOrderAmount(new BigDecimal("50.00")); // Lower than original amount
        
        when(promoCodeRepository.findByCode(TEST_CODE)).thenReturn(Optional.of(validCode));
        
        // Act
        boolean result = promoService.validatePromoCode(TEST_CODE, ORIGINAL_AMOUNT);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    void testApplyPromoCode_InvalidCode() {
        // Arrange
        when(promoCodeRepository.findByCode(TEST_CODE)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            promoService.applyPromoCode(TEST_CODE, ORIGINAL_AMOUNT);
        });
    }
    
    @Test
    void testApplyPromoCode_FixedAmountDiscount() {
        // Arrange
        PromoCode fixedAmountCode = new PromoCode();
        fixedAmountCode.setIsActive(true);
        fixedAmountCode.setStartsAt(LocalDateTime.now().minusDays(1));
        fixedAmountCode.setExpiresAt(LocalDateTime.now().plusDays(1));
        fixedAmountCode.setUsageLimit(10);
        fixedAmountCode.setUsedCount(0);
        fixedAmountCode.setType(PromoCode.PromoType.FIXED_AMOUNT);
        fixedAmountCode.setDiscountAmount(new BigDecimal("10.00"));
        
        when(promoCodeRepository.findByCode(TEST_CODE)).thenReturn(Optional.of(fixedAmountCode));
        when(promoCodeRepository.save(any(PromoCode.class))).thenReturn(fixedAmountCode);
        
        // Act
        PromoService.AppliedPromoResult result = promoService.applyPromoCode(TEST_CODE, ORIGINAL_AMOUNT);
        
        // Assert
        assertNotNull(result);
        assertEquals(new BigDecimal("90.00"), result.getFinalAmount());
        assertEquals(new BigDecimal("10.00"), result.getDiscountAmount());
        assertEquals(1, fixedAmountCode.getUsedCount()); // Usage count incremented
    }
    
    @Test
    void testApplyPromoCode_PercentageDiscount() {
        // Arrange
        PromoCode percentageCode = new PromoCode();
        percentageCode.setIsActive(true);
        percentageCode.setStartsAt(LocalDateTime.now().minusDays(1));
        percentageCode.setExpiresAt(LocalDateTime.now().plusDays(1));
        percentageCode.setUsageLimit(10);
        percentageCode.setUsedCount(0);
        percentageCode.setType(PromoCode.PromoType.PERCENTAGE);
        percentageCode.setDiscountPercentage(15.0);
        
        when(promoCodeRepository.findByCode(TEST_CODE)).thenReturn(Optional.of(percentageCode));
        when(promoCodeRepository.save(any(PromoCode.class))).thenReturn(percentageCode);
        
        // Act
        PromoService.AppliedPromoResult result = promoService.applyPromoCode(TEST_CODE, ORIGINAL_AMOUNT);
        
        // Assert
        assertNotNull(result);
        assertEquals(new BigDecimal("85.00"), result.getFinalAmount());
        assertEquals(new BigDecimal("15.00"), result.getDiscountAmount());
        assertEquals(1, percentageCode.getUsedCount()); // Usage count incremented
    }
    
    @Test
    void testApplyPromoCode_MaxDiscountLimit() {
        // Arrange
        PromoCode maxDiscountCode = new PromoCode();
        maxDiscountCode.setIsActive(true);
        maxDiscountCode.setStartsAt(LocalDateTime.now().minusDays(1));
        maxDiscountCode.setExpiresAt(LocalDateTime.now().plusDays(1));
        maxDiscountCode.setUsageLimit(10);
        maxDiscountCode.setUsedCount(0);
        maxDiscountCode.setType(PromoCode.PromoType.PERCENTAGE);
        maxDiscountCode.setDiscountPercentage(50.0); // Would give $50 discount
        maxDiscountCode.setMaxDiscountAmount(new BigDecimal("20.00")); // But capped at $20
        
        when(promoCodeRepository.findByCode(TEST_CODE)).thenReturn(Optional.of(maxDiscountCode));
        when(promoCodeRepository.save(any(PromoCode.class))).thenReturn(maxDiscountCode);
        
        // Act
        PromoService.AppliedPromoResult result = promoService.applyPromoCode(TEST_CODE, ORIGINAL_AMOUNT);
        
        // Assert
        assertNotNull(result);
        assertEquals(new BigDecimal("80.00"), result.getFinalAmount()); // $100 - $20 = $80
        assertEquals(new BigDecimal("20.00"), result.getDiscountAmount()); // Capped at $20
        assertEquals(1, maxDiscountCode.getUsedCount()); // Usage count incremented
    }
    
    @Test
    void testGetActivePromoCodes() {
        // Arrange
        List<PromoCode> activeCodes = Arrays.asList(
            createSamplePromoCode("ACTIVE1"),
            createSamplePromoCode("ACTIVE2")
        );
        
        when(promoCodeRepository.findActivePromoCodes(any(LocalDateTime.class))).thenReturn(activeCodes);
        
        // Act
        List<PromoCode> result = promoService.getActivePromoCodes();
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(promoCodeRepository, times(1)).findActivePromoCodes(any(LocalDateTime.class));
    }
    
    @Test
    void testGetAvailablePromoCodes() {
        // Arrange
        List<PromoCode> availableCodes = Arrays.asList(
            createSamplePromoCode("AVAILABLE1"),
            createSamplePromoCode("AVAILABLE2")
        );
        
        when(promoCodeRepository.findAvailablePromoCodes(any(LocalDateTime.class))).thenReturn(availableCodes);
        
        // Act
        List<PromoCode> result = promoService.getAvailablePromoCodes(ORIGINAL_AMOUNT);
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(promoCodeRepository, times(1)).findAvailablePromoCodes(any(LocalDateTime.class));
    }
    
    private PromoCode createSamplePromoCode(String name) {
        PromoCode code = new PromoCode();
        code.setName(name);
        code.setIsActive(true);
        code.setStartsAt(LocalDateTime.now().minusDays(1));
        code.setExpiresAt(LocalDateTime.now().plusDays(1));
        code.setUsageLimit(0); // Unlimited
        return code;
    }
}
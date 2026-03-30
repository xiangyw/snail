package com.snail.service;

import com.snail.entity.PromoCode;
import com.snail.repository.PromoCodeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Tag(name = "Promo Service", description = "推广码服务")
public class PromoService {
    
    private final PromoCodeRepository promoCodeRepository;
    
    /**
     * 创建推广码
     */
    @Operation(summary = "创建推广码", description = "创建一个新的推广码")
    public PromoCode createPromoCode(
            @Parameter(description = "推广码名称") String name,
            @Parameter(description = "推广码类型") PromoCode.PromoType type,
            @Parameter(description = "折扣金额") BigDecimal discountAmount,
            @Parameter(description = "折扣百分比") Double discountPercentage,
            @Parameter(description = "使用次数限制") Integer usageLimit,
            @Parameter(description = "最低订单金额") BigDecimal minOrderAmount,
            @Parameter(description = "最高折扣金额") BigDecimal maxDiscountAmount,
            @Parameter(description = "开始时间") LocalDateTime startsAt,
            @Parameter(description = "结束时间") LocalDateTime expiresAt) {
        
        PromoCode promoCode = new PromoCode();
        promoCode.setName(name);
        promoCode.setType(type);
        promoCode.setDiscountAmount(discountAmount);
        promoCode.setDiscountPercentage(discountPercentage);
        promoCode.setUsageLimit(usageLimit);
        promoCode.setMinOrderAmount(minOrderAmount);
        promoCode.setMaxDiscountAmount(maxDiscountAmount);
        promoCode.setStartsAt(startsAt);
        promoCode.setExpiresAt(expiresAt);
        
        return promoCodeRepository.save(promoCode);
    }
    
    /**
     * 验证推广码是否可用
     */
    @Operation(summary = "验证推广码", description = "验证推广码是否有效且可用")
    public boolean validatePromoCode(@Parameter(description = "推广码") String code, @Parameter(description = "订单金额") BigDecimal orderAmount) {
        Optional<PromoCode> promoCodeOpt = promoCodeRepository.findByCode(code);
        if (promoCodeOpt.isEmpty()) {
            log.warn("推广码不存在: {}", code);
            return false;
        }
        
        PromoCode promoCode = promoCodeOpt.get();
        
        // 检查是否活跃
        if (!promoCode.getIsActive()) {
            log.warn("推广码未激活: {}", code);
            return false;
        }
        
        // 检查是否过期
        LocalDateTime now = LocalDateTime.now();
        if (promoCode.getStartsAt() != null && promoCode.getStartsAt().isAfter(now)) {
            log.warn("推广码尚未开始: {}", code);
            return false;
        }
        if (promoCode.getExpiresAt() != null && promoCode.getExpiresAt().isBefore(now)) {
            log.warn("推广码已过期: {}", code);
            return false;
        }
        
        // 检查使用次数限制
        if (promoCode.getUsageLimit() > 0 && promoCode.getUsedCount() >= promoCode.getUsageLimit()) {
            log.warn("推广码使用次数已满: {}", code);
            return false;
        }
        
        // 检查最低订单金额
        if (promoCode.getMinOrderAmount() != null && orderAmount.compareTo(promoCode.getMinOrderAmount()) < 0) {
            log.warn("订单金额不足: {} < {}", orderAmount, promoCode.getMinOrderAmount());
            return false;
        }
        
        return true;
    }
    
    /**
     * 应用推广码折扣
     */
    @Operation(summary = "应用推广码", description = "应用推广码计算最终价格")
    @Transactional
    public AppliedPromoResult applyPromoCode(@Parameter(description = "推广码") String code, @Parameter(description = "原始金额") BigDecimal originalAmount) {
        if (!validatePromoCode(code, originalAmount)) {
            throw new IllegalArgumentException("推广码无效或不可用");
        }
        
        Optional<PromoCode> promoCodeOpt = promoCodeRepository.findByCode(code);
        PromoCode promoCode = promoCodeOpt.get();
        
        BigDecimal discountAmount = calculateDiscount(promoCode, originalAmount);
        
        // 限制最大折扣金额
        if (promoCode.getMaxDiscountAmount() != null && discountAmount.compareTo(promoCode.getMaxDiscountAmount()) > 0) {
            discountAmount = promoCode.getMaxDiscountAmount();
        }
        
        BigDecimal finalAmount = originalAmount.subtract(discountAmount);
        if (finalAmount.compareTo(BigDecimal.ZERO) < 0) {
            finalAmount = BigDecimal.ZERO;
        }
        
        // 增加使用计数
        promoCode.setUsedCount(promoCode.getUsedCount() + 1);
        promoCodeRepository.save(promoCode);
        
        return new AppliedPromoResult(finalAmount, discountAmount, promoCode);
    }
    
    /**
     * 计算折扣金额
     */
    private BigDecimal calculateDiscount(PromoCode promoCode, BigDecimal originalAmount) {
        switch (promoCode.getType()) {
            case FIXED_AMOUNT:
                return promoCode.getDiscountAmount() != null ? promoCode.getDiscountAmount() : BigDecimal.ZERO;
            case PERCENTAGE:
                if (promoCode.getDiscountPercentage() != null) {
                    BigDecimal percentage = BigDecimal.valueOf(promoCode.getDiscountPercentage()).divide(BigDecimal.valueOf(100));
                    return originalAmount.multiply(percentage).setScale(2, BigDecimal.ROUND_HALF_UP);
                }
                return BigDecimal.ZERO;
            case FREE_SHIPPING:
                // 免运费类型通常不减少商品金额，这里返回0
                return BigDecimal.ZERO;
            default:
                return BigDecimal.ZERO;
        }
    }
    
    /**
     * 获取活动推广码列表
     */
    @Operation(summary = "获取活动推广码", description = "获取当前有效的推广码")
    public List<PromoCode> getActivePromoCodes() {
        LocalDateTime now = LocalDateTime.now();
        return promoCodeRepository.findActivePromoCodes(now);
    }
    
    /**
     * 获取用户可用的推广码
     */
    @Operation(summary = "获取可用推广码", description = "根据订单金额获取可用的推广码")
    public List<PromoCode> getAvailablePromoCodes(@Parameter(description = "订单金额") BigDecimal orderAmount) {
        LocalDateTime now = LocalDateTime.now();
        return promoCodeRepository.findAvailablePromoCodes(now);
    }
    
    /**
     * 根据名称搜索推广码
     */
    @Operation(summary = "搜索推广码", description = "根据名称搜索推广码")
    public List<PromoCode> searchPromoCodes(@Parameter(description = "名称关键词") String name) {
        LocalDateTime now = LocalDateTime.now();
        return promoCodeRepository.findByNameContainingAndActive(name, now);
    }
    
    /**
     * 应用推广码结果类
     */
    @Schema(description = "应用推广码结果")
    public static class AppliedPromoResult {
        @Schema(description = "最终金额")
        private final BigDecimal finalAmount;
        
        @Schema(description = "折扣金额")
        private final BigDecimal discountAmount;
        
        @Schema(description = "使用的推广码信息")
        private final PromoCode promoCode;
        
        public AppliedPromoResult(BigDecimal finalAmount, BigDecimal discountAmount, PromoCode promoCode) {
            this.finalAmount = finalAmount;
            this.discountAmount = discountAmount;
            this.promoCode = promoCode;
        }
        
        // Getters
        public BigDecimal getFinalAmount() { return finalAmount; }
        public BigDecimal getDiscountAmount() { return discountAmount; }
        public PromoCode getPromoCode() { return promoCode; }
    }
}
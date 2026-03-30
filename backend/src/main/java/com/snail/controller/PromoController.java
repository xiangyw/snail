package com.snail.controller;

import com.snail.entity.PromoCode;
import com.snail.service.PromoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/promo")
@RequiredArgsConstructor
@Tag(name = "Promo API", description = "推广码相关API")
public class PromoController {
    
    private final PromoService promoService;
    
    /**
     * 创建推广码
     */
    @PostMapping("/create")
    @Operation(summary = "创建推广码", description = "创建一个新的推广码")
    public ResponseEntity<PromoCode> createPromoCode(
            @RequestParam @NotBlank(message = "推广码名称不能为空") String name,
            @RequestParam @NotNull(message = "推广码类型不能为空") PromoCode.PromoType type,
            @RequestParam(required = false) BigDecimal discountAmount,
            @RequestParam(required = false) Double discountPercentage,
            @RequestParam(defaultValue = "0") Integer usageLimit,
            @RequestParam(required = false) BigDecimal minOrderAmount,
            @RequestParam(required = false) BigDecimal maxDiscountAmount,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startsAt,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime expiresAt) {
        
        PromoCode promoCode = promoService.createPromoCode(
                name, type, discountAmount, discountPercentage, 
                usageLimit, minOrderAmount, maxDiscountAmount, 
                startsAt, expiresAt);
        
        return ResponseEntity.ok(promoCode);
    }
    
    /**
     * 验证推广码
     */
    @PostMapping("/validate")
    @Operation(summary = "验证推广码", description = "验证推广码是否有效且可用")
    public ResponseEntity<Boolean> validatePromoCode(
            @RequestParam @NotBlank(message = "推广码不能为空") String code,
            @RequestParam @NotNull(message = "订单金额不能为空") BigDecimal orderAmount) {
        
        boolean isValid = promoService.validatePromoCode(code, orderAmount);
        return ResponseEntity.ok(isValid);
    }
    
    /**
     * 应用推广码
     */
    @PostMapping("/apply")
    @Operation(summary = "应用推广码", description = "应用推广码计算最终价格")
    public ResponseEntity<PromoService.AppliedPromoResult> applyPromoCode(
            @RequestParam @NotBlank(message = "推广码不能为空") String code,
            @RequestParam @NotNull(message = "原始金额不能为空") BigDecimal originalAmount) {
        
        PromoService.AppliedPromoResult result = promoService.applyPromoCode(code, originalAmount);
        return ResponseEntity.ok(result);
    }
    
    /**
     * 获取活动推广码
     */
    @GetMapping("/active")
    @Operation(summary = "获取活动推广码", description = "获取当前有效的推广码")
    public ResponseEntity<List<PromoCode>> getActivePromoCodes() {
        
        List<PromoCode> promoCodes = promoService.getActivePromoCodes();
        return ResponseEntity.ok(promoCodes);
    }
    
    /**
     * 获取可用推广码
     */
    @GetMapping("/available")
    @Operation(summary = "获取可用推广码", description = "根据订单金额获取可用的推广码")
    public ResponseEntity<List<PromoCode>> getAvailablePromoCodes(
            @RequestParam @NotNull(message = "订单金额不能为空") BigDecimal orderAmount) {
        
        List<PromoCode> promoCodes = promoService.getAvailablePromoCodes(orderAmount);
        return ResponseEntity.ok(promoCodes);
    }
    
    /**
     * 搜索推广码
     */
    @GetMapping("/search")
    @Operation(summary = "搜索推广码", description = "根据名称搜索推广码")
    public ResponseEntity<List<PromoCode>> searchPromoCodes(
            @RequestParam @NotBlank(message = "搜索关键词不能为空") String name) {
        
        List<PromoCode> promoCodes = promoService.searchPromoCodes(name);
        return ResponseEntity.ok(promoCodes);
    }
}
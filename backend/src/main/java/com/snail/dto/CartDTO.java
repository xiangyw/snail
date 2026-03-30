package com.snail.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 购物车数据传输对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "购物车信息")
public class CartDTO {
    
    @Schema(description = "购物车ID")
    private Long id;
    
    @Schema(description = "用户ID")
    private Long userId;
    
    @Schema(description = "购物车项列表")
    private List<CartItemDTO> items;
    
    @Schema(description = "总数量")
    private Integer totalCount = 0;
    
    @Schema(description = "总金额")
    private BigDecimal totalAmount = BigDecimal.ZERO;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
    
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
    
    /**
     * 购物车项数据传输对象
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "购物车项信息")
    public static class CartItemDTO {
        
        @Schema(description = "购物车项ID")
        private Long id;
        
        @Schema(description = "商品信息")
        private ProductDTO product;
        
        @Schema(description = "商品数量", required = true)
        private Integer quantity;
        
        @Schema(description = "商品单价")
        private BigDecimal price;
        
        @Schema(description = "小计金额")
        private BigDecimal subtotal;
        
        @Schema(description = "创建时间")
        private LocalDateTime createdAt;
        
        @Schema(description = "更新时间")
        private LocalDateTime updatedAt;
    }
}
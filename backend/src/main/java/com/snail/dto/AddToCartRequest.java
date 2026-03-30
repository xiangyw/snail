package com.snail.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * 添加到购物车请求对象
 */
@Data
@Schema(description = "添加到购物车请求")
public class AddToCartRequest {
    
    @NotNull(message = "商品ID不能为空")
    @Schema(description = "商品ID", required = true)
    private Long productId;
    
    @NotNull(message = "商品数量不能为空")
    @Positive(message = "商品数量必须大于0")
    @Schema(description = "商品数量", required = true, example = "1")
    private Integer quantity = 1;
}
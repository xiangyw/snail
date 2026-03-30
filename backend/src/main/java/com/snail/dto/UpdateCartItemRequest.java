package com.snail.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * 更新购物车项请求对象
 */
@Data
@Schema(description = "更新购物车项请求")
public class UpdateCartItemRequest {
    
    @NotNull(message = "商品数量不能为空")
    @Positive(message = "商品数量必须大于0")
    @Schema(description = "商品数量", required = true, example = "2")
    private Integer quantity;
}
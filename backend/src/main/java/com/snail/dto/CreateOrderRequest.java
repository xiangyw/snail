package com.snail.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 创建订单请求对象
 */
@Data
@Schema(description = "创建订单请求")
public class CreateOrderRequest {
    
    @NotBlank(message = "收货地址不能为空")
    @Schema(description = "收货地址", required = true)
    private String shippingAddress;
    
    @NotBlank(message = "联系电话不能为空")
    @Schema(description = "联系电话", required = true)
    private String phoneNumber;
    
    @NotBlank(message = "收货人姓名不能为空")
    @Schema(description = "收货人姓名", required = true)
    private String receiverName;
    
    @Schema(description = "购物车项ID列表，如果不为空则从购物车创建订单")
    private List<Long> cartItemIds;
    
    @Schema(description = "商品项列表，如果直接购买则使用此字段")
    private List<OrderProductItem> productItems;
    
    /**
     * 订单商品项
     */
    @Data
    @Schema(description = "订单商品项")
    public static class OrderProductItem {
        
        @NotNull(message = "商品ID不能为空")
        @Schema(description = "商品ID", required = true)
        private Long productId;
        
        @NotNull(message = "商品数量不能为空")
        @Schema(description = "商品数量", required = true)
        private Integer quantity;
    }
}
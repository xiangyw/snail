package com.snail.dto;

import com.snail.entity.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单数据传输对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "订单信息")
public class OrderDTO {
    
    @Schema(description = "订单ID")
    private Long id;
    
    @Schema(description = "用户ID")
    private Long userId;
    
    @Schema(description = "订单号")
    private String orderNumber;
    
    @Schema(description = "订单状态")
    private Order.OrderStatus status;
    
    @NotNull(message = "订单总金额不能为空")
    @Schema(description = "订单总金额", required = true)
    private BigDecimal totalAmount;
    
    @NotBlank(message = "收货地址不能为空")
    @Schema(description = "收货地址", required = true)
    private String shippingAddress;
    
    @NotBlank(message = "联系电话不能为空")
    @Schema(description = "联系电话", required = true)
    private String phoneNumber;
    
    @NotBlank(message = "收货人姓名不能为空")
    @Schema(description = "收货人姓名", required = true)
    private String receiverName;
    
    @Schema(description = "订单项列表")
    private List<OrderItemDTO> items;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
    
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
    
    /**
     * 订单项数据传输对象
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "订单项信息")
    public static class OrderItemDTO {
        
        @Schema(description = "订单项ID")
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
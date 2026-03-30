package com.snail.admin.service;

import com.snail.entity.Order;
import com.snail.dto.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AdminOrderService {
    
    /**
     * 获取所有订单（分页）
     */
    Page<OrderDto> getAllOrders(Pageable pageable);
    
    /**
     * 根据ID获取订单
     */
    Optional<Order> getOrderById(Long id);
    
    /**
     * 更新订单状态
     */
    Order updateOrderStatus(Long orderId, String status);
    
    /**
     * 取消订单
     */
    Order cancelOrder(Long orderId);
    
    /**
     * 搜索订单
     */
    Page<OrderDto> searchOrders(String keyword, Pageable pageable);
    
    /**
     * 根据订单状态获取订单
     */
    Page<OrderDto> getOrdersByStatus(String status, Pageable pageable);
    
    /**
     * 根据用户ID获取订单
     */
    Page<OrderDto> getOrdersByUserId(Long userId, Pageable pageable);
    
    /**
     * 获取订单统计信息
     */
    OrderStatistics getOrderStatistics();
    
    /**
     * 获取总订单数
     */
    Long getTotalOrderCount();
    
    /**
     * 获取待处理订单数
     */
    Long getPendingOrderCount();
    
    /**
     * 获取已完成订单数
     */
    Long getCompletedOrderCount();
    
    /**
     * 获取总收入
     */
    Double getTotalRevenue();
    
    class OrderStatistics {
        private Long totalOrders;
        private Long pendingOrders;
        private Long paidOrders;
        private Long shippedOrders;
        private Long deliveredOrders;
        private Long cancelledOrders;
        private Long refundedOrders;
        private Double totalRevenue;
        
        public OrderStatistics(Long totalOrders, Long pendingOrders, Long paidOrders, Long shippedOrders,
                              Long deliveredOrders, Long cancelledOrders, Long refundedOrders, Double totalRevenue) {
            this.totalOrders = totalOrders;
            this.pendingOrders = pendingOrders;
            this.paidOrders = paidOrders;
            this.shippedOrders = shippedOrders;
            this.deliveredOrders = deliveredOrders;
            this.cancelledOrders = cancelledOrders;
            this.refundedOrders = refundedOrders;
            this.totalRevenue = totalRevenue;
        }
        
        // Getters and setters
        public Long getTotalOrders() { return totalOrders; }
        public void setTotalOrders(Long totalOrders) { this.totalOrders = totalOrders; }
        
        public Long getPendingOrders() { return pendingOrders; }
        public void setPendingOrders(Long pendingOrders) { this.pendingOrders = pendingOrders; }
        
        public Long getPaidOrders() { return paidOrders; }
        public void setPaidOrders(Long paidOrders) { this.paidOrders = paidOrders; }
        
        public Long getShippedOrders() { return shippedOrders; }
        public void setShippedOrders(Long shippedOrders) { this.shippedOrders = shippedOrders; }
        
        public Long getDeliveredOrders() { return deliveredOrders; }
        public void setDeliveredOrders(Long deliveredOrders) { this.deliveredOrders = deliveredOrders; }
        
        public Long getCancelledOrders() { return cancelledOrders; }
        public void setCancelledOrders(Long cancelledOrders) { this.cancelledOrders = cancelledOrders; }
        
        public Long getRefundedOrders() { return refundedOrders; }
        public void setRefundedOrders(Long refundedOrders) { this.refundedOrders = refundedOrders; }
        
        public Double getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(Double totalRevenue) { this.totalRevenue = totalRevenue; }
    }
}
package com.snail.service;

import com.snail.dto.CreateOrderRequest;
import com.snail.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 订单服务接口
 */
public interface OrderService {
    
    /**
     * 创建订单
     */
    OrderDTO createOrder(Long userId, CreateOrderRequest request);
    
    /**
     * 获取用户订单列表
     */
    Page<OrderDTO> getUserOrders(Long userId, Pageable pageable);
    
    /**
     * 根据订单号获取订单详情
     */
    OrderDTO getOrderByOrderNumber(String orderNumber);
    
    /**
     * 根据ID获取订单详情
     */
    OrderDTO getOrderById(Long orderId);
    
    /**
     * 取消订单
     */
    OrderDTO cancelOrder(Long userId, Long orderId);
    
    /**
     * 更新订单状态
     */
    OrderDTO updateOrderStatus(Long orderId, com.snail.entity.Order.OrderStatus status);
}
package com.snail.admin.service.impl;

import com.snail.admin.service.AdminOrderService;
import com.snail.entity.Order;
import com.snail.entity.Order.OrderStatus;
import com.snail.dto.OrderDto;
import com.snail.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AdminOrderServiceImpl implements AdminOrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Override
    public Page<OrderDto> getAllOrders(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        return orders.map(this::convertToDto);
    }
    
    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }
    
    @Override
    public Order updateOrderStatus(Long orderId, String status) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            try {
                OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
                order.setStatus(orderStatus);
                return orderRepository.save(order);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status: " + status);
            }
        } else {
            throw new RuntimeException("Order not found with id: " + orderId);
        }
    }
    
    @Override
    public Order cancelOrder(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus(OrderStatus.CANCELLED);
            return orderRepository.save(order);
        } else {
            throw new RuntimeException("Order not found with id: " + orderId);
        }
    }
    
    @Override
    public Page<OrderDto> searchOrders(String keyword, Pageable pageable) {
        Page<Order> orders = orderRepository.findByOrderNumberContainingIgnoreCaseOrReceiverNameContainingIgnoreCaseOrPhoneNumberContainingIgnoreCase(
            keyword, keyword, keyword, pageable);
        return orders.map(this::convertToDto);
    }
    
    @Override
    public Page<OrderDto> getOrdersByStatus(String status, Pageable pageable) {
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            Page<Order> orders = orderRepository.findByStatus(orderStatus, pageable);
            return orders.map(this::convertToDto);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }
    
    @Override
    public Page<OrderDto> getOrdersByUserId(Long userId, Pageable pageable) {
        Page<Order> orders = orderRepository.findByUserId(userId, pageable);
        return orders.map(this::convertToDto);
    }
    
    @Override
    public OrderStatistics getOrderStatistics() {
        Long totalOrders = orderRepository.count();
        Long pendingOrders = (long) orderRepository.findByStatus(OrderStatus.PENDING).size();
        Long paidOrders = (long) orderRepository.findByStatus(OrderStatus.PAID).size();
        Long shippedOrders = (long) orderRepository.findByStatus(OrderStatus.SHIPPED).size();
        Long deliveredOrders = (long) orderRepository.findByStatus(OrderStatus.DELIVERED).size();
        Long cancelledOrders = (long) orderRepository.findByStatus(OrderStatus.CANCELLED).size();
        Long refundedOrders = (long) orderRepository.findByStatus(OrderStatus.REFUNDED).size();
        BigDecimal totalRevenue = orderRepository.sumTotalAmountByPaidStatus();
        
        return new OrderStatistics(totalOrders, pendingOrders, paidOrders, shippedOrders,
                                  deliveredOrders, cancelledOrders, refundedOrders, 
                                  totalRevenue != null ? totalRevenue.doubleValue() : 0.0);
    }
    
    @Override
    public Long getTotalOrderCount() {
        return orderRepository.count();
    }
    
    @Override
    public Long getPendingOrderCount() {
        return orderRepository.countByStatus(OrderStatus.PENDING);
    }
    
    @Override
    public Long getCompletedOrderCount() {
        return orderRepository.countByStatus(OrderStatus.DELIVERED);
    }
    
    @Override
    public Double getTotalRevenue() {
        BigDecimal revenue = orderRepository.sumTotalAmountByPaidStatus();
        return revenue != null ? revenue.doubleValue() : 0.0;
    }
    
    private OrderDto convertToDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setPhoneNumber(order.getPhoneNumber());
        dto.setReceiverName(order.getReceiverName());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        return dto;
    }
}
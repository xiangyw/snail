package com.snail.repository;

import com.snail.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单数据访问接口
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    /**
     * 根据用户ID查询订单列表
     */
    Page<Order> findByUserId(Long userId, Pageable pageable);
    
    /**
     * 根据订单号查询订单
     */
    Order findByOrderNumber(String orderNumber);
    
    /**
     * 根据订单状态查询订单
     */
    List<Order> findByStatus(Order.OrderStatus status);
    
    /**
     * 根据订单状态分页查询
     */
    Page<Order> findByStatus(Order.OrderStatus status, Pageable pageable);
    
    /**
     * 模糊搜索订单（订单号、收货人姓名、电话号码）
     */
    Page<Order> findByOrderNumberContainingIgnoreCaseOrReceiverNameContainingIgnoreCaseOrPhoneNumberContainingIgnoreCase(
        String orderNumber, String receiverName, String phoneNumber, Pageable pageable);
    
    /**
     * 根据用户ID和订单状态查询订单
     */
    Page<Order> findByUserIdAndStatus(Long userId, Order.OrderStatus status, Pageable pageable);
    
    /**
     * 统计用户订单数量
     */
    long countByUserId(Long userId);
    
    /**
     * 统计用户已完成订单数量
     */
    long countByUserIdAndStatusIn(Long userId, List<Order.OrderStatus> statuses);
    
    /**
     * 统计订单总数
     */
    long countByStatus(Order.OrderStatus status);
    
    /**
     * 统计总收入
     */
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status IN ('PAID', 'SHIPPED', 'DELIVERED')")
    BigDecimal sumTotalAmountByPaidStatus();
    
    /**
     * 统计指定时间范围内的订单数量
     */
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    /**
     * 统计指定时间范围内的总收入
     */
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.createdAt BETWEEN :start AND :end AND o.status IN ('PAID', 'SHIPPED', 'DELIVERED')")
    BigDecimal sumTotalAmountByCreatedAtBetweenAndPaidStatus(LocalDateTime start, LocalDateTime end);
}
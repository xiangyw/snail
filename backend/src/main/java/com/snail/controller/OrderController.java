package com.snail.controller;

import com.snail.dto.CreateOrderRequest;
import com.snail.dto.OrderDTO;
import com.snail.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/api/orders")
@Tag(name = "订单管理", description = "订单相关操作接口")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @PostMapping
    @Operation(summary = "创建订单", description = "创建新订单")
    public ResponseEntity<OrderDTO> createOrder(@RequestParam Long userId, @Valid @RequestBody CreateOrderRequest request) {
        OrderDTO order = orderService.createOrder(userId, request);
        return ResponseEntity.ok(order);
    }
    
    @GetMapping
    @Operation(summary = "获取用户订单列表", description = "分页获取当前用户的订单列表")
    public ResponseEntity<Page<OrderDTO>> getUserOrders(@RequestParam Long userId,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderDTO> orders = orderService.getUserOrders(userId, pageable);
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/{orderId}")
    @Operation(summary = "获取订单详情", description = "根据ID获取订单详细信息")
    public ResponseEntity<OrderDTO> getOrderById(@RequestParam Long userId, @PathVariable Long orderId) {
        OrderDTO order = orderService.getOrderById(orderId);
        // 检查订单是否属于当前用户
        if (!order.getUserId().equals(userId)) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(order);
    }
    
    @GetMapping("/number/{orderNumber}")
    @Operation(summary = "根据订单号获取订单", description = "根据订单号获取订单详细信息")
    public ResponseEntity<OrderDTO> getOrderByOrderNumber(@RequestParam Long userId, @PathVariable String orderNumber) {
        OrderDTO order = orderService.getOrderByOrderNumber(orderNumber);
        // 检查订单是否属于当前用户
        if (!order.getUserId().equals(userId)) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(order);
    }
    
    @PutMapping("/{orderId}/cancel")
    @Operation(summary = "取消订单", description = "取消指定订单")
    public ResponseEntity<OrderDTO> cancelOrder(@RequestParam Long userId, @PathVariable Long orderId) {
        OrderDTO order = orderService.cancelOrder(userId, orderId);
        return ResponseEntity.ok(order);
    }
    
    @PutMapping("/{orderId}/status")
    @Operation(summary = "更新订单状态", description = "管理员更新订单状态")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Long orderId, 
                                                      @RequestParam com.snail.entity.Order.OrderStatus status) {
        OrderDTO order = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(order);
    }
}
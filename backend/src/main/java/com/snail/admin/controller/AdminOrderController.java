package com.snail.admin.controller;

import com.snail.admin.annotation.RequireAdmin;
import com.snail.admin.service.AdminOrderService;
import com.snail.dto.OrderDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin/orders")
@Tag(name = "Admin Orders", description = "管理员订单管理接口")
public class AdminOrderController {
    
    @Autowired
    private AdminOrderService adminOrderService;
    
    @GetMapping
    @Operation(summary = "获取所有订单", description = "分页获取所有订单信息")
    @RequireAdmin
    public ResponseEntity<Page<OrderDto>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderDto> orders = adminOrderService.getAllOrders(pageable);
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取订单", description = "根据订单ID获取订单详细信息")
    @RequireAdmin
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        Optional<com.snail.entity.Order> order = adminOrderService.getOrderById(id);
        if (order.isPresent()) {
            OrderDto dto = convertToDto(order.get());
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/status")
    @Operation(summary = "更新订单状态", description = "更新指定订单的状态")
    @RequireAdmin
    public ResponseEntity<com.snail.entity.Order> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        com.snail.entity.Order updatedOrder = adminOrderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }
    
    @PutMapping("/{id}/cancel")
    @Operation(summary = "取消订单", description = "取消指定订单")
    @RequireAdmin
    public ResponseEntity<com.snail.entity.Order> cancelOrder(@PathVariable Long id) {
        com.snail.entity.Order cancelledOrder = adminOrderService.cancelOrder(id);
        return ResponseEntity.ok(cancelledOrder);
    }
    
    @GetMapping("/search")
    @Operation(summary = "搜索订单", description = "根据关键词搜索订单")
    @RequireAdmin
    public ResponseEntity<Page<OrderDto>> searchOrders(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderDto> orders = adminOrderService.searchOrders(keyword, pageable);
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态获取订单", description = "根据订单状态获取订单列表")
    @RequireAdmin
    public ResponseEntity<Page<OrderDto>> getOrdersByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderDto> orders = adminOrderService.getOrdersByStatus(status, pageable);
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "根据用户ID获取订单", description = "根据用户ID获取该用户的所有订单")
    @RequireAdmin
    public ResponseEntity<Page<OrderDto>> getOrdersByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderDto> orders = adminOrderService.getOrdersByUserId(userId, pageable);
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/stats")
    @Operation(summary = "获取订单统计", description = "获取订单统计信息")
    @RequireAdmin
    public ResponseEntity<AdminOrderService.OrderStatistics> getOrderStatistics() {
        AdminOrderService.OrderStatistics stats = adminOrderService.getOrderStatistics();
        return ResponseEntity.ok(stats);
    }
    
    private OrderDto convertToDto(com.snail.entity.Order order) {
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
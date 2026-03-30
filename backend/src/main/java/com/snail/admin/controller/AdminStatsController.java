package com.snail.admin.controller;

import com.snail.admin.annotation.RequireAdmin;
import com.snail.admin.service.AdminStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/stats")
@Tag(name = "Admin Statistics", description = "管理员统计接口")
public class AdminStatsController {
    
    @Autowired
    private AdminStatsService adminStatsService;
    
    @GetMapping("/platform")
    @Operation(summary = "获取平台总体统计", description = "获取平台的整体统计数据")
    @RequireAdmin
    public ResponseEntity<AdminStatsService.PlatformStats> getPlatformStats() {
        AdminStatsService.PlatformStats stats = adminStatsService.getPlatformStats();
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/users")
    @Operation(summary = "获取用户统计", description = "获取用户相关统计数据")
    @RequireAdmin
    public ResponseEntity<AdminStatsService.UserStats> getUserStats() {
        AdminStatsService.UserStats stats = adminStatsService.getUserStats();
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/contents")
    @Operation(summary = "获取内容统计", description = "获取内容相关统计数据")
    @RequireAdmin
    public ResponseEntity<AdminStatsService.ContentStats> getContentStats() {
        AdminStatsService.ContentStats stats = adminStatsService.getContentStats();
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/tasks")
    @Operation(summary = "获取任务统计", description = "获取任务相关统计数据")
    @RequireAdmin
    public ResponseEntity<AdminStatsService.TaskStats> getTaskStats() {
        AdminStatsService.TaskStats stats = adminStatsService.getTaskStats();
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/orders")
    @Operation(summary = "获取订单统计", description = "获取订单相关统计数据")
    @RequireAdmin
    public ResponseEntity<AdminStatsService.OrderStats> getOrderStats() {
        AdminStatsService.OrderStats stats = adminStatsService.getOrderStats();
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/trends")
    @Operation(summary = "获取近期趋势", description = "获取近期数据趋势")
    @RequireAdmin
    public ResponseEntity<Map<String, Object>> getRecentTrends() {
        Map<String, Object> trends = adminStatsService.getRecentTrends();
        return ResponseEntity.ok(trends);
    }
    
    @GetMapping("/registrations")
    @Operation(summary = "获取每日注册统计", description = "获取每日用户注册统计数据")
    @RequireAdmin
    public ResponseEntity<Map<java.time.LocalDate, Long>> getDailyRegistrationStats(
            @RequestParam(defaultValue = "30") int days) {
        Map<java.time.LocalDate, Long> stats = adminStatsService.getDailyRegistrationStats(days);
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/active-users")
    @Operation(summary = "获取每日活跃用户统计", description = "获取每日活跃用户统计数据")
    @RequireAdmin
    public ResponseEntity<Map<java.time.LocalDate, Long>> getDailyActiveUsersStats(
            @RequestParam(defaultValue = "30") int days) {
        Map<java.time.LocalDate, Long> stats = adminStatsService.getDailyActiveUsersStats(days);
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/revenue")
    @Operation(summary = "获取每日收入统计", description = "获取每日收入统计数据")
    @RequireAdmin
    public ResponseEntity<Map<java.time.LocalDate, Double>> getDailyRevenueStats(
            @RequestParam(defaultValue = "30") int days) {
        Map<java.time.LocalDate, Double> stats = adminStatsService.getDailyRevenueStats(days);
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/locations")
    @Operation(summary = "获取用户地域分布", description = "获取用户地域分布统计数据")
    @RequireAdmin
    public ResponseEntity<Map<String, Long>> getUserLocationStats() {
        Map<String, Long> stats = adminStatsService.getUserLocationStats();
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/devices")
    @Operation(summary = "获取设备类型分布", description = "获取设备类型分布统计数据")
    @RequireAdmin
    public ResponseEntity<Map<String, Long>> getDeviceTypeStats() {
        Map<String, Long> stats = adminStatsService.getDeviceTypeStats();
        return ResponseEntity.ok(stats);
    }
}
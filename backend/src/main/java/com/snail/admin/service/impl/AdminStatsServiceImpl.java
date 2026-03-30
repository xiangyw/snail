package com.snail.admin.service.impl;

import com.snail.admin.service.AdminStatsService;
import com.snail.entity.Order;
import com.snail.repository.ContentRepository;
import com.snail.repository.OrderRepository;
import com.snail.repository.TaskRepository;
import com.snail.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminStatsServiceImpl implements AdminStatsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ContentRepository contentRepository;
    
    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Override
    public PlatformStats getPlatformStats() {
        Long totalUsers = userRepository.count();
        Long totalContents = contentRepository.count();
        Long totalTasks = taskRepository.count();
        Long totalOrders = orderRepository.count();
        Double totalRevenue = orderRepository.sumTotalAmountByPaidStatus() != null ? 
                             orderRepository.sumTotalAmountByPaidStatus().doubleValue() : 0.0;
        Long activeUsersToday = userRepository.count(); // This would need to be updated to actual active users
        Long newUsersToday = userRepository.count(); // This would need to be updated to actual new users today
        Long newContentsToday = contentRepository.countToday() != null ? contentRepository.countToday() : 0;
        Long newOrdersToday = orderRepository.countByCreatedAtBetween(
            LocalDateTime.now().withHour(0).withMinute(0).withSecond(0),
            LocalDateTime.now().withHour(23).withMinute(59).withSecond(59));
        
        return new PlatformStats(totalUsers, totalContents, totalTasks, totalOrders,
                               totalRevenue, activeUsersToday, newUsersToday, 
                               newContentsToday, newOrdersToday);
    }
    
    @Override
    public UserStats getUserStats() {
        Long totalUsers = userRepository.count();
        Long activeUsers = userRepository.countByIsActiveTrue();
        Long inactiveUsers = totalUsers - activeUsers;
        Long adminUsers = userRepository.countByRole(com.snail.entity.UserRole.ADMIN);
        Long moderatorUsers = userRepository.countByRole(com.snail.entity.UserRole.MODERATOR);
        Long regularUsers = userRepository.countByRole(com.snail.entity.UserRole.USER);
        
        return new UserStats(totalUsers, activeUsers, inactiveUsers, adminUsers, 
                           moderatorUsers, regularUsers);
    }
    
    @Override
    public ContentStats getContentStats() {
        Long totalContents = contentRepository.count();
        Long publishedContents = contentRepository.countByStatus(com.snail.entity.Content.ContentStatus.PUBLISHED);
        Long draftContents = contentRepository.countByStatus(com.snail.entity.Content.ContentStatus.DRAFT);
        Long archivedContents = contentRepository.countByStatus(com.snail.entity.Content.ContentStatus.ARCHIVED);
        Long pendingReviewContents = contentRepository.countByStatus(com.snail.entity.Content.ContentStatus.DRAFT);
        
        return new ContentStats(totalContents, publishedContents, draftContents, 
                               archivedContents, pendingReviewContents);
    }
    
    @Override
    public TaskStats getTaskStats() {
        Long totalTasks = taskRepository.count();
        Long activeTasks = taskRepository.countActiveTasks();
        Long inactiveTasks = taskRepository.countByStatus(com.snail.entity.Task.TaskStatus.INACTIVE);
        Long completedTasks = totalTasks; // Placeholder - actual completion stats would require more logic
        Long dailyTasks = taskRepository.countByType(com.snail.entity.Task.TaskType.DAILY);
        Long weeklyTasks = taskRepository.countByType(com.snail.entity.Task.TaskType.WEEKLY);
        Long oneTimeTasks = taskRepository.countByType(com.snail.entity.Task.TaskType.ONE_TIME);
        Long contentCreationTasks = taskRepository.countByType(com.snail.entity.Task.TaskType.CONTENT_CREATION);
        
        return new TaskStats(totalTasks, activeTasks, inactiveTasks, completedTasks,
                           dailyTasks, weeklyTasks, oneTimeTasks, contentCreationTasks);
    }
    
    @Override
    public OrderStats getOrderStats() {
        Long totalOrders = orderRepository.count();
        Long pendingOrders = orderRepository.countByStatus(Order.OrderStatus.PENDING);
        Long paidOrders = orderRepository.countByStatus(Order.OrderStatus.PAID);
        Long shippedOrders = orderRepository.countByStatus(Order.OrderStatus.SHIPPED);
        Long deliveredOrders = orderRepository.countByStatus(Order.OrderStatus.DELIVERED);
        Long cancelledOrders = orderRepository.countByStatus(Order.OrderStatus.CANCELLED);
        Long refundedOrders = orderRepository.countByStatus(Order.OrderStatus.REFUNDED);
        Double totalRevenue = orderRepository.sumTotalAmountByPaidStatus() != null ? 
                             orderRepository.sumTotalAmountByPaidStatus().doubleValue() : 0.0;
        
        return new OrderStats(totalOrders, pendingOrders, paidOrders, shippedOrders,
                             deliveredOrders, cancelledOrders, refundedOrders, totalRevenue);
    }
    
    @Override
    public Map<String, Object> getRecentTrends() {
        Map<String, Object> trends = new HashMap<>();
        
        // Placeholder implementation - would need actual trend calculation
        trends.put("daily_new_users", userRepository.count());
        trends.put("daily_new_contents", contentRepository.countToday());
        trends.put("daily_new_orders", orderRepository.countByCreatedAtBetween(
            LocalDateTime.now().withHour(0).withMinute(0).withSecond(0),
            LocalDateTime.now().withHour(23).withMinute(59).withSecond(59)));
        trends.put("daily_revenue", orderRepository.sumTotalAmountByPaidStatus());
        
        return trends;
    }
    
    @Override
    public Map<LocalDate, Long> getDailyRegistrationStats(int days) {
        Map<LocalDate, Long> stats = new HashMap<>();
        // Placeholder implementation - would need actual daily registration logic
        LocalDate currentDate = LocalDate.now();
        for (int i = 0; i < days; i++) {
            LocalDate date = currentDate.minusDays(i);
            stats.put(date, 0L); // Placeholder
        }
        return stats;
    }
    
    @Override
    public Map<LocalDate, Long> getDailyActiveUsersStats(int days) {
        Map<LocalDate, Long> stats = new HashMap<>();
        // Placeholder implementation - would need actual daily active user logic
        LocalDate currentDate = LocalDate.now();
        for (int i = 0; i < days; i++) {
            LocalDate date = currentDate.minusDays(i);
            stats.put(date, 0L); // Placeholder
        }
        return stats;
    }
    
    @Override
    public Map<LocalDate, Double> getDailyRevenueStats(int days) {
        Map<LocalDate, Double> stats = new HashMap<>();
        // Placeholder implementation - would need actual daily revenue logic
        LocalDate currentDate = LocalDate.now();
        for (int i = 0; i < days; i++) {
            LocalDate date = currentDate.minusDays(i);
            stats.put(date, 0.0); // Placeholder
        }
        return stats;
    }
    
    @Override
    public Map<String, Long> getUserLocationStats() {
        Map<String, Long> stats = new HashMap<>();
        // Placeholder implementation - would need actual location data
        stats.put("Unknown", userRepository.count());
        return stats;
    }
    
    @Override
    public Map<String, Long> getDeviceTypeStats() {
        Map<String, Long> stats = new HashMap<>();
        // Placeholder implementation - would need actual device data
        stats.put("Unknown", userRepository.count());
        return stats;
    }
}
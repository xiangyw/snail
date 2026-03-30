package com.snail.admin.service;

import java.time.LocalDate;
import java.util.Map;

public interface AdminStatsService {
    
    /**
     * 获取平台总体统计数据
     */
    PlatformStats getPlatformStats();
    
    /**
     * 获取用户相关统计
     */
    UserStats getUserStats();
    
    /**
     * 获取内容相关统计
     */
    ContentStats getContentStats();
    
    /**
     * 获取任务相关统计
     */
    TaskStats getTaskStats();
    
    /**
     * 获取订单相关统计
     */
    OrderStats getOrderStats();
    
    /**
     * 获取最近7天的数据趋势
     */
    Map<String, Object> getRecentTrends();
    
    /**
     * 获取每日注册用户数（最近30天）
     */
    Map<LocalDate, Long> getDailyRegistrationStats(int days);
    
    /**
     * 获取每日活跃用户数（最近30天）
     */
    Map<LocalDate, Long> getDailyActiveUsersStats(int days);
    
    /**
     * 获取每日收入统计（最近30天）
     */
    Map<LocalDate, Double> getDailyRevenueStats(int days);
    
    /**
     * 获取用户地域分布
     */
    Map<String, Long> getUserLocationStats();
    
    /**
     * 获取设备类型分布
     */
    Map<String, Long> getDeviceTypeStats();
    
    class PlatformStats {
        private Long totalUsers;
        private Long totalContents;
        private Long totalTasks;
        private Long totalOrders;
        private Double totalRevenue;
        private Long activeUsersToday;
        private Long newUsersToday;
        private Long newContentsToday;
        private Long newOrdersToday;
        
        public PlatformStats(Long totalUsers, Long totalContents, Long totalTasks, Long totalOrders,
                            Double totalRevenue, Long activeUsersToday, Long newUsersToday, 
                            Long newContentsToday, Long newOrdersToday) {
            this.totalUsers = totalUsers;
            this.totalContents = totalContents;
            this.totalTasks = totalTasks;
            this.totalOrders = totalOrders;
            this.totalRevenue = totalRevenue;
            this.activeUsersToday = activeUsersToday;
            this.newUsersToday = newUsersToday;
            this.newContentsToday = newContentsToday;
            this.newOrdersToday = newOrdersToday;
        }
        
        // Getters and setters
        public Long getTotalUsers() { return totalUsers; }
        public void setTotalUsers(Long totalUsers) { this.totalUsers = totalUsers; }
        
        public Long getTotalContents() { return totalContents; }
        public void setTotalContents(Long totalContents) { this.totalContents = totalContents; }
        
        public Long getTotalTasks() { return totalTasks; }
        public void setTotalTasks(Long totalTasks) { this.totalTasks = totalTasks; }
        
        public Long getTotalOrders() { return totalOrders; }
        public void setTotalOrders(Long totalOrders) { this.totalOrders = totalOrders; }
        
        public Double getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(Double totalRevenue) { this.totalRevenue = totalRevenue; }
        
        public Long getActiveUsersToday() { return activeUsersToday; }
        public void setActiveUsersToday(Long activeUsersToday) { this.activeUsersToday = activeUsersToday; }
        
        public Long getNewUsersToday() { return newUsersToday; }
        public void setNewUsersToday(Long newUsersToday) { this.newUsersToday = newUsersToday; }
        
        public Long getNewContentsToday() { return newContentsToday; }
        public void setNewContentsToday(Long newContentsToday) { this.newContentsToday = newContentsToday; }
        
        public Long getNewOrdersToday() { return newOrdersToday; }
        public void setNewOrdersToday(Long newOrdersToday) { this.newOrdersToday = newOrdersToday; }
    }
    
    class UserStats {
        private Long totalUsers;
        private Long activeUsers;
        private Long inactiveUsers;
        private Long adminUsers;
        private Long moderatorUsers;
        private Long regularUsers;
        
        public UserStats(Long totalUsers, Long activeUsers, Long inactiveUsers, Long adminUsers, 
                        Long moderatorUsers, Long regularUsers) {
            this.totalUsers = totalUsers;
            this.activeUsers = activeUsers;
            this.inactiveUsers = inactiveUsers;
            this.adminUsers = adminUsers;
            this.moderatorUsers = moderatorUsers;
            this.regularUsers = regularUsers;
        }
        
        // Getters and setters
        public Long getTotalUsers() { return totalUsers; }
        public void setTotalUsers(Long totalUsers) { this.totalUsers = totalUsers; }
        
        public Long getActiveUsers() { return activeUsers; }
        public void setActiveUsers(Long activeUsers) { this.activeUsers = activeUsers; }
        
        public Long getInactiveUsers() { return inactiveUsers; }
        public void setInactiveUsers(Long inactiveUsers) { this.inactiveUsers = inactiveUsers; }
        
        public Long getAdminUsers() { return adminUsers; }
        public void setAdminUsers(Long adminUsers) { this.adminUsers = adminUsers; }
        
        public Long getModeratorUsers() { return moderatorUsers; }
        public void setModeratorUsers(Long moderatorUsers) { this.moderatorUsers = moderatorUsers; }
        
        public Long getRegularUsers() { return regularUsers; }
        public void setRegularUsers(Long regularUsers) { this.regularUsers = regularUsers; }
    }
    
    class ContentStats {
        private Long totalContents;
        private Long publishedContents;
        private Long draftContents;
        private Long archivedContents;
        private Long pendingReviewContents;
        
        public ContentStats(Long totalContents, Long publishedContents, Long draftContents, 
                           Long archivedContents, Long pendingReviewContents) {
            this.totalContents = totalContents;
            this.publishedContents = publishedContents;
            this.draftContents = draftContents;
            this.archivedContents = archivedContents;
            this.pendingReviewContents = pendingReviewContents;
        }
        
        // Getters and setters
        public Long getTotalContents() { return totalContents; }
        public void setTotalContents(Long totalContents) { this.totalContents = totalContents; }
        
        public Long getPublishedContents() { return publishedContents; }
        public void setPublishedContents(Long publishedContents) { this.publishedContents = publishedContents; }
        
        public Long getDraftContents() { return draftContents; }
        public void setDraftContents(Long draftContents) { this.draftContents = draftContents; }
        
        public Long getArchivedContents() { return archivedContents; }
        public void setArchivedContents(Long archivedContents) { this.archivedContents = archivedContents; }
        
        public Long getPendingReviewContents() { return pendingReviewContents; }
        public void setPendingReviewContents(Long pendingReviewContents) { this.pendingReviewContents = pendingReviewContents; }
    }
    
    class TaskStats {
        private Long totalTasks;
        private Long activeTasks;
        private Long inactiveTasks;
        private Long completedTasks;
        private Long dailyTasks;
        private Long weeklyTasks;
        private Long oneTimeTasks;
        private Long contentCreationTasks;
        
        public TaskStats(Long totalTasks, Long activeTasks, Long inactiveTasks, Long completedTasks,
                        Long dailyTasks, Long weeklyTasks, Long oneTimeTasks, Long contentCreationTasks) {
            this.totalTasks = totalTasks;
            this.activeTasks = activeTasks;
            this.inactiveTasks = inactiveTasks;
            this.completedTasks = completedTasks;
            this.dailyTasks = dailyTasks;
            this.weeklyTasks = weeklyTasks;
            this.oneTimeTasks = oneTimeTasks;
            this.contentCreationTasks = contentCreationTasks;
        }
        
        // Getters and setters
        public Long getTotalTasks() { return totalTasks; }
        public void setTotalTasks(Long totalTasks) { this.totalTasks = totalTasks; }
        
        public Long getActiveTasks() { return activeTasks; }
        public void setActiveTasks(Long activeTasks) { this.activeTasks = activeTasks; }
        
        public Long getInactiveTasks() { return inactiveTasks; }
        public void setInactiveTasks(Long inactiveTasks) { this.inactiveTasks = inactiveTasks; }
        
        public Long getCompletedTasks() { return completedTasks; }
        public void setCompletedTasks(Long completedTasks) { this.completedTasks = completedTasks; }
        
        public Long getDailyTasks() { return dailyTasks; }
        public void setDailyTasks(Long dailyTasks) { this.dailyTasks = dailyTasks; }
        
        public Long getWeeklyTasks() { return weeklyTasks; }
        public void setWeeklyTasks(Long weeklyTasks) { this.weeklyTasks = weeklyTasks; }
        
        public Long getOneTimeTasks() { return oneTimeTasks; }
        public void setOneTimeTasks(Long oneTimeTasks) { this.oneTimeTasks = oneTimeTasks; }
        
        public Long getContentCreationTasks() { return contentCreationTasks; }
        public void setContentCreationTasks(Long contentCreationTasks) { this.contentCreationTasks = contentCreationTasks; }
    }
    
    class OrderStats {
        private Long totalOrders;
        private Long pendingOrders;
        private Long paidOrders;
        private Long shippedOrders;
        private Long deliveredOrders;
        private Long cancelledOrders;
        private Long refundedOrders;
        private Double totalRevenue;
        
        public OrderStats(Long totalOrders, Long pendingOrders, Long paidOrders, Long shippedOrders,
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
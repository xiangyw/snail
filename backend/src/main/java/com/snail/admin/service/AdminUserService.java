package com.snail.admin.service;

import com.snail.entity.User;
import com.snail.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AdminUserService {
    
    /**
     * 获取所有用户列表（分页）
     */
    Page<UserDto> getAllUsers(Pageable pageable);
    
    /**
     * 根据ID获取用户
     */
    Optional<User> getUserById(Long id);
    
    /**
     * 更新用户角色
     */
    User updateUserRole(Long userId, String role);
    
    /**
     * 禁用/启用用户
     */
    User toggleUserStatus(Long userId, Boolean active);
    
    /**
     * 删除用户（软删除）
     */
    User deleteUser(Long userId);
    
    /**
     * 根据用户名或邮箱搜索用户
     */
    Page<UserDto> searchUsers(String keyword, Pageable pageable);
    
    /**
     * 获取用户统计信息
     */
    UserStatistics getUserStatistics();
    
    /**
     * 获取活跃用户数
     */
    Long getActiveUserCount();
    
    /**
     * 获取注册用户总数
     */
    Long getTotalUserCount();
    
    /**
     * 根据角色获取用户数量
     */
    Long getUserCountByRole(String role);
    
    class UserStatistics {
        private Long totalUsers;
        private Long activeUsers;
        private Long adminUsers;
        private Long moderatorUsers;
        private Long regularUsers;
        
        public UserStatistics(Long totalUsers, Long activeUsers, Long adminUsers, Long moderatorUsers, Long regularUsers) {
            this.totalUsers = totalUsers;
            this.activeUsers = activeUsers;
            this.adminUsers = adminUsers;
            this.moderatorUsers = moderatorUsers;
            this.regularUsers = regularUsers;
        }
        
        // Getters and setters
        public Long getTotalUsers() { return totalUsers; }
        public void setTotalUsers(Long totalUsers) { this.totalUsers = totalUsers; }
        
        public Long getActiveUsers() { return activeUsers; }
        public void setActiveUsers(Long activeUsers) { this.activeUsers = activeUsers; }
        
        public Long getAdminUsers() { return adminUsers; }
        public void setAdminUsers(Long adminUsers) { this.adminUsers = adminUsers; }
        
        public Long getModeratorUsers() { return moderatorUsers; }
        public void setModeratorUsers(Long moderatorUsers) { this.moderatorUsers = moderatorUsers; }
        
        public Long getRegularUsers() { return regularUsers; }
        public void setRegularUsers(Long regularUsers) { this.regularUsers = regularUsers; }
    }
}
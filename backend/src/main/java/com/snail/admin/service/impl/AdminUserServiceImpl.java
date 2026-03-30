package com.snail.admin.service.impl;

import com.snail.admin.service.AdminUserService;
import com.snail.entity.User;
import com.snail.entity.UserRole;
import com.snail.dto.UserDto;
import com.snail.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminUserServiceImpl implements AdminUserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public Page<UserDto> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(this::convertToDto);
    }
    
    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    @Override
    public User updateUserRole(Long userId, String role) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            try {
                UserRole userRole = UserRole.valueOf(role.toUpperCase());
                user.setRole(userRole);
                return userRepository.save(user);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid role: " + role);
            }
        } else {
            throw new RuntimeException("User not found with id: " + userId);
        }
    }
    
    @Override
    public User toggleUserStatus(Long userId, Boolean active) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setIsActive(active);
            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found with id: " + userId);
        }
    }
    
    @Override
    public User deleteUser(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // 这里使用软删除，只是标记为非活跃状态
            user.setIsActive(false);
            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found with id: " + userId);
        }
    }
    
    @Override
    public Page<UserDto> searchUsers(String keyword, Pageable pageable) {
        Page<User> users = userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword, pageable);
        return users.map(this::convertToDto);
    }
    
    @Override
    public UserStatistics getUserStatistics() {
        Long totalUsers = userRepository.count();
        Long activeUsers = userRepository.countByIsActiveTrue();
        Long adminUsers = userRepository.countByRole(UserRole.ADMIN);
        Long moderatorUsers = userRepository.countByRole(UserRole.MODERATOR);
        Long regularUsers = userRepository.countByRole(UserRole.USER);
        
        return new UserStatistics(totalUsers, activeUsers, adminUsers, moderatorUsers, regularUsers);
    }
    
    @Override
    public Long getActiveUserCount() {
        return userRepository.countByIsActiveTrue();
    }
    
    @Override
    public Long getTotalUserCount() {
        return userRepository.count();
    }
    
    @Override
    public Long getUserCountByRole(String role) {
        try {
            UserRole userRole = UserRole.valueOf(role.toUpperCase());
            return userRepository.countByRole(userRole);
        } catch (IllegalArgumentException e) {
            return 0L;
        }
    }
    
    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setRole(user.getRole());
        dto.setIsActive(user.getIsActive());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
}
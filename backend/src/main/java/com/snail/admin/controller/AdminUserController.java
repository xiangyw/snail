package com.snail.admin.controller;

import com.snail.admin.annotation.RequireAdmin;
import com.snail.admin.service.AdminUserService;
import com.snail.dto.UserDto;
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
@RequestMapping("/api/admin/users")
@Tag(name = "Admin Users", description = "管理员用户管理接口")
public class AdminUserController {
    
    @Autowired
    private AdminUserService adminUserService;
    
    @GetMapping
    @Operation(summary = "获取所有用户", description = "分页获取所有用户信息")
    @RequireAdmin
    public ResponseEntity<Page<UserDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDto> users = adminUserService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取用户", description = "根据用户ID获取用户详细信息")
    @RequireAdmin
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<com.snail.entity.User> user = adminUserService.getUserById(id);
        if (user.isPresent()) {
            UserDto dto = convertToDto(user.get());
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/role")
    @Operation(summary = "更新用户角色", description = "更新指定用户的权限角色")
    @RequireAdmin
    public ResponseEntity<com.snail.entity.User> updateUserRole(
            @PathVariable Long id,
            @RequestParam String role) {
        com.snail.entity.User updatedUser = adminUserService.updateUserRole(id, role);
        return ResponseEntity.ok(updatedUser);
    }
    
    @PutMapping("/{id}/status")
    @Operation(summary = "切换用户状态", description = "禁用或启用用户账户")
    @RequireAdmin
    public ResponseEntity<com.snail.entity.User> toggleUserStatus(
            @PathVariable Long id,
            @RequestParam Boolean active) {
        com.snail.entity.User updatedUser = adminUserService.toggleUserStatus(id, active);
        return ResponseEntity.ok(updatedUser);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "删除指定用户（软删除）")
    @RequireAdmin
    public ResponseEntity<com.snail.entity.User> deleteUser(@PathVariable Long id) {
        com.snail.entity.User deletedUser = adminUserService.deleteUser(id);
        return ResponseEntity.ok(deletedUser);
    }
    
    @GetMapping("/search")
    @Operation(summary = "搜索用户", description = "根据关键词搜索用户")
    @RequireAdmin
    public ResponseEntity<Page<UserDto>> searchUsers(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDto> users = adminUserService.searchUsers(keyword, pageable);
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/stats")
    @Operation(summary = "获取用户统计", description = "获取用户统计信息")
    @RequireAdmin
    public ResponseEntity<AdminUserService.UserStatistics> getUserStatistics() {
        AdminUserService.UserStatistics stats = adminUserService.getUserStatistics();
        return ResponseEntity.ok(stats);
    }
    
    private UserDto convertToDto(com.snail.entity.User user) {
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
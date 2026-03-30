package com.snail.admin.controller;

import com.snail.admin.annotation.RequireAdmin;
import com.snail.admin.service.AdminTaskService;
import com.snail.dto.TaskDto;
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
@RequestMapping("/api/admin/tasks")
@Tag(name = "Admin Tasks", description = "管理员任务管理接口")
public class AdminTaskController {
    
    @Autowired
    private AdminTaskService adminTaskService;
    
    @GetMapping
    @Operation(summary = "获取所有任务", description = "分页获取所有任务信息")
    @RequireAdmin
    public ResponseEntity<Page<TaskDto>> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TaskDto> tasks = adminTaskService.getAllTasks(pageable);
        return ResponseEntity.ok(tasks);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取任务", description = "根据任务ID获取任务详细信息")
    @RequireAdmin
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
        Optional<com.snail.entity.Task> task = adminTaskService.getTaskById(id);
        if (task.isPresent()) {
            TaskDto dto = convertToDto(task.get());
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    @Operation(summary = "创建新任务", description = "创建新的任务")
    @RequireAdmin
    public ResponseEntity<com.snail.entity.Task> createTask(@RequestBody com.snail.entity.Task task) {
        com.snail.entity.Task createdTask = adminTaskService.createTask(task);
        return ResponseEntity.ok(createdTask);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新任务", description = "更新指定任务的信息")
    @RequireAdmin
    public ResponseEntity<com.snail.entity.Task> updateTask(
            @PathVariable Long id,
            @RequestBody com.snail.entity.Task task) {
        com.snail.entity.Task updatedTask = adminTaskService.updateTask(id, task);
        return ResponseEntity.ok(updatedTask);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "删除任务", description = "删除指定任务（逻辑删除）")
    @RequireAdmin
    public ResponseEntity<com.snail.entity.Task> deleteTask(@PathVariable Long id) {
        com.snail.entity.Task deletedTask = adminTaskService.deleteTask(id);
        return ResponseEntity.ok(deletedTask);
    }
    
    @PutMapping("/{id}/status")
    @Operation(summary = "切换任务状态", description = "激活或停用任务")
    @RequireAdmin
    public ResponseEntity<com.snail.entity.Task> toggleTaskStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        com.snail.entity.Task updatedTask = adminTaskService.toggleTaskStatus(id, status);
        return ResponseEntity.ok(updatedTask);
    }
    
    @GetMapping("/search")
    @Operation(summary = "搜索任务", description = "根据关键词搜索任务")
    @RequireAdmin
    public ResponseEntity<Page<TaskDto>> searchTasks(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TaskDto> tasks = adminTaskService.searchTasks(keyword, pageable);
        return ResponseEntity.ok(tasks);
    }
    
    @GetMapping("/type/{type}")
    @Operation(summary = "根据类型获取任务", description = "根据任务类型获取任务列表")
    @RequireAdmin
    public ResponseEntity<Page<TaskDto>> getTasksByType(
            @PathVariable String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TaskDto> tasks = adminTaskService.getTasksByType(type, pageable);
        return ResponseEntity.ok(tasks);
    }
    
    @GetMapping("/stats")
    @Operation(summary = "获取任务统计", description = "获取任务统计信息")
    @RequireAdmin
    public ResponseEntity<AdminTaskService.TaskStatistics> getTaskStatistics() {
        AdminTaskService.TaskStatistics stats = adminTaskService.getTaskStatistics();
        return ResponseEntity.ok(stats);
    }
    
    private TaskDto convertToDto(com.snail.entity.Task task) {
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setType(task.getType());
        dto.setStatus(task.getStatus());
        dto.setPoints(task.getPoints());
        dto.setMaxCompletionCount(task.getMaxCompletionCount());
        dto.setCurrentCompletionCount(task.getCurrentCompletionCount());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());
        dto.setExpiresAt(task.getExpiresAt());
        return dto;
    }
}
package com.snail.admin.controller;

import com.snail.admin.annotation.RequireAdmin;
import com.snail.admin.dto.AdminOperationLogDto;
import com.snail.admin.entity.AdminOperationLog;
import com.snail.admin.service.AdminOperationLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin/logs")
@Tag(name = "Admin Logs", description = "管理员操作日志接口")
public class AdminLogController {
    
    @Autowired
    private AdminOperationLogService adminOperationLogService;
    
    @GetMapping
    @Operation(summary = "获取所有操作日志", description = "分页获取所有管理员操作日志")
    @RequireAdmin
    public ResponseEntity<Page<AdminOperationLogDto>> getAllLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdminOperationLog> logs = adminOperationLogService.getAllLogs(pageable);
        Page<AdminOperationLogDto> dtos = logs.map(this::convertToDto);
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/admin/{adminUserId}")
    @Operation(summary = "根据管理员ID获取操作日志", description = "根据管理员用户ID获取其操作日志")
    @RequireAdmin
    public ResponseEntity<Page<AdminOperationLogDto>> getLogsByAdminUserId(
            @PathVariable Long adminUserId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdminOperationLog> logs = adminOperationLogService.getLogsByAdminUserId(adminUserId, pageable);
        Page<AdminOperationLogDto> dtos = logs.map(this::convertToDto);
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/operation-type/{operationType}")
    @Operation(summary = "根据操作类型获取日志", description = "根据操作类型获取操作日志")
    @RequireAdmin
    public ResponseEntity<Page<AdminOperationLogDto>> getLogsByOperationType(
            @PathVariable String operationType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdminOperationLog> logs = adminOperationLogService.getLogsByOperationType(operationType, pageable);
        Page<AdminOperationLogDto> dtos = logs.map(this::convertToDto);
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/resource-type/{resourceType}")
    @Operation(summary = "根据资源类型获取日志", description = "根据资源类型获取操作日志")
    @RequireAdmin
    public ResponseEntity<Page<AdminOperationLogDto>> getLogsByResourceType(
            @PathVariable String resourceType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdminOperationLog> logs = adminOperationLogService.getLogsByResourceType(resourceType, pageable);
        Page<AdminOperationLogDto> dtos = logs.map(this::convertToDto);
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/time-range")
    @Operation(summary = "根据时间范围获取日志", description = "根据时间范围获取操作日志")
    @RequireAdmin
    public ResponseEntity<Page<AdminOperationLogDto>> getLogsByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdminOperationLog> logs = adminOperationLogService.getLogsByTimeRange(start, end, pageable);
        Page<AdminOperationLogDto> dtos = logs.map(this::convertToDto);
        return ResponseEntity.ok(dtos);
    }
    
    @DeleteMapping("/cleanup")
    @Operation(summary = "清理过期日志", description = "清理指定天数之前的过期操作日志")
    @RequireAdmin
    public ResponseEntity<String> cleanupLogs(@RequestParam(defaultValue = "30") int daysOld) {
        adminOperationLogService.cleanupLogs(daysOld);
        return ResponseEntity.ok("成功清理 " + daysOld + " 天前的日志");
    }
    
    private AdminOperationLogDto convertToDto(AdminOperationLog log) {
        AdminOperationLogDto dto = new AdminOperationLogDto();
        dto.setId(log.getId());
        dto.setAdminUserId(log.getAdminUserId());
        dto.setAdminUsername(log.getAdminUsername());
        dto.setOperationType(log.getOperationType());
        dto.setResourceType(log.getResourceType());
        dto.setResourceId(log.getResourceId());
        dto.setOperationDetails(log.getOperationDetails());
        dto.setIpAddress(log.getIpAddress());
        dto.setUserAgent(log.getUserAgent());
        dto.setCreatedAt(log.getCreatedAt());
        return dto;
    }
}
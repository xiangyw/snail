package com.snail.admin.service;

import com.snail.admin.entity.AdminOperationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface AdminOperationLogService {
    
    /**
     * 记录操作日志
     */
    AdminOperationLog logOperation(Long adminUserId, String adminUsername, 
                                  String operationType, String resourceType, 
                                  Long resourceId, String operationDetails, 
                                  String ipAddress, String userAgent);
    
    /**
     * 根据管理员ID获取操作日志
     */
    Page<AdminOperationLog> getLogsByAdminUserId(Long adminUserId, Pageable pageable);
    
    /**
     * 根据操作类型获取操作日志
     */
    Page<AdminOperationLog> getLogsByOperationType(String operationType, Pageable pageable);
    
    /**
     * 根据资源类型获取操作日志
     */
    Page<AdminOperationLog> getLogsByResourceType(String resourceType, Pageable pageable);
    
    /**
     * 根据时间段获取操作日志
     */
    Page<AdminOperationLog> getLogsByTimeRange(LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    /**
     * 获取所有操作日志
     */
    Page<AdminOperationLog> getAllLogs(Pageable pageable);
    
    /**
     * 清理过期的操作日志
     */
    void cleanupLogs(int daysOld);
}
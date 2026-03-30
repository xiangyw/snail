package com.snail.admin.service.impl;

import com.snail.admin.entity.AdminOperationLog;
import com.snail.admin.repository.AdminOperationLogRepository;
import com.snail.admin.service.AdminOperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminOperationLogServiceImpl implements AdminOperationLogService {
    
    @Autowired
    private AdminOperationLogRepository logRepository;
    
    @Override
    public AdminOperationLog logOperation(Long adminUserId, String adminUsername,
                                         String operationType, String resourceType,
                                         Long resourceId, String operationDetails,
                                         String ipAddress, String userAgent) {
        AdminOperationLog logEntry = new AdminOperationLog();
        logEntry.setAdminUserId(adminUserId);
        logEntry.setAdminUsername(adminUsername);
        logEntry.setOperationType(operationType);
        logEntry.setResourceType(resourceType);
        logEntry.setResourceId(resourceId);
        logEntry.setOperationDetails(operationDetails);
        logEntry.setIpAddress(ipAddress);
        logEntry.setUserAgent(userAgent);
        logEntry.setCreatedAt(LocalDateTime.now());
        
        return logRepository.save(logEntry);
    }
    
    @Override
    public Page<AdminOperationLog> getLogsByAdminUserId(Long adminUserId, Pageable pageable) {
        return logRepository.findByAdminUserId(adminUserId, pageable);
    }
    
    @Override
    public Page<AdminOperationLog> getLogsByOperationType(String operationType, Pageable pageable) {
        return logRepository.findByOperationType(operationType, pageable);
    }
    
    @Override
    public Page<AdminOperationLog> getLogsByResourceType(String resourceType, Pageable pageable) {
        return logRepository.findByResourceType(resourceType, pageable);
    }
    
    @Override
    public Page<AdminOperationLog> getLogsByTimeRange(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return logRepository.findByCreatedAtBetween(start, end, pageable);
    }
    
    @Override
    public Page<AdminOperationLog> getAllLogs(Pageable pageable) {
        return logRepository.findAll(pageable);
    }
    
    @Override
    public void cleanupLogs(int daysOld) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysOld);
        LocalDateTime startDate = LocalDateTime.of(1970, 1, 1, 0, 0, 0); // Start from epoch
        List<AdminOperationLog> oldLogs = logRepository.findByCreatedAtBetween(startDate, cutoffDate, Pageable.unpaged()).getContent();
        logRepository.deleteAll(oldLogs);
    }
}
package com.snail.admin.repository;

import com.snail.admin.entity.AdminOperationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AdminOperationLogRepository extends JpaRepository<AdminOperationLog, Long> {
    
    Page<AdminOperationLog> findByAdminUserId(Long adminUserId, Pageable pageable);
    
    Page<AdminOperationLog> findByOperationType(String operationType, Pageable pageable);
    
    Page<AdminOperationLog> findByResourceType(String resourceType, Pageable pageable);
    
    Page<AdminOperationLog> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    List<AdminOperationLog> findByAdminUserIdAndCreatedAtBetween(Long adminUserId, LocalDateTime start, LocalDateTime end);
}
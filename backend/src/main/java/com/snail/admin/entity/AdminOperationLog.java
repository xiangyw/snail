package com.snail.admin.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "admin_operation_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminOperationLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "admin_user_id", nullable = false)
    private Long adminUserId;
    
    @Column(name = "admin_username", nullable = false)
    private String adminUsername;
    
    @Column(name = "operation_type", nullable = false)
    private String operationType; // CREATE, UPDATE, DELETE, READ
    
    @Column(name = "resource_type", nullable = false)
    private String resourceType; // USER, CONTENT, TASK, ORDER
    
    @Column(name = "resource_id")
    private Long resourceId;
    
    @Column(name = "operation_details", columnDefinition = "TEXT")
    private String operationDetails;
    
    @Column(name = "ip_address")
    private String ipAddress;
    
    @Column(name = "user_agent")
    private String userAgent;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    public enum OperationType {
        CREATE, UPDATE, DELETE, READ
    }
    
    public enum ResourceType {
        USER, CONTENT, TASK, ORDER
    }
}
package com.snail.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    private String description;
    
    @Enumerated(EnumType.STRING)
    private TaskType type;
    
    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.ACTIVE;
    
    private Integer points = 0;
    
    @Column(name = "max_completion_count")
    private Integer maxCompletionCount = 100; // Max number of times this task can be completed
    
    @Column(name = "current_completion_count")
    private Integer currentCompletionCount = 0;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @Column(name = "expires_at")
    private LocalDateTime expiresAt; // When the task expires
    
    public enum TaskType {
        DAILY, WEEKLY, ONE_TIME, CONTENT_CREATION
    }
    
    public enum TaskStatus {
        ACTIVE, INACTIVE, EXPIRED
    }
    
    // Check if task can still be completed
    public boolean canBeCompleted() {
        return this.status == TaskStatus.ACTIVE && 
               (this.maxCompletionCount == 0 || this.currentCompletionCount < this.maxCompletionCount) &&
               (this.expiresAt == null || this.expiresAt.isAfter(LocalDateTime.now()));
    }
}
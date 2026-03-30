package com.snail.dto;

import com.snail.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private Task.TaskType type;
    private Task.TaskStatus status;
    private Integer points;
    private Integer maxCompletionCount;
    private Integer currentCompletionCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime expiresAt;
    
    public static TaskDto fromEntity(Task task) {
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
    
    public static Task toEntity(TaskDto dto) {
        Task task = new Task();
        task.setId(dto.getId());
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setType(dto.getType());
        task.setStatus(dto.getStatus());
        task.setPoints(dto.getPoints());
        task.setMaxCompletionCount(dto.getMaxCompletionCount());
        task.setCurrentCompletionCount(dto.getCurrentCompletionCount());
        task.setExpiresAt(dto.getExpiresAt());
        return task;
    }
}
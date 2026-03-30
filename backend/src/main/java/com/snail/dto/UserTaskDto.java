package com.snail.dto;

import com.snail.entity.UserTask;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTaskDto {
    private Long id;
    private Long userId;
    private Long taskId;
    private UserTask.UserTaskStatus status;
    private LocalDateTime completedAt;
    private LocalDateTime claimedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static UserTaskDto fromEntity(UserTask userTask) {
        UserTaskDto dto = new UserTaskDto();
        dto.setId(userTask.getId());
        dto.setUserId(userTask.getUser().getId());
        dto.setTaskId(userTask.getTask().getId());
        dto.setStatus(userTask.getStatus());
        dto.setCompletedAt(userTask.getCompletedAt());
        dto.setClaimedAt(userTask.getClaimedAt());
        dto.setCreatedAt(userTask.getCreatedAt());
        dto.setUpdatedAt(userTask.getUpdatedAt());
        return dto;
    }
}
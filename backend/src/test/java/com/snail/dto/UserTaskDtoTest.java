package com.snail.dto;

import com.snail.entity.UserTask;
import com.snail.entity.User;
import com.snail.entity.Task;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTaskDtoTest {

    @Test
    void testUserTaskDtoCreation() {
        UserTaskDto dto = new UserTaskDto();
        dto.setId(1L);
        dto.setUserId(1L);
        dto.setTaskId(1L);
        dto.setStatus(UserTask.UserTaskStatus.PENDING);
        dto.setCompletedAt(LocalDateTime.now());
        dto.setClaimedAt(LocalDateTime.now());
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());

        assertEquals(1L, dto.getId());
        assertEquals(Long.valueOf(1L), dto.getUserId());
        assertEquals(Long.valueOf(1L), dto.getTaskId());
        assertEquals(UserTask.UserTaskStatus.PENDING, dto.getStatus());
        assertNotNull(dto.getCompletedAt());
        assertNotNull(dto.getClaimedAt());
        assertNotNull(dto.getCreatedAt());
        assertNotNull(dto.getUpdatedAt());
    }

    @Test
    void testUserTaskDtoFromEntity() {
        UserTask userTask = new UserTask();
        User user = new User();
        user.setId(1L);
        Task task = new Task();
        task.setId(1L);
        
        userTask.setId(1L);
        userTask.setUser(user);
        userTask.setTask(task);
        userTask.setStatus(UserTask.UserTaskStatus.COMPLETED);
        userTask.setCompletedAt(LocalDateTime.now().minusHours(1));
        userTask.setClaimedAt(null); // Not claimed yet

        UserTaskDto dto = UserTaskDto.fromEntity(userTask);

        assertEquals(1L, dto.getId());
        assertEquals(Long.valueOf(1L), dto.getUserId());
        assertEquals(Long.valueOf(1L), dto.getTaskId());
        assertEquals(UserTask.UserTaskStatus.COMPLETED, dto.getStatus());
        assertNotNull(dto.getCompletedAt());
        assertNull(dto.getClaimedAt());
        assertNotNull(dto.getCreatedAt());
    }
}
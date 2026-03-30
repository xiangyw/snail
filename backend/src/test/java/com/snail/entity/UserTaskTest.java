package com.snail.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTaskTest {

    @Test
    void testUserTaskCreation() {
        UserTask userTask = new UserTask();
        User user = new User();
        user.setId(1L);
        Task task = new Task();
        task.setId(1L);
        
        userTask.setId(1L);
        userTask.setUser(user);
        userTask.setTask(task);
        userTask.setStatus(UserTask.UserTaskStatus.PENDING);
        userTask.setCompletedAt(LocalDateTime.now());
        userTask.setClaimedAt(LocalDateTime.now());

        assertEquals(1L, userTask.getId());
        assertEquals(user, userTask.getUser());
        assertEquals(task, userTask.getTask());
        assertEquals(UserTask.UserTaskStatus.PENDING, userTask.getStatus());
        assertNotNull(userTask.getCompletedAt());
        assertNotNull(userTask.getClaimedAt());
    }

    @Test
    void testUserTaskDefaults() {
        UserTask userTask = new UserTask();
        
        assertNull(userTask.getId());
        assertNull(userTask.getUser());
        assertNull(userTask.getTask());
        assertEquals(UserTask.UserTaskStatus.PENDING, userTask.getStatus()); // Default status
        assertNull(userTask.getCompletedAt());
        assertNull(userTask.getClaimedAt());
        assertNotNull(userTask.getCreatedAt()); // Should have a creation time
        assertNotNull(userTask.getUpdatedAt()); // Should have an update time
    }

    @Test
    void testDifferentUserTaskStatuses() {
        UserTask userTask = new UserTask();
        
        // Test various user task statuses
        userTask.setStatus(UserTask.UserTaskStatus.PENDING);
        assertEquals(UserTask.UserTaskStatus.PENDING, userTask.getStatus());
        
        userTask.setStatus(UserTask.UserTaskStatus.COMPLETED);
        assertEquals(UserTask.UserTaskStatus.COMPLETED, userTask.getStatus());
        
        userTask.setStatus(UserTask.UserTaskStatus.CLAIMED);
        assertEquals(UserTask.UserTaskStatus.CLAIMED, userTask.getStatus());
        
        userTask.setStatus(UserTask.UserTaskStatus.FAILED);
        assertEquals(UserTask.UserTaskStatus.FAILED, userTask.getStatus());
    }
}
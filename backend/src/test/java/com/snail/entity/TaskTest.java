package com.snail.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void testTaskCreation() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("This is a test task");
        task.setType(Task.TaskType.DAILY);
        task.setStatus(Task.TaskStatus.ACTIVE);
        task.setPoints(10);
        task.setMaxCompletionCount(5);

        assertEquals(1L, task.getId());
        assertEquals("Test Task", task.getTitle());
        assertEquals("This is a test task", task.getDescription());
        assertEquals(Task.TaskType.DAILY, task.getType());
        assertEquals(Task.TaskStatus.ACTIVE, task.getStatus());
        assertEquals(Integer.valueOf(10), task.getPoints());
        assertEquals(Integer.valueOf(5), task.getMaxCompletionCount());
    }

    @Test
    void testCanCompleteTaskActive() {
        Task task = new Task();
        task.setStatus(Task.TaskStatus.ACTIVE);
        task.setMaxCompletionCount(2);
        task.setCurrentCompletionCount(1);

        assertTrue(task.canBeCompleted());
    }

    @Test
    void testCannotCompleteTaskMaxReached() {
        Task task = new Task();
        task.setStatus(Task.TaskStatus.ACTIVE);
        task.setMaxCompletionCount(2);
        task.setCurrentCompletionCount(2); // Already reached max

        assertFalse(task.canBeCompleted());
    }

    @Test
    void testCannotCompleteTaskInactive() {
        Task task = new Task();
        task.setStatus(Task.TaskStatus.INACTIVE);
        task.setMaxCompletionCount(2);
        task.setCurrentCompletionCount(1);

        assertFalse(task.canBeCompleted());
    }

    @Test
    void testCannotCompleteTaskExpired() {
        Task task = new Task();
        task.setStatus(Task.TaskStatus.ACTIVE);
        task.setMaxCompletionCount(2);
        task.setCurrentCompletionCount(1);
        task.setExpiresAt(LocalDateTime.now().minusDays(1)); // Expired

        assertFalse(task.canBeCompleted());
    }

    @Test
    void testCanCompleteUnlimitedTask() {
        Task task = new Task();
        task.setStatus(Task.TaskStatus.ACTIVE);
        task.setMaxCompletionCount(0); // Unlimited
        task.setCurrentCompletionCount(100); // Even with high count

        assertTrue(task.canBeCompleted());
    }

    @Test
    void testTaskDefaults() {
        Task task = new Task();
        
        assertNull(task.getId());
        assertNull(task.getTitle());
        assertNull(task.getDescription());
        assertEquals(Task.TaskType.class, task.getType().getClass().getSuperclass() != null ? 
                     task.getType().getClass() : Task.TaskType.class); // Need to set type to get default
        assertEquals(Task.TaskStatus.ACTIVE, task.getStatus()); // Default status
        assertEquals(Integer.valueOf(0), task.getPoints()); // Default points
        assertEquals(Integer.valueOf(100), task.getMaxCompletionCount()); // Default max
        assertEquals(Integer.valueOf(0), task.getCurrentCompletionCount()); // Default current
        assertNotNull(task.getCreatedAt()); // Should have a creation time
    }
}
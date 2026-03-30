package com.snail.dto;

import com.snail.entity.Task;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskDtoTest {

    @Test
    void testTaskDtoCreation() {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(1L);
        taskDto.setTitle("Test Task");
        taskDto.setDescription("This is a test task");
        taskDto.setType(Task.TaskType.DAILY);
        taskDto.setStatus(Task.TaskStatus.ACTIVE);
        taskDto.setPoints(10);
        taskDto.setMaxCompletionCount(5);
        taskDto.setCurrentCompletionCount(2);
        taskDto.setCreatedAt(LocalDateTime.now());
        taskDto.setUpdatedAt(LocalDateTime.now());
        taskDto.setExpiresAt(LocalDateTime.now().plusDays(7));

        assertEquals(1L, taskDto.getId());
        assertEquals("Test Task", taskDto.getTitle());
        assertEquals("This is a test task", taskDto.getDescription());
        assertEquals(Task.TaskType.DAILY, taskDto.getType());
        assertEquals(Task.TaskStatus.ACTIVE, taskDto.getStatus());
        assertEquals(Integer.valueOf(10), taskDto.getPoints());
        assertEquals(Integer.valueOf(5), taskDto.getMaxCompletionCount());
        assertEquals(Integer.valueOf(2), taskDto.getCurrentCompletionCount());
        assertNotNull(taskDto.getCreatedAt());
        assertNotNull(taskDto.getUpdatedAt());
        assertNotNull(taskDto.getExpiresAt());
    }

    @Test
    void testTaskDtoFromEntity() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task Entity");
        task.setDescription("This is a test task entity");
        task.setType(Task.TaskType.WEEKLY);
        task.setStatus(Task.TaskStatus.INACTIVE);
        task.setPoints(25);
        task.setMaxCompletionCount(10);
        task.setCurrentCompletionCount(3);

        TaskDto taskDto = TaskDto.fromEntity(task);

        assertEquals(1L, taskDto.getId());
        assertEquals("Test Task Entity", taskDto.getTitle());
        assertEquals("This is a test task entity", taskDto.getDescription());
        assertEquals(Task.TaskType.WEEKLY, taskDto.getType());
        assertEquals(Task.TaskStatus.INACTIVE, taskDto.getStatus());
        assertEquals(Integer.valueOf(25), taskDto.getPoints());
        assertEquals(Integer.valueOf(10), taskDto.getMaxCompletionCount());
        assertEquals(Integer.valueOf(3), taskDto.getCurrentCompletionCount());
    }

    @Test
    void testTaskDtoToEntity() {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(2L);
        taskDto.setTitle("Converted Task");
        taskDto.setDescription("This task was converted from DTO");
        taskDto.setType(Task.TaskType.ONE_TIME);
        taskDto.setStatus(Task.TaskStatus.EXPIRED);
        taskDto.setPoints(50);
        taskDto.setMaxCompletionCount(1);
        taskDto.setCurrentCompletionCount(1);

        Task task = TaskDto.toEntity(taskDto);

        assertEquals(2L, task.getId());
        assertEquals("Converted Task", task.getTitle());
        assertEquals("This task was converted from DTO", task.getDescription());
        assertEquals(Task.TaskType.ONE_TIME, task.getType());
        assertEquals(Task.TaskStatus.EXPIRED, task.getStatus());
        assertEquals(Integer.valueOf(50), task.getPoints());
        assertEquals(Integer.valueOf(1), task.getMaxCompletionCount());
        assertEquals(Integer.valueOf(1), task.getCurrentCompletionCount());
    }
}
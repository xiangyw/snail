package com.snail.service;

import com.snail.dto.TaskDto;
import com.snail.entity.Task;
import com.snail.entity.User;
import com.snail.entity.UserTask;
import com.snail.repository.TaskRepository;
import com.snail.repository.UserTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserTaskRepository userTaskRepository;

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        taskService = new TaskService();
        // Use reflection to inject dependencies
        org.springframework.test.util.ReflectionTestUtils.setField(taskService, "taskRepository", taskRepository);
        org.springframework.test.util.ReflectionTestUtils.setField(taskService, "userTaskRepository", userTaskRepository);
    }

    @Test
    void testGetAllTasks() {
        // Arrange
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");

        List<Task> taskList = Arrays.asList(task1, task2);
        when(taskRepository.findAll()).thenReturn(taskList);

        // Act
        List<TaskDto> result = taskService.getAllTasks();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Task 1", result.get(0).getTitle());
        assertEquals("Task 2", result.get(1).getTitle());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testGetTaskById() {
        // Arrange
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Act
        TaskDto result = taskService.getTaskById(1L);

        // Assert
        assertEquals(1L, result.getId());
        assertEquals("Test Task", result.getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTaskByIdNotFound() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            taskService.getTaskById(1L);
        });
        
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateTask() {
        // Arrange
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("New Task");
        taskDto.setDescription("New Description");
        taskDto.setPoints(10);

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle("New Task");
        savedTask.setDescription("New Description");
        savedTask.setPoints(10);

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        // Act
        TaskDto result = taskService.createTask(taskDto);

        // Assert
        assertEquals("New Task", result.getTitle());
        assertEquals("New Description", result.getDescription());
        assertEquals(Integer.valueOf(10), result.getPoints());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testAssignTaskToUser() {
        // Arrange
        User user = new User();
        user.setId(1L);
        
        Task task = new Task();
        task.setId(1L);
        task.setStatus(Task.TaskStatus.ACTIVE);
        task.setMaxCompletionCount(5);
        task.setCurrentCompletionCount(2);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userTaskRepository.findByUserAndTask(user, task)).thenReturn(Optional.empty());

        // Act
        boolean result = taskService.assignTaskToUser(user, 1L);

        // Assert
        assertTrue(result);
        verify(taskRepository, times(1)).findById(1L);
        verify(userTaskRepository, times(1)).findByUserAndTask(user, task);
    }

    @Test
    void testAssignTaskToUserAlreadyAssigned() {
        // Arrange
        User user = new User();
        user.setId(1L);
        
        Task task = new Task();
        task.setId(1L);
        task.setStatus(Task.TaskStatus.ACTIVE);
        task.setMaxCompletionCount(5);
        task.setCurrentCompletionCount(2);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userTaskRepository.findByUserAndTask(user, task)).thenReturn(Optional.of(new UserTask()));

        // Act
        boolean result = taskService.assignTaskToUser(user, 1L);

        // Assert
        assertFalse(result); // Should return false if already assigned
        verify(taskRepository, times(1)).findById(1L);
        verify(userTaskRepository, times(1)).findByUserAndTask(user, task);
    }
}
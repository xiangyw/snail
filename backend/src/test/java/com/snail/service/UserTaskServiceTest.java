package com.snail.service;

import com.snail.dto.UserTaskDto;
import com.snail.entity.User;
import com.snail.entity.UserTask;
import com.snail.entity.Task;
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

class UserTaskServiceTest {

    @Mock
    private UserTaskRepository userTaskRepository;

    private UserTaskService userTaskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userTaskService = new UserTaskService();
        // Use reflection to inject dependencies
        org.springframework.test.util.ReflectionTestUtils.setField(userTaskService, "userTaskRepository", userTaskRepository);
    }

    @Test
    void testGetUserTasksByUser() {
        // Arrange
        User user = new User();
        user.setId(1L);

        UserTask userTask1 = new UserTask();
        userTask1.setId(1L);
        userTask1.setUser(user);
        userTask1.setStatus(UserTask.UserTaskStatus.PENDING);

        UserTask userTask2 = new UserTask();
        userTask2.setId(2L);
        userTask2.setUser(user);
        userTask2.setStatus(UserTask.UserTaskStatus.COMPLETED);

        List<UserTask> userTaskList = Arrays.asList(userTask1, userTask2);
        when(userTaskRepository.findByUser(user)).thenReturn(userTaskList);

        // Act
        List<UserTaskDto> result = userTaskService.getUserTasksByUser(user);

        // Assert
        assertEquals(2, result.size());
        verify(userTaskRepository, times(1)).findByUser(user);
    }

    @Test
    void testGetUserTaskById() {
        // Arrange
        UserTask userTask = new UserTask();
        userTask.setId(1L);
        userTask.setStatus(UserTask.UserTaskStatus.PENDING);
        
        when(userTaskRepository.findById(1L)).thenReturn(Optional.of(userTask));

        // Act
        UserTaskDto result = userTaskService.getUserTaskById(1L);

        // Assert
        assertEquals(1L, result.getId());
        assertEquals(UserTask.UserTaskStatus.PENDING, result.getStatus());
        verify(userTaskRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserTaskByIdNotFound() {
        // Arrange
        when(userTaskRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            userTaskService.getUserTaskById(1L);
        });
        
        verify(userTaskRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateUserTaskStatus() {
        // Arrange
        UserTask userTask = new UserTask();
        userTask.setId(1L);
        userTask.setStatus(UserTask.UserTaskStatus.PENDING);

        when(userTaskRepository.findById(1L)).thenReturn(Optional.of(userTask));
        when(userTaskRepository.save(any(UserTask.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UserTaskDto result = userTaskService.updateUserTaskStatus(1L, UserTask.UserTaskStatus.CLAIMED);

        // Assert
        assertEquals(UserTask.UserTaskStatus.CLAIMED, result.getStatus());
        verify(userTaskRepository, times(1)).findById(1L);
        verify(userTaskRepository, times(1)).save(any(UserTask.class));
    }
}
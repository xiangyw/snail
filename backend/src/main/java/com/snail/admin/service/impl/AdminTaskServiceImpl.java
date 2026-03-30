package com.snail.admin.service.impl;

import com.snail.admin.service.AdminTaskService;
import com.snail.entity.Task;
import com.snail.entity.Task.TaskStatus;
import com.snail.entity.Task.TaskType;
import com.snail.dto.TaskDto;
import com.snail.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminTaskServiceImpl implements AdminTaskService {
    
    @Autowired
    private TaskRepository taskRepository;
    
    @Override
    public Page<TaskDto> getAllTasks(Pageable pageable) {
        Page<Task> tasks = taskRepository.findAll(pageable);
        return tasks.map(this::convertToDto);
    }
    
    @Override
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }
    
    @Override
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }
    
    @Override
    public Task updateTask(Long taskId, Task task) {
        Optional<Task> taskOpt = taskRepository.findById(taskId);
        if (taskOpt.isPresent()) {
            Task existingTask = taskOpt.get();
            existingTask.setTitle(task.getTitle());
            existingTask.setDescription(task.getDescription());
            existingTask.setType(task.getType());
            existingTask.setStatus(task.getStatus());
            existingTask.setPoints(task.getPoints());
            existingTask.setMaxCompletionCount(task.getMaxCompletionCount());
            existingTask.setExpiresAt(task.getExpiresAt());
            return taskRepository.save(existingTask);
        } else {
            throw new RuntimeException("Task not found with id: " + taskId);
        }
    }
    
    @Override
    public Task deleteTask(Long taskId) {
        Optional<Task> taskOpt = taskRepository.findById(taskId);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            // 逻辑删除，将状态设置为INACTIVE
            task.setStatus(TaskStatus.INACTIVE);
            return taskRepository.save(task);
        } else {
            throw new RuntimeException("Task not found with id: " + taskId);
        }
    }
    
    @Override
    public Task toggleTaskStatus(Long taskId, String status) {
        Optional<Task> taskOpt = taskRepository.findById(taskId);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            try {
                TaskStatus taskStatus = TaskStatus.valueOf(status.toUpperCase());
                task.setStatus(taskStatus);
                return taskRepository.save(task);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status: " + status);
            }
        } else {
            throw new RuntimeException("Task not found with id: " + taskId);
        }
    }
    
    @Override
    public Page<TaskDto> searchTasks(String keyword, Pageable pageable) {
        Page<Task> tasks = taskRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword, pageable);
        return tasks.map(this::convertToDto);
    }
    
    @Override
    public Page<TaskDto> getTasksByType(String taskType, Pageable pageable) {
        try {
            TaskType type = TaskType.valueOf(taskType.toUpperCase());
            Page<Task> tasks = taskRepository.findByType(type, pageable);
            return tasks.map(this::convertToDto);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid task type: " + taskType);
        }
    }
    
    @Override
    public TaskStatistics getTaskStatistics() {
        Long totalTasks = taskRepository.count();
        Long activeTasks = taskRepository.countActiveTasks();
        Long inactiveTasks = taskRepository.countByStatus(TaskStatus.INACTIVE);
        Long expiredTasks = taskRepository.countByStatus(TaskStatus.EXPIRED);
        Long dailyTasks = taskRepository.countByType(TaskType.DAILY);
        Long weeklyTasks = taskRepository.countByType(TaskType.WEEKLY);
        Long oneTimeTasks = taskRepository.countByType(TaskType.ONE_TIME);
        Long contentCreationTasks = taskRepository.countByType(TaskType.CONTENT_CREATION);
        
        return new TaskStatistics(totalTasks, activeTasks, inactiveTasks, expiredTasks,
                                 dailyTasks, weeklyTasks, oneTimeTasks, contentCreationTasks);
    }
    
    @Override
    public Long getActiveTaskCount() {
        return taskRepository.countActiveTasks();
    }
    
    @Override
    public Long getTotalTaskCount() {
        return taskRepository.count();
    }
    
    @Override
    public Long getCompletedTaskCount() {
        // For now, returning all tasks - this could be enhanced with actual completion tracking
        return taskRepository.count();
    }
    
    private TaskDto convertToDto(Task task) {
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
}
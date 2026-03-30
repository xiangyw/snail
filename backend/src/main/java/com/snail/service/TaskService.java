package com.snail.service;

import com.snail.dto.TaskDto;
import com.snail.entity.Task;
import com.snail.entity.User;
import com.snail.entity.UserTask;
import com.snail.repository.TaskRepository;
import com.snail.repository.UserTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserTaskRepository userTaskRepository;

    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(TaskDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getActiveTasks() {
        return taskRepository.findByStatus(Task.TaskStatus.ACTIVE).stream()
                .map(TaskDto::fromEntity)
                .collect(Collectors.toList());
    }

    public TaskDto getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        return TaskDto.fromEntity(task);
    }

    public TaskDto createTask(TaskDto taskDto) {
        Task task = TaskDto.toEntity(taskDto);
        Task savedTask = taskRepository.save(task);
        return TaskDto.fromEntity(savedTask);
    }

    public TaskDto updateTask(Long id, TaskDto taskDto) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        existingTask.setTitle(taskDto.getTitle());
        existingTask.setDescription(taskDto.getDescription());
        existingTask.setType(taskDto.getType());
        existingTask.setStatus(taskDto.getStatus());
        existingTask.setPoints(taskDto.getPoints());
        existingTask.setMaxCompletionCount(taskDto.getMaxCompletionCount());
        existingTask.setExpiresAt(taskDto.getExpiresAt());

        Task updatedTask = taskRepository.save(existingTask);
        return TaskDto.fromEntity(updatedTask);
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }

    public boolean assignTaskToUser(User user, Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));

        // Check if task can be completed
        if (!task.canBeCompleted()) {
            return false;
        }

        // Check if user already has this task assigned
        if (userTaskRepository.findByUserAndTask(user, task).isPresent()) {
            return false; // User already has this task
        }

        UserTask userTask = new UserTask();
        userTask.setUser(user);
        userTask.setTask(task);
        userTask.setStatus(UserTask.UserTaskStatus.PENDING);

        userTaskRepository.save(userTask);
        return true;
    }

    public boolean completeTask(User user, Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));

        // Check if task can be completed
        if (!task.canBeCompleted()) {
            return false;
        }

        UserTask userTask = userTaskRepository.findByUserAndTask(user, task)
                .orElseThrow(() -> new RuntimeException("User task not found"));

        if (userTask.getStatus() != UserTask.UserTaskStatus.PENDING) {
            return false; // Task already completed or in a different state
        }

        userTask.setStatus(UserTask.UserTaskStatus.COMPLETED);
        userTask.setCompletedAt(LocalDateTime.now());
        userTaskRepository.save(userTask);

        // Update task completion count
        task.setCurrentCompletionCount(task.getCurrentCompletionCount() + 1);
        taskRepository.save(task);

        return true;
    }
}
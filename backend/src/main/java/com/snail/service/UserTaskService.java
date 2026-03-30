package com.snail.service;

import com.snail.dto.UserTaskDto;
import com.snail.entity.User;
import com.snail.entity.UserTask;
import com.snail.repository.UserTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserTaskService {

    @Autowired
    private UserTaskRepository userTaskRepository;

    public List<UserTaskDto> getUserTasksByUser(User user) {
        return userTaskRepository.findByUser(user).stream()
                .map(UserTaskDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<UserTaskDto> getUserTasksByUserAndStatus(User user, UserTask.UserTaskStatus status) {
        return userTaskRepository.findByUserAndStatus(user, status).stream()
                .map(UserTaskDto::fromEntity)
                .collect(Collectors.toList());
    }

    public UserTaskDto getUserTaskById(Long id) {
        UserTask userTask = userTaskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserTask not found with id: " + id));
        return UserTaskDto.fromEntity(userTask);
    }

    public UserTaskDto updateUserTaskStatus(Long id, UserTask.UserTaskStatus newStatus) {
        UserTask userTask = userTaskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserTask not found with id: " + id));

        userTask.setStatus(newStatus);
        userTask.setClaimedAt(newStatus == UserTask.UserTaskStatus.CLAIMED ? java.time.LocalDateTime.now() : null);

        UserTask updatedUserTask = userTaskRepository.save(userTask);
        return UserTaskDto.fromEntity(updatedUserTask);
    }
}
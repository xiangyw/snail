package com.snail.repository;

import com.snail.entity.UserTask;
import com.snail.entity.User;
import com.snail.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTaskRepository extends JpaRepository<UserTask, Long> {
    List<UserTask> findByUser(User user);
    List<UserTask> findByTask(Task task);
    List<UserTask> findByUserAndStatus(User user, UserTask.UserTaskStatus status);
    Optional<UserTask> findByUserAndTask(User user, Task task);
}
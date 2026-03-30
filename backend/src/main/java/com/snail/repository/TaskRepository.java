package com.snail.repository;

import com.snail.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(Task.TaskStatus status);
    List<Task> findByType(Task.TaskType type);
    List<Task> findByStatusAndType(Task.TaskStatus status, Task.TaskType type);
    
    // Admin task management methods
    Page<Task> findAll(Pageable pageable);
    Page<Task> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description, Pageable pageable);
    Page<Task> findByType(Task.TaskType type, Pageable pageable);
    
    // Task statistics methods
    Long countByStatus(Task.TaskStatus status);
    Long countByType(Task.TaskType type);
    
    @Query("SELECT COUNT(t) FROM Task t WHERE t.status = 'ACTIVE'")
    Long countActiveTasks();
    
    @Query("SELECT COUNT(t) FROM Task t WHERE FUNCTION('DATE', t.createdAt) = FUNCTION('DATE', CURRENT_DATE)")
    Long countToday();
}
package com.snail.repository;

import com.snail.entity.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void testFindByStatus() {
        // Arrange
        Task task1 = new Task();
        task1.setTitle("Active Task 1");
        task1.setStatus(Task.TaskStatus.ACTIVE);
        
        Task task2 = new Task();
        task2.setTitle("Inactive Task");
        task2.setStatus(Task.TaskStatus.INACTIVE);
        
        Task task3 = new Task();
        task3.setTitle("Active Task 2");
        task3.setStatus(Task.TaskStatus.ACTIVE);

        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);

        // Act
        List<Task> activeTasks = taskRepository.findByStatus(Task.TaskStatus.ACTIVE);

        // Assert
        assertEquals(2, activeTasks.size());
        assertTrue(activeTasks.stream().allMatch(t -> t.getStatus() == Task.TaskStatus.ACTIVE));
    }

    @Test
    void testFindByType() {
        // Arrange
        Task task1 = new Task();
        task1.setTitle("Daily Task 1");
        task1.setType(Task.TaskType.DAILY);
        
        Task task2 = new Task();
        task2.setTitle("Weekly Task");
        task2.setType(Task.TaskType.WEEKLY);
        
        Task task3 = new Task();
        task3.setTitle("Daily Task 2");
        task3.setType(Task.TaskType.DAILY);

        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);

        // Act
        List<Task> dailyTasks = taskRepository.findByType(Task.TaskType.DAILY);

        // Assert
        assertEquals(2, dailyTasks.size());
        assertTrue(dailyTasks.stream().allMatch(t -> t.getType() == Task.TaskType.DAILY));
    }

    @Test
    void testFindByStatusAndType() {
        // Arrange
        Task task1 = new Task();
        task1.setTitle("Active Daily Task 1");
        task1.setType(Task.TaskType.DAILY);
        task1.setStatus(Task.TaskStatus.ACTIVE);
        
        Task task2 = new Task();
        task2.setTitle("Inactive Daily Task");
        task2.setType(Task.TaskType.DAILY);
        task2.setStatus(Task.TaskStatus.INACTIVE);
        
        Task task3 = new Task();
        task3.setTitle("Active Daily Task 2");
        task3.setType(Task.TaskType.DAILY);
        task3.setStatus(Task.TaskStatus.ACTIVE);

        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);

        // Act
        List<Task> activeDailyTasks = taskRepository.findByStatusAndType(Task.TaskStatus.ACTIVE, Task.TaskType.DAILY);

        // Assert
        assertEquals(2, activeDailyTasks.size());
        assertTrue(activeDailyTasks.stream().allMatch(t -> 
            t.getStatus() == Task.TaskStatus.ACTIVE && t.getType() == Task.TaskType.DAILY));
    }
}
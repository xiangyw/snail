package com.snail.admin.service;

import com.snail.entity.Task;
import com.snail.dto.TaskDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AdminTaskService {
    
    /**
     * 获取所有任务（分页）
     */
    Page<TaskDto> getAllTasks(Pageable pageable);
    
    /**
     * 根据ID获取任务
     */
    Optional<Task> getTaskById(Long id);
    
    /**
     * 创建新任务
     */
    Task createTask(Task task);
    
    /**
     * 更新任务
     */
    Task updateTask(Long taskId, Task task);
    
    /**
     * 删除任务（逻辑删除）
     */
    Task deleteTask(Long taskId);
    
    /**
     * 激活/停用任务
     */
    Task toggleTaskStatus(Long taskId, String status);
    
    /**
     * 搜索任务
     */
    Page<TaskDto> searchTasks(String keyword, Pageable pageable);
    
    /**
     * 根据任务类型获取任务
     */
    Page<TaskDto> getTasksByType(String taskType, Pageable pageable);
    
    /**
     * 获取任务统计信息
     */
    TaskStatistics getTaskStatistics();
    
    /**
     * 获取活跃任务数
     */
    Long getActiveTaskCount();
    
    /**
     * 获取总任务数
     */
    Long getTotalTaskCount();
    
    /**
     * 获取已完成任务数
     */
    Long getCompletedTaskCount();
    
    class TaskStatistics {
        private Long totalTasks;
        private Long activeTasks;
        private Long inactiveTasks;
        private Long expiredTasks;
        private Long dailyTasks;
        private Long weeklyTasks;
        private Long oneTimeTasks;
        private Long contentCreationTasks;
        
        public TaskStatistics(Long totalTasks, Long activeTasks, Long inactiveTasks, Long expiredTasks,
                             Long dailyTasks, Long weeklyTasks, Long oneTimeTasks, Long contentCreationTasks) {
            this.totalTasks = totalTasks;
            this.activeTasks = activeTasks;
            this.inactiveTasks = inactiveTasks;
            this.expiredTasks = expiredTasks;
            this.dailyTasks = dailyTasks;
            this.weeklyTasks = weeklyTasks;
            this.oneTimeTasks = oneTimeTasks;
            this.contentCreationTasks = contentCreationTasks;
        }
        
        // Getters and setters
        public Long getTotalTasks() { return totalTasks; }
        public void setTotalTasks(Long totalTasks) { this.totalTasks = totalTasks; }
        
        public Long getActiveTasks() { return activeTasks; }
        public void setActiveTasks(Long activeTasks) { this.activeTasks = activeTasks; }
        
        public Long getInactiveTasks() { return inactiveTasks; }
        public void setInactiveTasks(Long inactiveTasks) { this.inactiveTasks = inactiveTasks; }
        
        public Long getExpiredTasks() { return expiredTasks; }
        public void setExpiredTasks(Long expiredTasks) { this.expiredTasks = expiredTasks; }
        
        public Long getDailyTasks() { return dailyTasks; }
        public void setDailyTasks(Long dailyTasks) { this.dailyTasks = dailyTasks; }
        
        public Long getWeeklyTasks() { return weeklyTasks; }
        public void setWeeklyTasks(Long weeklyTasks) { this.weeklyTasks = weeklyTasks; }
        
        public Long getOneTimeTasks() { return oneTimeTasks; }
        public void setOneTimeTasks(Long oneTimeTasks) { this.oneTimeTasks = oneTimeTasks; }
        
        public Long getContentCreationTasks() { return contentCreationTasks; }
        public void setContentCreationTasks(Long contentCreationTasks) { this.contentCreationTasks = contentCreationTasks; }
    }
}
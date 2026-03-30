package com.snail.controller;

import com.snail.dto.UserTaskDto;
import com.snail.entity.User;
import com.snail.service.TaskService;
import com.snail.service.UserTaskService;
import com.snail.util.AuthenticationFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-tasks")
@Tag(name = "User Tasks", description = "User task assignment and completion endpoints")
public class UserTaskController {

    @Autowired
    private UserTaskService userTaskService;

    @Autowired
    private TaskService taskService;
    
    @Autowired
    private AuthenticationFacade authenticationFacade;

    @GetMapping("/my")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Get my assigned tasks", description = "Retrieve tasks assigned to the current user")
    public ResponseEntity<List<UserTaskDto>> getMyTasks() {
        User currentUser = authenticationFacade.getCurrentUser();
        
        List<UserTaskDto> userTasks = userTaskService.getUserTasksByUser(currentUser);
        return ResponseEntity.ok(userTasks);
    }

    @GetMapping("/my/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Get my tasks by status", description = "Retrieve tasks assigned to the current user filtered by status")
    public ResponseEntity<List<UserTaskDto>> getMyTasksByStatus(@PathVariable String status) {
        User currentUser = authenticationFacade.getCurrentUser();
        
        com.snail.entity.UserTask.UserTaskStatus userTaskStatus = 
                com.snail.entity.UserTask.UserTaskStatus.valueOf(status.toUpperCase());
        
        List<UserTaskDto> userTasks = userTaskService.getUserTasksByUserAndStatus(currentUser, userTaskStatus);
        return ResponseEntity.ok(userTasks);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Get user task by ID", description = "Retrieve a specific user task by ID")
    public ResponseEntity<UserTaskDto> getUserTaskById(@PathVariable Long id) {
        UserTaskDto userTask = userTaskService.getUserTaskById(id);
        return ResponseEntity.ok(userTask);
    }

    @PostMapping("/assign/{taskId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Assign task to user", description = "Assign a task to the current user")
    public ResponseEntity<Boolean> assignTask(@PathVariable Long taskId) {
        User currentUser = authenticationFacade.getCurrentUser();
        
        Boolean success = taskService.assignTaskToUser(currentUser, taskId);
        return ResponseEntity.ok(success);
    }

    @PostMapping("/complete/{taskId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Complete task", description = "Mark a task as completed by the current user")
    public ResponseEntity<Boolean> completeTask(@PathVariable Long taskId) {
        User currentUser = authenticationFacade.getCurrentUser();
        
        Boolean success = taskService.completeTask(currentUser, taskId);
        return ResponseEntity.ok(success);
    }

    @PutMapping("/{id}/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Update user task status", description = "Update the status of a user task")
    public ResponseEntity<UserTaskDto> updateUserTaskStatus(
            @PathVariable Long id, 
            @PathVariable String status) {
        
        com.snail.entity.UserTask.UserTaskStatus userTaskStatus = 
                com.snail.entity.UserTask.UserTaskStatus.valueOf(status.toUpperCase());
        
        UserTaskDto updatedUserTask = userTaskService.updateUserTaskStatus(id, userTaskStatus);
        return ResponseEntity.ok(updatedUserTask);
    }
}
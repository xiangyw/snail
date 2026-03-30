package com.snail.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snail.dto.LoginRequest;
import com.snail.dto.AuthResponse;
import com.snail.dto.UserTaskDto;
import com.snail.entity.Task;
import com.snail.entity.User;
import com.snail.entity.UserRole;
import com.snail.entity.UserTask;
import com.snail.repository.TaskRepository;
import com.snail.repository.UserRepository;
import com.snail.repository.UserTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 用户任务 API 集成测试
 * 测试用户任务的分配、完成、状态更新等接口
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserTaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTaskRepository userTaskRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String userToken;
    private String adminToken;
    private Long testTaskId;
    private Long testUserTaskId;

    @BeforeEach
    void setUp() throws Exception {
        // 清理测试数据
        userTaskRepository.deleteAll();
        taskRepository.deleteAll();
        userRepository.deleteAll();

        // 创建测试用户
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("user@example.com");
        user.setPassword(passwordEncoder.encode("user123"));
        user.setFirstName("Test");
        user.setLastName("User");
        user.setRole(UserRole.USER);
        userRepository.save(user);

        // 创建管理员
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@example.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setRole(UserRole.ADMIN);
        userRepository.save(admin);

        // 创建测试任务
        Task task = new Task();
        task.setTitle("用户任务测试");
        task.setDescription("用于测试用户任务分配");
        task.setType(Task.TaskType.SURVEY);
        task.setStatus(Task.TaskStatus.ACTIVE);
        task.setPoints(100);
        task.setMaxCompletionCount(100);
        task.setCurrentCompletionCount(0);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        task.setExpiresAt(LocalDateTime.now().plusDays(30));
        Task savedTask = taskRepository.save(task);
        testTaskId = savedTask.getId();

        // 创建用户任务关联
        UserTask userTask = new UserTask();
        userTask.setUser(user);
        userTask.setTask(task);
        userTask.setStatus(UserTask.UserTaskStatus.ASSIGNED);
        userTask.setCreatedAt(LocalDateTime.now());
        userTask.setUpdatedAt(LocalDateTime.now());
        UserTask savedUserTask = userTaskRepository.save(userTask);
        testUserTaskId = savedUserTask.getId();

        // 获取 token
        userToken = getAuthToken("testuser", "user123");
        adminToken = getAuthToken("admin", "admin123");
    }

    private String getAuthToken(String username, String password) throws Exception {
        LoginRequest request = new LoginRequest(username, password);
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andReturn();
        String response = result.getResponse().getContentAsString();
        AuthResponse authResponse = objectMapper.readValue(response, AuthResponse.class);
        return authResponse.getAccessToken();
    }

    @Test
    @DisplayName("获取我的任务 - 成功")
    void testGetMyTasks_Success() throws Exception {
        mockMvc.perform(get("/api/user-tasks/my")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].taskId").value(testTaskId));
    }

    @Test
    @DisplayName("获取我的任务 - 未授权")
    void testGetMyTasks_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/user-tasks/my"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("根据状态获取我的任务 - 成功")
    void testGetMyTasksByStatus_Success() throws Exception {
        mockMvc.perform(get("/api/user-tasks/my/{status}", "ASSIGNED")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("根据状态获取我的任务 - 无效状态")
    void testGetMyTasksByStatus_InvalidStatus() throws Exception {
        mockMvc.perform(get("/api/user-tasks/my/{status}", "INVALID_STATUS")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("根据 ID 获取用户任务 - 成功")
    void testGetUserTaskById_Success() throws Exception {
        mockMvc.perform(get("/api/user-tasks/{id}", testUserTaskId)
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testUserTaskId))
                .andExpect(jsonPath("$.status").value("ASSIGNED"));
    }

    @Test
    @DisplayName("分配任务给用户 - 成功")
    void testAssignTask_Success() throws Exception {
        // 创建新任务用于分配测试
        Task newTask = new Task();
        newTask.setTitle("待分配任务");
        newTask.setDescription("等待分配");
        newTask.setType(Task.TaskType.SURVEY);
        newTask.setStatus(Task.TaskStatus.ACTIVE);
        newTask.setPoints(50);
        newTask.setMaxCompletionCount(100);
        newTask.setCreatedAt(LocalDateTime.now());
        newTask.setUpdatedAt(LocalDateTime.now());
        newTask.setExpiresAt(LocalDateTime.now().plusDays(30));
        Task savedTask = taskRepository.save(newTask);

        mockMvc.perform(post("/api/user-tasks/assign/{taskId}", savedTask.getId())
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("完成任务 - 成功")
    void testCompleteTask_Success() throws Exception {
        mockMvc.perform(post("/api/user-tasks/complete/{taskId}", testTaskId)
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // 验证任务状态已更新
        mockMvc.perform(get("/api/user-tasks/my")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("COMPLETED"));
    }

    @Test
    @DisplayName("更新用户任务状态 - 成功")
    void testUpdateUserTaskStatus_Success() throws Exception {
        mockMvc.perform(put("/api/user-tasks/{id}/status/{status}", testUserTaskId, "IN_PROGRESS")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testUserTaskId))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    @DisplayName("更新用户任务状态 - 无效状态")
    void testUpdateUserTaskStatus_InvalidStatus() throws Exception {
        mockMvc.perform(put("/api/user-tasks/{id}/status/{status}", testUserTaskId, "INVALID")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().is5xxServerError());
    }
}

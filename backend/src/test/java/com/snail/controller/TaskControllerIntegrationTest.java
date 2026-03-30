package com.snail.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snail.dto.LoginRequest;
import com.snail.dto.AuthResponse;
import com.snail.dto.TaskDto;
import com.snail.entity.Task;
import com.snail.entity.User;
import com.snail.entity.UserRole;
import com.snail.repository.TaskRepository;
import com.snail.repository.UserRepository;
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
 * 任务 API 集成测试
 * 测试任务列表、详情、创建、更新、删除等接口
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String adminToken;
    private String userToken;
    private Long testTaskId;

    @BeforeEach
    void setUp() throws Exception {
        // 清理测试数据
        taskRepository.deleteAll();
        userRepository.deleteAll();

        // 创建管理员用户
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@example.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setRole(UserRole.ADMIN);
        userRepository.save(admin);

        // 创建普通用户
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("user@example.com");
        user.setPassword(passwordEncoder.encode("user123"));
        user.setFirstName("Test");
        user.setLastName("User");
        user.setRole(UserRole.USER);
        userRepository.save(user);

        // 创建测试任务
        Task task = new Task();
        task.setTitle("测试任务");
        task.setDescription("这是一个测试任务");
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

        // 获取管理员 token
        adminToken = getAuthToken("admin", "admin123");
        userToken = getAuthToken("testuser", "user123");
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
    @DisplayName("获取所有任务 - 成功")
    void testGetAllTasks_Success() throws Exception {
        mockMvc.perform(get("/api/tasks")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("获取所有任务 - 未授权")
    void testGetAllTasks_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("获取活跃任务 - 成功")
    void testGetActiveTasks_Success() throws Exception {
        mockMvc.perform(get("/api/tasks/active")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("根据 ID 获取任务 - 成功")
    void testGetTaskById_Success() throws Exception {
        mockMvc.perform(get("/api/tasks/{id}", testTaskId)
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testTaskId))
                .andExpect(jsonPath("$.title").value("测试任务"));
    }

    @Test
    @DisplayName("根据 ID 获取任务 - 任务不存在")
    void testGetTaskById_NotFound() throws Exception {
        mockMvc.perform(get("/api/tasks/{id}", 9999L)
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("创建任务 - 管理员成功")
    void testCreateTask_AdminSuccess() throws Exception {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("新任务");
        taskDto.setDescription("新创建的任务");
        taskDto.setType(Task.TaskType.SURVEY);
        taskDto.setStatus(Task.TaskStatus.ACTIVE);
        taskDto.setPoints(50);
        taskDto.setMaxCompletionCount(50);

        mockMvc.perform(post("/api/tasks")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("新任务"))
                .andExpect(jsonPath("$.points").value(50));
    }

    @Test
    @DisplayName("创建任务 - 普通用户无权限")
    void testCreateTask_UserForbidden() throws Exception {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("未授权任务");
        taskDto.setDescription("普通用户不能创建任务");
        taskDto.setType(Task.TaskType.SURVEY);
        taskDto.setStatus(Task.TaskStatus.ACTIVE);
        taskDto.setPoints(50);

        mockMvc.perform(post("/api/tasks")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("更新任务 - 管理员成功")
    void testUpdateTask_AdminSuccess() throws Exception {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("更新后的任务");
        taskDto.setDescription("已更新");
        taskDto.setType(Task.TaskType.SURVEY);
        taskDto.setStatus(Task.TaskStatus.ACTIVE);
        taskDto.setPoints(200);

        mockMvc.perform(put("/api/tasks/{id}", testTaskId)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("更新后的任务"))
                .andExpect(jsonPath("$.points").value(200));
    }

    @Test
    @DisplayName("更新任务 - 普通用户无权限")
    void testUpdateTask_UserForbidden() throws Exception {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("未授权更新");
        taskDto.setPoints(999);

        mockMvc.perform(put("/api/tasks/{id}", testTaskId)
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("删除任务 - 管理员成功")
    void testDeleteTask_AdminSuccess() throws Exception {
        mockMvc.perform(delete("/api/tasks/{id}", testTaskId)
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());

        // 验证任务已被删除
        mockMvc.perform(get("/api/tasks/{id}", testTaskId)
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("删除任务 - 普通用户无权限")
    void testDeleteTask_UserForbidden() throws Exception {
        mockMvc.perform(delete("/api/tasks/{id}", testTaskId)
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }
}

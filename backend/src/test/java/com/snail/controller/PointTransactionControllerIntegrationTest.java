package com.snail.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snail.dto.LoginRequest;
import com.snail.dto.AuthResponse;
import com.snail.dto.PointTransactionDto;
import com.snail.entity.PointTransaction;
import com.snail.entity.Task;
import com.snail.entity.User;
import com.snail.entity.UserRole;
import com.snail.entity.UserTask;
import com.snail.repository.PointTransactionRepository;
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
 * 积分交易 API 集成测试
 * 测试积分余额查询、积分流水等接口
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PointTransactionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PointTransactionRepository pointTransactionRepository;

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
    private Long testUserId;

    @BeforeEach
    void setUp() throws Exception {
        // 清理测试数据
        pointTransactionRepository.deleteAll();
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
        user.setPoints(500); // 初始积分
        User savedUser = userRepository.save(user);
        testUserId = savedUser.getId();

        // 创建管理员
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@example.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setRole(UserRole.ADMIN);
        admin.setPoints(1000);
        userRepository.save(admin);

        // 创建测试积分交易记录
        PointTransaction transaction1 = new PointTransaction();
        transaction1.setUser(user);
        transaction1.setAmount(100);
        transaction1.setType(PointTransaction.TransactionType.EARN);
        transaction1.setDescription("完成任务奖励");
        transaction1.setTransactionReference("TXN001");
        transaction1.setCreatedAt(LocalDateTime.now());
        pointTransactionRepository.save(transaction1);

        PointTransaction transaction2 = new PointTransaction();
        transaction2.setUser(user);
        transaction2.setAmount(-50);
        transaction2.setType(PointTransaction.TransactionType.SPEND);
        transaction2.setDescription("兑换奖品");
        transaction2.setTransactionReference("TXN002");
        transaction2.setCreatedAt(LocalDateTime.now().minusDays(1));
        pointTransactionRepository.save(transaction2);

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
    @DisplayName("获取所有交易记录 - 管理员成功")
    void testGetAllTransactions_AdminSuccess() throws Exception {
        mockMvc.perform(get("/api/transactions")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("获取所有交易记录 - 普通用户无权限")
    void testGetAllTransactions_UserForbidden() throws Exception {
        mockMvc.perform(get("/api/transactions")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("获取我的交易记录 - 成功")
    void testGetMyTransactions_Success() throws Exception {
        mockMvc.perform(get("/api/transactions/my")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("获取我的交易记录 - 未授权")
    void testGetMyTransactions_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/transactions/my"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("获取指定用户的交易记录 - 管理员成功")
    void testGetTransactionsByUser_AdminSuccess() throws Exception {
        mockMvc.perform(get("/api/transactions/user/{userId}", testUserId)
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("获取指定用户的交易记录 - 普通用户无权限")
    void testGetTransactionsByUser_UserForbidden() throws Exception {
        mockMvc.perform(get("/api/transactions/user/{userId}", testUserId)
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("获取指定用户的交易记录 - 用户不存在")
    void testGetTransactionsByUser_UserNotFound() throws Exception {
        mockMvc.perform(get("/api/transactions/user/{userId}", 9999L)
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
}

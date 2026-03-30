package com.snail.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snail.dto.LoginRequest;
import com.snail.dto.AuthResponse;
import com.snail.dto.UserProfileDTO;
import com.snail.entity.User;
import com.snail.entity.UserRole;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 用户管理 API 集成测试
 * 测试管理员对用户的管理操作（列表、详情、更新、删除）
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String adminToken;
    private String userToken;
    private Long testUserId;

    @BeforeEach
    void setUp() throws Exception {
        // 清理测试数据
        userRepository.deleteAll();

        // 创建管理员
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@example.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setRole(UserRole.ADMIN);
        userRepository.save(admin);

        // 创建测试用户
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("user@example.com");
        user.setPassword(passwordEncoder.encode("user123"));
        user.setFirstName("Test");
        user.setLastName("User");
        user.setRole(UserRole.USER);
        User savedUser = userRepository.save(user);
        testUserId = savedUser.getId();

        // 获取 token
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
    @DisplayName("获取所有用户 - 管理员成功")
    void testGetAllUsers_AdminSuccess() throws Exception {
        mockMvc.perform(get("/api/users")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("获取所有用户 - 普通用户无权限")
    void testGetAllUsers_UserForbidden() throws Exception {
        mockMvc.perform(get("/api/users")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("获取所有用户 - 未授权")
    void testGetAllUsers_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("根据 ID 获取用户 - 管理员成功")
    void testGetUserById_AdminSuccess() throws Exception {
        mockMvc.perform(get("/api/users/{id}", testUserId)
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testUserId))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("user@example.com"));
    }

    @Test
    @DisplayName("根据 ID 获取用户 - 普通用户无权限")
    void testGetUserById_UserForbidden() throws Exception {
        mockMvc.perform(get("/api/users/{id}", testUserId)
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("根据 ID 获取用户 - 用户不存在")
    void testGetUserById_NotFound() throws Exception {
        mockMvc.perform(get("/api/users/{id}", 9999L)
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("更新用户资料 - 管理员成功")
    void testUpdateUser_AdminSuccess() throws Exception {
        UserProfileDTO profileDTO = new UserProfileDTO("Updated", "Name");

        mockMvc.perform(put("/api/users/{id}", testUserId)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profileDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"))
                .andExpect(jsonPath("$.lastName").value("Name"));
    }

    @Test
    @DisplayName("更新用户资料 - 普通用户无权限")
    void testUpdateUser_UserForbidden() throws Exception {
        UserProfileDTO profileDTO = new UserProfileDTO("Hacked", "Name");

        mockMvc.perform(put("/api/users/{id}", testUserId)
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profileDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("更新用户资料 - 用户不存在")
    void testUpdateUser_NotFound() throws Exception {
        UserProfileDTO profileDTO = new UserProfileDTO("Updated", "Name");

        mockMvc.perform(put("/api/users/{id}", 9999L)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profileDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("删除用户 - 管理员成功")
    void testDeleteUser_AdminSuccess() throws Exception {
        mockMvc.perform(delete("/api/users/{id}", testUserId)
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());

        // 验证用户已被删除
        mockMvc.perform(get("/api/users/{id}", testUserId)
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("删除用户 - 普通用户无权限")
    void testDeleteUser_UserForbidden() throws Exception {
        mockMvc.perform(delete("/api/users/{id}", testUserId)
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }
}

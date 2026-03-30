package com.snail.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snail.dto.AuthRequest;
import com.snail.dto.AuthResponse;
import com.snail.dto.LoginRequest;
import com.snail.dto.RegisterRequest;
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
 * 认证 API 集成测试
 * 测试注册、登录、登出、获取当前用户信息、更新资料等接口
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String authToken;
    private Long testUserId;

    @BeforeEach
    void setUp() {
        // 清理测试数据
        userRepository.deleteAll();
        
        // 创建测试用户
        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword(passwordEncoder.encode("password123"));
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setRole(UserRole.USER);
        
        User savedUser = userRepository.save(testUser);
        testUserId = savedUser.getId();
    }

    @Test
    @DisplayName("用户注册 - 成功")
    void testRegister_Success() throws Exception {
        RegisterRequest request = new RegisterRequest(
            "newuser",
            "newuser@example.com",
            "password123",
            "New",
            "User"
        );

        MvcResult result = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        AuthResponse authResponse = objectMapper.readValue(response, AuthResponse.class);
        assertNotNull(authResponse.getAccessToken());
        assertEquals("newuser", authResponse.getUsername());
    }

    @Test
    @DisplayName("用户注册 - 用户名已存在")
    void testRegister_UsernameAlreadyExists() throws Exception {
        RegisterRequest request = new RegisterRequest(
            "testuser",
            "another@example.com",
            "password123",
            "Another",
            "User"
        );

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    @DisplayName("用户注册 - 邮箱已存在")
    void testRegister_EmailAlreadyExists() throws Exception {
        RegisterRequest request = new RegisterRequest(
            "anotheruser",
            "test@example.com",
            "password123",
            "Another",
            "User"
        );

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    @DisplayName("用户登录 - 成功")
    void testLogin_Success() throws Exception {
        LoginRequest request = new LoginRequest("testuser", "password123");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        AuthResponse authResponse = objectMapper.readValue(response, AuthResponse.class);
        assertNotNull(authResponse.getAccessToken());
        authToken = authResponse.getAccessToken();
    }

    @Test
    @DisplayName("用户登录 - 密码错误")
    void testLogin_WrongPassword() throws Exception {
        LoginRequest request = new LoginRequest("testuser", "wrongpassword");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid credentials"));
    }

    @Test
    @DisplayName("用户登录 - 用户不存在")
    void testLogin_UserNotFound() throws Exception {
        LoginRequest request = new LoginRequest("nonexistent", "password123");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("获取当前用户信息 - 成功")
    void testGetCurrentUser_Success() throws Exception {
        // 先登录获取 token
        LoginRequest loginRequest = new LoginRequest("testuser", "password123");
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn();
        
        String response = loginResult.getResponse().getContentAsString();
        AuthResponse authResponse = objectMapper.readValue(response, AuthResponse.class);
        String token = authResponse.getAccessToken();

        mockMvc.perform(get("/api/auth/me")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @DisplayName("获取当前用户信息 - 未授权")
    void testGetCurrentUser_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("更新用户资料 - 成功")
    void testUpdateProfile_Success() throws Exception {
        // 先登录获取 token
        LoginRequest loginRequest = new LoginRequest("testuser", "password123");
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn();
        
        String response = loginResult.getResponse().getContentAsString();
        AuthResponse authResponse = objectMapper.readValue(response, AuthResponse.class);
        String token = authResponse.getAccessToken();

        com.snail.dto.UserProfileDTO profileDTO = new com.snail.dto.UserProfileDTO("Updated", "Name");

        mockMvc.perform(put("/api/auth/profile")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profileDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"))
                .andExpect(jsonPath("$.lastName").value("Name"));
    }

    @Test
    @DisplayName("用户登出 - 成功")
    void testLogout_Success() throws Exception {
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logged out successfully"));
    }

    @Test
    @DisplayName("用户注册 - 无效邮箱格式")
    void testRegister_InvalidEmail() throws Exception {
        RegisterRequest request = new RegisterRequest(
            "baduser",
            "invalid-email",
            "password123",
            "Bad",
            "User"
        );

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("用户注册 - 密码为空")
    void testRegister_EmptyPassword() throws Exception {
        RegisterRequest request = new RegisterRequest(
            "nopassuser",
            "nopass@example.com",
            "",
            "No",
            "Pass"
        );

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}

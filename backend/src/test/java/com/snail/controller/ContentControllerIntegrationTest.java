package com.snail.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snail.dto.ContentDto;
import com.snail.entity.Content;
import com.snail.entity.User;
import com.snail.entity.UserRole;
import com.snail.repository.ContentRepository;
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
 * 内容管理 API 集成测试
 * 测试内容发布、列表、详情、更新、删除等接口
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ContentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String authToken;
    private String adminToken;
    private Long testUserId;
    private Long adminUserId;
    private Long testContentId;

    @BeforeEach
    void setUp() throws Exception {
        // 清理测试数据
        contentRepository.deleteAll();
        userRepository.deleteAll();

        // 创建测试用户
        User testUser = new User();
        testUser.setUsername("contentuser");
        testUser.setEmail("contentuser@example.com");
        testUser.setPassword(passwordEncoder.encode("password123"));
        testUser.setFirstName("Content");
        testUser.setLastName("User");
        testUser.setRole(UserRole.USER);
        
        User savedUser = userRepository.save(testUser);
        testUserId = savedUser.getId();

        // 创建管理员用户
        User adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword(passwordEncoder.encode("admin123"));
        adminUser.setFirstName("Admin");
        adminUser.setLastName("User");
        adminUser.setRole(UserRole.ADMIN);
        
        User savedAdmin = userRepository.save(adminUser);
        adminUserId = savedAdmin.getId();

        // 登录获取用户 token
        loginAndGetToken("contentuser", "password123");
        
        // 登录获取管理员 token
        loginAndGetToken("admin", "admin123");
        adminToken = authToken;
        loginAndGetToken("contentuser", "password123");

        // 创建测试内容
        Content testContent = new Content();
        testContent.setTitle("Test Content");
        testContent.setBody("This is test content body");
        testContent.setType(Content.ContentType.TEXT);
        testContent.setStatus(Content.ContentStatus.PUBLISHED);
        testContent.setUser(savedUser);
        
        Content savedContent = contentRepository.save(testContent);
        testContentId = savedContent.getId();
    }

    private void loginAndGetToken(String username, String password) throws Exception {
        String loginJson = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isOk())
                .andReturn();
        
        String response = result.getResponse().getContentAsString();
        com.snail.dto.AuthResponse authResponse = objectMapper.readValue(response, com.snail.dto.AuthResponse.class);
        authToken = authResponse.getAccessToken();
    }

    // ==================== 获取内容列表测试 ====================

    @Test
    @DisplayName("获取所有内容列表 - 成功")
    void testGetAllContents_Success() throws Exception {
        mockMvc.perform(get("/api/contents")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("获取所有内容列表 - 未授权")
    void testGetAllContents_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/contents")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("获取我的内容列表 - 成功")
    void testGetMyContents_Success() throws Exception {
        mockMvc.perform(get("/api/contents/my")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Content"));
    }

    @Test
    @DisplayName("获取我的内容列表 - 未授权")
    void testGetMyContents_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/contents/my")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    // ==================== 获取内容详情测试 ====================

    @Test
    @DisplayName("获取内容详情 - 成功")
    void testGetContentById_Success() throws Exception {
        mockMvc.perform(get("/api/contents/{id}", testContentId)
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testContentId))
                .andExpect(jsonPath("$.title").value("Test Content"))
                .andExpect(jsonPath("$.body").value("This is test content body"));
    }

    @Test
    @DisplayName("获取内容详情 - 内容不存在")
    void testGetContentById_NotFound() throws Exception {
        mockMvc.perform(get("/api/contents/{id}", 999L)
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("获取内容详情 - 未授权")
    void testGetContentById_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/contents/{id}", testContentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    // ==================== 创建内容测试 ====================

    @Test
    @DisplayName("创建内容 - 成功")
    void testCreateContent_Success() throws Exception {
        ContentDto contentDto = new ContentDto();
        contentDto.setTitle("New Content");
        contentDto.setBody("This is new content body");
        contentDto.setType(Content.ContentType.TEXT);
        contentDto.setStatus(Content.ContentStatus.PUBLISHED);

        String contentJson = objectMapper.writeValueAsString(contentDto);

        MvcResult result = mockMvc.perform(post("/api/contents")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("New Content"))
                .andExpect(jsonPath("$.body").value("This is new content body"))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        ContentDto createdContent = objectMapper.readValue(response, ContentDto.class);
        assertNotNull(createdContent.getId());
        assertEquals(testUserId, createdContent.getUserId());
    }

    @Test
    @DisplayName("创建内容 - 未授权")
    void testCreateContent_Unauthorized() throws Exception {
        ContentDto contentDto = new ContentDto();
        contentDto.setTitle("New Content");
        contentDto.setBody("This is new content body");

        String contentJson = objectMapper.writeValueAsString(contentDto);

        mockMvc.perform(post("/api/contents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("创建内容 - 标题为空")
    void testCreateContent_EmptyTitle() throws Exception {
        ContentDto contentDto = new ContentDto();
        contentDto.setTitle("");
        contentDto.setBody("This is new content body");

        String contentJson = objectMapper.writeValueAsString(contentDto);

        mockMvc.perform(post("/api/contents")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentJson))
                .andExpect(status().is4xxClientError());
    }

    // ==================== 更新内容测试 ====================

    @Test
    @DisplayName("更新内容 - 成功")
    void testUpdateContent_Success() throws Exception {
        ContentDto contentDto = new ContentDto();
        contentDto.setTitle("Updated Content");
        contentDto.setBody("This is updated content body");
        contentDto.setType(Content.ContentType.IMAGE);
        contentDto.setStatus(Content.ContentStatus.PUBLISHED);

        String contentJson = objectMapper.writeValueAsString(contentDto);

        mockMvc.perform(put("/api/contents/{id}", testContentId)
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testContentId))
                .andExpect(jsonPath("$.title").value("Updated Content"))
                .andExpect(jsonPath("$.body").value("This is updated content body"))
                .andExpect(jsonPath("$.type").value("IMAGE"));
    }

    @Test
    @DisplayName("更新内容 - 内容不存在")
    void testUpdateContent_NotFound() throws Exception {
        ContentDto contentDto = new ContentDto();
        contentDto.setTitle("Updated Content");
        contentDto.setBody("This is updated content body");

        String contentJson = objectMapper.writeValueAsString(contentDto);

        mockMvc.perform(put("/api/contents/{id}", 999L)
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentJson))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("更新内容 - 无权更新他人内容")
    void testUpdateContent_NoPermission() throws Exception {
        // 用管理员创建内容
        Content adminContent = new Content();
        adminContent.setTitle("Admin Content");
        adminContent.setBody("Admin content body");
        adminContent.setType(Content.ContentType.TEXT);
        adminContent.setStatus(Content.ContentStatus.PUBLISHED);
        
        User adminUser = userRepository.findById(adminUserId).orElseThrow();
        adminContent.setUser(adminUser);
        Content savedAdminContent = contentRepository.save(adminContent);

        // 尝试用普通用户更新
        ContentDto contentDto = new ContentDto();
        contentDto.setTitle("Hacked Content");
        contentDto.setBody("Hacked content body");

        String contentJson = objectMapper.writeValueAsString(contentDto);

        mockMvc.perform(put("/api/contents/{id}", savedAdminContent.getId())
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentJson))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("更新内容 - 未授权")
    void testUpdateContent_Unauthorized() throws Exception {
        ContentDto contentDto = new ContentDto();
        contentDto.setTitle("Updated Content");

        String contentJson = objectMapper.writeValueAsString(contentDto);

        mockMvc.perform(put("/api/contents/{id}", testContentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentJson))
                .andExpect(status().isUnauthorized());
    }

    // ==================== 删除内容测试 ====================

    @Test
    @DisplayName("删除内容 - 成功")
    void testDeleteContent_Success() throws Exception {
        mockMvc.perform(delete("/api/contents/{id}", testContentId)
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // 验证内容已被删除
        mockMvc.perform(get("/api/contents/{id}", testContentId)
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("删除内容 - 内容不存在")
    void testDeleteContent_NotFound() throws Exception {
        mockMvc.perform(delete("/api/contents/{id}", 999L)
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("删除内容 - 无权删除他人内容")
    void testDeleteContent_NoPermission() throws Exception {
        // 用管理员创建内容
        Content adminContent = new Content();
        adminContent.setTitle("Admin Content To Delete");
        adminContent.setBody("Admin content body");
        adminContent.setType(Content.ContentType.TEXT);
        adminContent.setStatus(Content.ContentStatus.PUBLISHED);
        
        User adminUser = userRepository.findById(adminUserId).orElseThrow();
        adminContent.setUser(adminUser);
        Content savedAdminContent = contentRepository.save(adminContent);

        // 尝试用普通用户删除
        mockMvc.perform(delete("/api/contents/{id}", savedAdminContent.getId())
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("删除内容 - 未授权")
    void testDeleteContent_Unauthorized() throws Exception {
        mockMvc.perform(delete("/api/contents/{id}", testContentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("管理员删除任意内容 - 成功")
    void testDeleteContent_AsAdmin_Success() throws Exception {
        // 用普通用户创建内容
        Content userContent = new Content();
        userContent.setTitle("User Content");
        userContent.setBody("User content body");
        userContent.setType(Content.ContentType.TEXT);
        userContent.setStatus(Content.ContentStatus.PUBLISHED);
        
        User user = userRepository.findById(testUserId).orElseThrow();
        userContent.setUser(user);
        Content savedUserContent = contentRepository.save(userContent);

        // 用管理员删除
        mockMvc.perform(delete("/api/contents/{id}", savedUserContent.getId())
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());
    }

    // ==================== 浏览量测试 ====================

    @Test
    @DisplayName("获取内容详情时浏览量自增")
    void testGetContentById_ViewCountIncrement() throws Exception {
        // 获取初始浏览量
        MvcResult result1 = mockMvc.perform(get("/api/contents/{id}", testContentId)
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andReturn();
        
        String response1 = result1.getResponse().getContentAsString();
        ContentDto content1 = objectMapper.readValue(response1, ContentDto.class);
        int initialViewCount = content1.getViewCount();

        // 再次获取
        MvcResult result2 = mockMvc.perform(get("/api/contents/{id}", testContentId)
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andReturn();
        
        String response2 = result2.getResponse().getContentAsString();
        ContentDto content2 = objectMapper.readValue(response2, ContentDto.class);
        
        // 验证浏览量增加
        assertEquals(initialViewCount + 1, content2.getViewCount());
    }
}

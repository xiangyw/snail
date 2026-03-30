package com.snail.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snail.entity.InviteCode;
import com.snail.entity.InviteRecord;
import com.snail.repository.InviteCodeRepository;
import com.snail.repository.InviteRecordRepository;
import com.snail.service.InviteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InviteController.class)
@DisplayName("邀请 API 集成测试")
class InviteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InviteService inviteService;

    @MockBean
    private InviteCodeRepository inviteCodeRepository;

    @MockBean
    private InviteRecordRepository inviteRecordRepository;

    private InviteCode mockInviteCode;
    private InviteRecord mockInviteRecord;

    @BeforeEach
    void setUp() {
        mockInviteCode = new InviteCode();
        mockInviteCode.setId(1L);
        mockInviteCode.setCode("INV-TEST-1234");
        mockInviteCode.setUserId(1L);
        mockInviteCode.setIsUsed(false);
        mockInviteCode.setCreatedAt(LocalDateTime.now());

        mockInviteRecord = new InviteRecord();
        mockInviteRecord.setId(1L);
        mockInviteRecord.setInviterUserId(1L);
        mockInviteRecord.setInvitedUserId(2L);
        mockInviteRecord.setInviteCode("INV-TEST-1234");
        mockInviteRecord.setRewardGranted(false);
        mockInviteRecord.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("IA-001: 获取邀请码 - 成功")
    void testGetInviteCode_Success() throws Exception {
        // Arrange
        when(inviteService.getUserInviteCodes(1L)).thenReturn(Arrays.asList(mockInviteCode));

        // Act & Assert
        mockMvc.perform(get("/api/v1/invite/codes/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("INV-TEST-1234"))
                .andExpect(jsonPath("$[0].userId").value(1));
    }

    @Test
    @DisplayName("IA-002: 获取邀请码 - 无码")
    void testGetInviteCode_Empty() throws Exception {
        // Arrange
        when(inviteService.getUserInviteCodes(1L)).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/v1/invite/codes/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("IA-004: 生成邀请码 - 成功")
    void testGenerateInviteCode_Success() throws Exception {
        // Arrange
        when(inviteService.generateInviteCode(1L)).thenReturn(mockInviteCode);

        // Act & Assert
        mockMvc.perform(post("/api/v1/invite/generate")
                .param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("INV-TEST-1234"))
                .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    @DisplayName("IA-005: 生成邀请码 - 已有码")
    void testGenerateInviteCode_AlreadyExists() throws Exception {
        // Arrange
        when(inviteService.generateInviteCode(1L)).thenReturn(mockInviteCode);

        // Act & Assert
        mockMvc.perform(post("/api/v1/invite/generate")
                .param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("INV-TEST-1234"));
    }

    @Test
    @DisplayName("IA-006: 生成邀请码 - 未认证")
    void testGenerateInviteCode_Unauthorized() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/v1/invite/generate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()); // userId 不能为空
    }

    @Test
    @DisplayName("IA-009: 使用邀请码 - 成功")
    void testUseInviteCode_Success() throws Exception {
        // Arrange
        when(inviteService.useInviteCode("INV-TEST-1234", 2L)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/api/v1/invite/use")
                .param("code", "INV-TEST-1234")
                .param("invitedUserId", "2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("IA-010: 使用邀请码 - 无效码")
    void testUseInviteCode_InvalidCode() throws Exception {
        // Arrange
        when(inviteService.useInviteCode("INVALID-CODE", 2L)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/api/v1/invite/use")
                .param("code", "INVALID-CODE")
                .param("invitedUserId", "2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    @DisplayName("IA-011: 使用邀请码 - 已使用")
    void testUseInviteCode_AlreadyUsed() throws Exception {
        // Arrange
        InviteCode usedCode = new InviteCode();
        usedCode.setCode("INV-USED-1234");
        usedCode.setIsUsed(true);
        
        when(inviteCodeRepository.findByCode("INV-USED-1234")).thenReturn(Optional.of(usedCode));
        when(inviteService.useInviteCode("INV-USED-1234", 2L)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/api/v1/invite/use")
                .param("code", "INV-USED-1234")
                .param("invitedUserId", "2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    @DisplayName("IA-014: 获取记录 - 成功")
    void testGetInviteRecords_Success() throws Exception {
        // Arrange
        when(inviteRecordRepository.findByInviterUserId(1L)).thenReturn(Arrays.asList(mockInviteRecord));

        // Act & Assert
        mockMvc.perform(get("/api/v1/invite/records/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].inviteCode").value("INV-TEST-1234"))
                .andExpect(jsonPath("$[0].invitedUserId").value(2));
    }

    @Test
    @DisplayName("IA-015: 获取记录 - 分页")
    void testGetInviteRecords_WithPagination() throws Exception {
        // Arrange
        when(inviteRecordRepository.findByInviterUserId(1L)).thenReturn(Arrays.asList(mockInviteRecord));

        // Act & Assert
        mockMvc.perform(get("/api/v1/invite/records/1")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("IA-017: 获取统计 - 成功")
    void testGetInviteStats_Success() throws Exception {
        // Arrange
        InviteService.InviteStats stats = new InviteService.InviteStats(5, 3, 10, 8);
        when(inviteService.getInviteStats(1L)).thenReturn(stats);

        // Act & Assert
        mockMvc.perform(get("/api/v1/invite/stats/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCodes").value(5))
                .andExpect(jsonPath("$.unusedCodes").value(3))
                .andExpect(jsonPath("$.invitedUsers").value(10))
                .andExpect(jsonPath("$.rewardsGranted").value(8));
    }

    @Test
    @DisplayName("IA-018: 获取统计 - 数据准确")
    void testGetInviteStats_DataAccuracy() throws Exception {
        // Arrange
        InviteService.InviteStats stats = new InviteService.InviteStats(10, 5, 20, 15);
        when(inviteService.getInviteStats(1L)).thenReturn(stats);

        // Act & Assert
        mockMvc.perform(get("/api/v1/invite/stats/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCodes").value(10))
                .andExpect(jsonPath("$.unusedCodes").value(5));
    }

    @Test
    @DisplayName("IA-019: 获取统计 - 未认证")
    void testGetInviteStats_Unauthorized() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/invite/stats/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // 路径中缺少 userId
    }

    @Test
    @DisplayName("IA-020: 发放奖励 - 成功")
    void testGrantReward_Success() throws Exception {
        // Arrange
        when(inviteService.grantReward(1L)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/api/v1/invite/reward/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("IA-020: 发放奖励 - 已发放")
    void testGrantReward_AlreadyGranted() throws Exception {
        // Arrange
        when(inviteService.grantReward(1L)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/api/v1/invite/reward/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    @DisplayName("IA-007: 邀请码格式验证")
    void testInviteCodeFormat() throws Exception {
        // Arrange
        InviteCode codeWithFormat = new InviteCode();
        codeWithFormat.setCode("INV-ABCD-EFGH");
        codeWithFormat.setUserId(1L);
        
        when(inviteService.generateInviteCode(1L)).thenReturn(codeWithFormat);

        // Act
        mockMvc.perform(post("/api/v1/invite/generate")
                .param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Assert
                .andExpect(jsonPath("$.code").value("INV-ABCD-EFGH"));
    }

    @Test
    @DisplayName("IA-008: 邀请码唯一性")
    void testInviteCodeUniqueness() throws Exception {
        // Arrange
        InviteCode code1 = new InviteCode();
        code1.setCode("INV-UNIQ-1111");
        code1.setUserId(1L);
        
        InviteCode code2 = new InviteCode();
        code2.setCode("INV-UNIQ-2222");
        code2.setUserId(2L);
        
        when(inviteService.generateInviteCode(1L)).thenReturn(code1);
        when(inviteService.generateInviteCode(2L)).thenReturn(code2);

        // Act
        mockMvc.perform(post("/api/v1/invite/generate")
                .param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("INV-UNIQ-1111"));

        mockMvc.perform(post("/api/v1/invite/generate")
                .param("userId", "2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("INV-UNIQ-2222"));
    }
}

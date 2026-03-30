package com.snail.admin.service;

import com.snail.admin.entity.AdminOperationLog;
import com.snail.admin.repository.AdminOperationLogRepository;
import com.snail.admin.service.impl.AdminOperationLogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminOperationLogServiceTest {

    @Mock
    private AdminOperationLogRepository logRepository;

    @InjectMocks
    private AdminOperationLogServiceImpl adminOperationLogService;

    private AdminOperationLog mockLog;

    @BeforeEach
    void setUp() {
        mockLog = new AdminOperationLog();
        mockLog.setId(1L);
        mockLog.setAdminUserId(1L);
        mockLog.setAdminUsername("admin");
        mockLog.setOperationType("CREATE");
        mockLog.setResourceType("USER");
        mockLog.setResourceId(100L);
        mockLog.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testLogOperation() {
        when(logRepository.save(any(AdminOperationLog.class))).thenReturn(mockLog);

        AdminOperationLog result = adminOperationLogService.logOperation(
                1L, "admin", "CREATE", "USER", 100L, "Created new user", "127.0.0.1", "Mozilla/5.0");

        assertNotNull(result);
        assertEquals("admin", result.getAdminUsername());
        assertEquals("CREATE", result.getOperationType());
        verify(logRepository, times(1)).save(any(AdminOperationLog.class));
    }

    @Test
    void testGetLogsByAdminUserId() {
        List<AdminOperationLog> logs = Arrays.asList(mockLog);
        Page<AdminOperationLog> logPage = new PageImpl<>(logs);

        when(logRepository.findByAdminUserId(eq(1L), any(PageRequest.class))).thenReturn(logPage);

        Page<AdminOperationLog> result = adminOperationLogService.getLogsByAdminUserId(1L, PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(logRepository, times(1)).findByAdminUserId(eq(1L), any(PageRequest.class));
    }

    @Test
    void testGetLogsByOperationType() {
        List<AdminOperationLog> logs = Arrays.asList(mockLog);
        Page<AdminOperationLog> logPage = new PageImpl<>(logs);

        when(logRepository.findByOperationType(eq("CREATE"), any(PageRequest.class))).thenReturn(logPage);

        Page<AdminOperationLog> result = adminOperationLogService.getLogsByOperationType("CREATE", PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(logRepository, times(1)).findByOperationType(eq("CREATE"), any(PageRequest.class));
    }

    @Test
    void testGetAllLogs() {
        List<AdminOperationLog> logs = Arrays.asList(mockLog);
        Page<AdminOperationLog> logPage = new PageImpl<>(logs);

        when(logRepository.findAll(any(PageRequest.class))).thenReturn(logPage);

        Page<AdminOperationLog> result = adminOperationLogService.getAllLogs(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(logRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void testCleanupLogs() {
        List<AdminOperationLog> oldLogs = Arrays.asList(mockLog);
        Page<AdminOperationLog> logPage = new PageImpl<>(oldLogs);
        
        when(logRepository.findByCreatedAtBetween(any(LocalDateTime.class), any(LocalDateTime.class), any()))
                .thenReturn(logPage);

        adminOperationLogService.cleanupLogs(30);

        verify(logRepository, times(1)).deleteAll(oldLogs);
    }
}
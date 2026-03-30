package com.snail.service;

import com.snail.entity.InviteCode;
import com.snail.entity.InviteRecord;
import com.snail.repository.InviteCodeRepository;
import com.snail.repository.InviteRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InviteServiceTest {
    
    @Mock
    private InviteCodeRepository inviteCodeRepository;
    
    @Mock
    private InviteRecordRepository inviteRecordRepository;
    
    @InjectMocks
    private InviteService inviteService;
    
    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_INVITED_USER_ID = 2L;
    private static final String TEST_CODE = "INV-ABCD-EFGH";
    
    @BeforeEach
    void setUp() {
    }
    
    @Test
    void testGenerateInviteCode_Success() {
        // Arrange
        InviteCode expectedCode = new InviteCode();
        expectedCode.setCode(TEST_CODE);
        expectedCode.setUserId(TEST_USER_ID);
        
        when(inviteCodeRepository.countUnusedCodesByUserId(TEST_USER_ID)).thenReturn(0L);
        when(inviteCodeRepository.save(any(InviteCode.class))).thenReturn(expectedCode);
        
        // Act
        InviteCode result = inviteService.generateInviteCode(TEST_USER_ID);
        
        // Assert
        assertNotNull(result);
        assertEquals(TEST_USER_ID, result.getUserId());
        verify(inviteCodeRepository, times(1)).save(any(InviteCode.class));
    }
    
    @Test
    void testGenerateInviteCode_MaxLimitReached() {
        // Arrange
        when(inviteCodeRepository.countUnusedCodesByUserId(TEST_USER_ID)).thenReturn(5L);
        
        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            inviteService.generateInviteCode(TEST_USER_ID);
        });
    }
    
    @Test
    void testUseInviteCode_CodeNotFound() {
        // Arrange
        when(inviteCodeRepository.findByCode(TEST_CODE)).thenReturn(Optional.empty());
        
        // Act
        boolean result = inviteService.useInviteCode(TEST_CODE, TEST_INVITED_USER_ID);
        
        // Assert
        assertFalse(result);
        verify(inviteCodeRepository, times(1)).findByCode(TEST_CODE);
        verify(inviteRecordRepository, never()).save(any(InviteRecord.class));
    }
    
    @Test
    void testUseInviteCode_CodeAlreadyUsed() {
        // Arrange
        InviteCode usedCode = new InviteCode();
        usedCode.setCode(TEST_CODE);
        usedCode.setIsUsed(true);
        
        when(inviteCodeRepository.findByCode(TEST_CODE)).thenReturn(Optional.of(usedCode));
        
        // Act
        boolean result = inviteService.useInviteCode(TEST_CODE, TEST_INVITED_USER_ID);
        
        // Assert
        assertFalse(result);
        verify(inviteCodeRepository, times(1)).findByCode(TEST_CODE);
        verify(inviteRecordRepository, never()).save(any(InviteRecord.class));
    }
    
    @Test
    void testUseInviteCode_Success() {
        // Arrange
        InviteCode inviteCode = new InviteCode();
        inviteCode.setId(1L);
        inviteCode.setCode(TEST_CODE);
        inviteCode.setUserId(TEST_USER_ID);
        inviteCode.setIsUsed(false);
        
        when(inviteCodeRepository.findByCode(TEST_CODE)).thenReturn(Optional.of(inviteCode));
        when(inviteCodeRepository.save(any(InviteCode.class))).thenReturn(inviteCode);
        
        // Act
        boolean result = inviteService.useInviteCode(TEST_CODE, TEST_INVITED_USER_ID);
        
        // Assert
        assertTrue(result);
        verify(inviteCodeRepository, times(1)).findByCode(TEST_CODE);
        verify(inviteCodeRepository, times(1)).save(any(InviteCode.class));
        verify(inviteRecordRepository, times(1)).save(any(InviteRecord.class));
        
        // Verify the invite code was updated
        assertEquals(true, inviteCode.getIsUsed());
        assertEquals(TEST_INVITED_USER_ID, inviteCode.getUsedByUserId());
    }
    
    @Test
    void testGrantReward_RecordNotFound() {
        // Arrange
        when(inviteRecordRepository.findById(1L)).thenReturn(Optional.empty());
        
        // Act
        boolean result = inviteService.grantReward(1L);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    void testGrantReward_AlreadyGranted() {
        // Arrange
        InviteRecord record = new InviteRecord();
        record.setId(1L);
        record.setRewardGranted(true);
        
        when(inviteRecordRepository.findById(1L)).thenReturn(Optional.of(record));
        
        // Act
        boolean result = inviteService.grantReward(1L);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    void testGrantReward_Success() {
        // Arrange
        InviteRecord record = new InviteRecord();
        record.setId(1L);
        record.setRewardGranted(false);
        
        when(inviteRecordRepository.findById(1L)).thenReturn(Optional.of(record));
        when(inviteRecordRepository.save(any(InviteRecord.class))).thenReturn(record);
        
        // Act
        boolean result = inviteService.grantReward(1L);
        
        // Assert
        assertTrue(result);
        assertTrue(record.getRewardGranted());
        assertNotNull(record.getRewardGrantedAt());
        verify(inviteRecordRepository, times(1)).save(any(InviteRecord.class));
    }
}
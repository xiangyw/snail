package com.snail.dto;

import com.snail.entity.PointTransaction;
import com.snail.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PointTransactionDtoTest {

    @Test
    void testPointTransactionDtoCreation() {
        PointTransactionDto dto = new PointTransactionDto();
        dto.setId(1L);
        dto.setUserId(1L);
        dto.setAmount(50);
        dto.setType(PointTransaction.TransactionType.TASK_REWARD);
        dto.setDescription("Reward for completing task");
        dto.setTransactionReference("TASK_123");
        dto.setCreatedAt(LocalDateTime.now());

        assertEquals(1L, dto.getId());
        assertEquals(Long.valueOf(1L), dto.getUserId());
        assertEquals(Integer.valueOf(50), dto.getAmount());
        assertEquals(PointTransaction.TransactionType.TASK_REWARD, dto.getType());
        assertEquals("Reward for completing task", dto.getDescription());
        assertEquals("TASK_123", dto.getTransactionReference());
        assertNotNull(dto.getCreatedAt());
    }

    @Test
    void testPointTransactionDtoFromEntity() {
        PointTransaction transaction = new PointTransaction();
        User user = new User();
        user.setId(1L);
        
        transaction.setId(1L);
        transaction.setUser(user);
        transaction.setAmount(-10); // Deduction
        transaction.setType(PointTransaction.TransactionType.PENALTY_DEDUCTION);
        transaction.setDescription("Penalty for late submission");
        transaction.setTransactionReference("PENALTY_456");

        PointTransactionDto dto = PointTransactionDto.fromEntity(transaction);

        assertEquals(1L, dto.getId());
        assertEquals(Long.valueOf(1L), dto.getUserId());
        assertEquals(Integer.valueOf(-10), dto.getAmount());
        assertEquals(PointTransaction.TransactionType.PENALTY_DEDUCTION, dto.getType());
        assertEquals("Penalty for late submission", dto.getDescription());
        assertEquals("PENALTY_456", dto.getTransactionReference());
        assertNotNull(dto.getCreatedAt());
    }
}
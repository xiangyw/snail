package com.snail.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PointTransactionTest {

    @Test
    void testPointTransactionCreation() {
        PointTransaction transaction = new PointTransaction();
        User user = new User();
        user.setId(1L);
        
        transaction.setId(1L);
        transaction.setUser(user);
        transaction.setAmount(50);
        transaction.setType(PointTransaction.TransactionType.TASK_REWARD);
        transaction.setDescription("Reward for completing task");
        transaction.setTransactionReference("TASK_123");

        assertEquals(1L, transaction.getId());
        assertEquals(user, transaction.getUser());
        assertEquals(Integer.valueOf(50), transaction.getAmount());
        assertEquals(PointTransaction.TransactionType.TASK_REWARD, transaction.getType());
        assertEquals("Reward for completing task", transaction.getDescription());
        assertEquals("TASK_123", transaction.getTransactionReference());
    }

    @Test
    void testPointTransactionDefaults() {
        PointTransaction transaction = new PointTransaction();
        
        assertNull(transaction.getId());
        assertNull(transaction.getUser());
        assertNull(transaction.getAmount());
        assertNull(transaction.getType());
        assertNull(transaction.getDescription());
        assertNull(transaction.getTransactionReference());
        assertNotNull(transaction.getCreatedAt()); // Should have a creation time
    }

    @Test
    void testDifferentTransactionTypes() {
        PointTransaction transaction = new PointTransaction();
        
        // Test various transaction types
        transaction.setType(PointTransaction.TransactionType.TASK_COMPLETION);
        assertEquals(PointTransaction.TransactionType.TASK_COMPLETION, transaction.getType());
        
        transaction.setType(PointTransaction.TransactionType.REFERRAL_BONUS);
        assertEquals(PointTransaction.TransactionType.REFERRAL_BONUS, transaction.getType());
        
        transaction.setType(PointTransaction.TransactionType.DAILY_CHECKIN);
        assertEquals(PointTransaction.TransactionType.DAILY_CHECKIN, transaction.getType());
        
        transaction.setType(PointTransaction.TransactionType.ADMIN_ADJUSTMENT);
        assertEquals(PointTransaction.TransactionType.ADMIN_ADJUSTMENT, transaction.getType());
        
        transaction.setType(PointTransaction.TransactionType.PENALTY_DEDUCTION);
        assertEquals(PointTransaction.TransactionType.PENALTY_DEDUCTION, transaction.getType());
    }
}
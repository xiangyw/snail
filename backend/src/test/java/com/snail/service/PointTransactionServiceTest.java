package com.snail.service;

import com.snail.dto.PointTransactionDto;
import com.snail.entity.PointTransaction;
import com.snail.entity.User;
import com.snail.repository.PointTransactionRepository;
import com.snail.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PointTransactionServiceTest {

    @Mock
    private PointTransactionRepository pointTransactionRepository;

    @Mock
    private UserRepository userRepository;

    private PointTransactionService pointTransactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pointTransactionService = new PointTransactionService();
        // Use reflection to inject dependencies
        org.springframework.test.util.ReflectionTestUtils.setField(pointTransactionService, "pointTransactionRepository", pointTransactionRepository);
        org.springframework.test.util.ReflectionTestUtils.setField(pointTransactionService, "userRepository", userRepository);
    }

    @Test
    void testGetAllTransactions() {
        // Arrange
        PointTransaction transaction1 = new PointTransaction();
        transaction1.setId(1L);
        transaction1.setAmount(50);

        PointTransaction transaction2 = new PointTransaction();
        transaction2.setId(2L);
        transaction2.setAmount(-10);

        List<PointTransaction> transactionList = Arrays.asList(transaction1, transaction2);
        when(pointTransactionRepository.findAll()).thenReturn(transactionList);

        // Act
        List<PointTransactionDto> result = pointTransactionService.getAllTransactions();

        // Assert
        assertEquals(2, result.size());
        verify(pointTransactionRepository, times(1)).findAll();
    }

    @Test
    void testGetTransactionsByUser() {
        // Arrange
        User user = new User();
        user.setId(1L);
        
        PointTransaction transaction = new PointTransaction();
        transaction.setId(1L);
        transaction.setAmount(50);
        transaction.setUser(user);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(pointTransactionRepository.findByUser(user)).thenReturn(Arrays.asList(transaction));

        // Act
        List<PointTransactionDto> result = pointTransactionService.getTransactionsByUser(1L);

        // Assert
        assertEquals(1, result.size());
        assertEquals(Integer.valueOf(50), result.get(0).getAmount());
        verify(userRepository, times(1)).findById(1L);
        verify(pointTransactionRepository, times(1)).findByUser(user);
    }

    @Test
    void testCreateTransaction() {
        // Arrange
        User user = new User();
        user.setId(1L);

        PointTransaction savedTransaction = new PointTransaction();
        savedTransaction.setId(1L);
        savedTransaction.setUser(user);
        savedTransaction.setAmount(100);
        savedTransaction.setType(com.snail.entity.PointTransaction.TransactionType.TASK_REWARD);
        savedTransaction.setDescription("Test transaction");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(pointTransactionRepository.save(any(PointTransaction.class))).thenReturn(savedTransaction);

        // Act
        PointTransactionDto result = pointTransactionService.createTransaction(
            1L, 
            100, 
            com.snail.entity.PointTransaction.TransactionType.TASK_REWARD, 
            "Test transaction", 
            "REF123"
        );

        // Assert
        assertEquals(Long.valueOf(1L), result.getUserId());
        assertEquals(Integer.valueOf(100), result.getAmount());
        assertEquals(com.snail.entity.PointTransaction.TransactionType.TASK_REWARD, result.getType());
        assertEquals("Test transaction", result.getDescription());
        verify(userRepository, times(1)).findById(1L);
        verify(pointTransactionRepository, times(1)).save(any(PointTransaction.class));
    }

    @Test
    void testGetTransactionsByUserNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            pointTransactionService.getTransactionsByUser(1L);
        });
        
        verify(userRepository, times(1)).findById(1L);
    }
}
package com.snail.service;

import com.snail.dto.PointTransactionDto;
import com.snail.entity.PointTransaction;
import com.snail.entity.User;
import com.snail.repository.PointTransactionRepository;
import com.snail.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PointTransactionService {

    @Autowired
    private PointTransactionRepository pointTransactionRepository;

    @Autowired
    private UserRepository userRepository;

    public List<PointTransactionDto> getAllTransactions() {
        return pointTransactionRepository.findAll().stream()
                .map(PointTransactionDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PointTransactionDto> getTransactionsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
                
        return pointTransactionRepository.findByUser(user).stream()
                .map(PointTransactionDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PointTransactionDto> getRecentTransactionsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
                
        return pointTransactionRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .limit(50) // Limit to last 50 transactions
                .map(PointTransactionDto::fromEntity)
                .collect(Collectors.toList());
    }

    public PointTransactionDto createTransaction(Long userId, Integer amount, PointTransaction.TransactionType type, String description, String reference) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        PointTransaction transaction = new PointTransaction();
        transaction.setUser(user);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setDescription(description);
        transaction.setTransactionReference(reference);

        PointTransaction savedTransaction = pointTransactionRepository.save(transaction);
        return PointTransactionDto.fromEntity(savedTransaction);
    }
}
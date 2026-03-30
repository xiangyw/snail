package com.snail.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "point_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointTransaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private Integer amount; // Positive for credits, negative for debits
    
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    
    private String description;
    
    @Column(name = "transaction_reference") // Reference to related entity (e.g., task_id)
    private String transactionReference;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    public enum TransactionType {
        TASK_COMPLETION, TASK_REWARD, REFERRAL_BONUS, DAILY_CHECKIN, ADMIN_ADJUSTMENT, PENALTY_DEDUCTION
    }
}
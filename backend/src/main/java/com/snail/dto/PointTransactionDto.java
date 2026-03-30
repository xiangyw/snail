package com.snail.dto;

import com.snail.entity.PointTransaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointTransactionDto {
    private Long id;
    private Long userId;
    private Integer amount;
    private PointTransaction.TransactionType type;
    private String description;
    private String transactionReference;
    private LocalDateTime createdAt;
    
    public static PointTransactionDto fromEntity(PointTransaction transaction) {
        PointTransactionDto dto = new PointTransactionDto();
        dto.setId(transaction.getId());
        dto.setUserId(transaction.getUser().getId());
        dto.setAmount(transaction.getAmount());
        dto.setType(transaction.getType());
        dto.setDescription(transaction.getDescription());
        dto.setTransactionReference(transaction.getTransactionReference());
        dto.setCreatedAt(transaction.getCreatedAt());
        return dto;
    }
}
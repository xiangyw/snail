package com.snail.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "invite_codes")
@Data
public class InviteCode {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 20)
    private String code;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(name = "is_used")
    private Boolean isUsed = false;
    
    @Column(name = "used_at")
    private LocalDateTime usedAt;
    
    @Column(name = "used_by_user_id")
    private Long usedByUserId;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    public void generateCode() {
        if (this.code == null) {
            this.code = generateUniqueCode();
        }
    }
    
    private String generateUniqueCode() {
        // Generate a unique invite code (e.g., INV-XXXX-XXXX)
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return "INV-" + uuid.toUpperCase().substring(0, 4) + "-" + uuid.toUpperCase().substring(4, 8);
    }
}
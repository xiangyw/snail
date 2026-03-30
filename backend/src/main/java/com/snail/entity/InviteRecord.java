package com.snail.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "invite_records")
@Data
public class InviteRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "inviter_user_id", nullable = false)
    private Long inviterUserId;
    
    @Column(name = "invited_user_id", nullable = false)
    private Long invitedUserId;
    
    @Column(name = "invite_code", nullable = false, length = 20)
    private String inviteCode;
    
    @Column(name = "reward_granted")
    private Boolean rewardGranted = false;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "reward_granted_at")
    private LocalDateTime rewardGrantedAt;
}
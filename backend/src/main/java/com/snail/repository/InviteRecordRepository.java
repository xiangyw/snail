package com.snail.repository;

import com.snail.entity.InviteRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InviteRecordRepository extends JpaRepository<InviteRecord, Long> {
    
    List<InviteRecord> findByInviterUserId(Long userId);
    
    List<InviteRecord> findByInvitedUserId(Long userId);
    
    @Query("SELECT COUNT(ir) FROM InviteRecord ir WHERE ir.inviterUserId = ?1")
    Long countByInviterUserId(Long userId);
    
    @Query("SELECT COUNT(ir) FROM InviteRecord ir WHERE ir.invitedUserId = ?1")
    Long countByInvitedUserId(Long userId);
    
    @Query("SELECT COUNT(ir) FROM InviteRecord ir WHERE ir.inviterUserId = ?1 AND ir.rewardGranted = true")
    Long countRewardsGrantedByInviterUserId(Long userId);
}
package com.snail.repository;

import com.snail.entity.InviteCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InviteCodeRepository extends JpaRepository<InviteCode, Long> {
    
    Optional<InviteCode> findByCode(String code);
    
    boolean existsByCode(String code);
    
    @Query("SELECT COUNT(ic) FROM InviteCode ic WHERE ic.userId = ?1 AND ic.isUsed = false")
    Long countUnusedCodesByUserId(Long userId);
    
    @Query("SELECT COUNT(ic) FROM InviteCode ic WHERE ic.userId = ?1")
    Long countByUserId(Long userId);
    
    @Query("SELECT COUNT(ic) FROM InviteCode ic WHERE ic.usedByUserId = ?1")
    Long countUsedByUserId(Long userId);
}
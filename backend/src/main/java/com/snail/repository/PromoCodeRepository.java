package com.snail.repository;

import com.snail.entity.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, Long> {
    
    Optional<PromoCode> findByCode(String code);
    
    boolean existsByCode(String code);
    
    @Query("SELECT pc FROM PromoCode pc WHERE pc.isActive = true AND pc.expiresAt > ?1 AND pc.startsAt <= ?1")
    List<PromoCode> findActivePromoCodes(LocalDateTime now);
    
    @Query("SELECT pc FROM PromoCode pc WHERE pc.isActive = true AND pc.expiresAt > ?1 AND pc.startsAt <= ?1 AND pc.usageLimit = 0 OR (pc.usageLimit > 0 AND pc.usedCount < pc.usageLimit)")
    List<PromoCode> findAvailablePromoCodes(LocalDateTime now);
    
    @Query("SELECT pc FROM PromoCode pc WHERE pc.name LIKE %?1% AND pc.isActive = true")
    List<PromoCode> findByNameContainingAndActive(String name, LocalDateTime now);
}
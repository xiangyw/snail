package com.snail.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "promo_codes")
@Data
public class PromoCode {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 20)
    private String code;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PromoType type;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal discountAmount;
    
    @Column(name = "discount_percentage")
    private Double discountPercentage;
    
    @Column(name = "usage_limit")
    private Integer usageLimit = 0; // 0 means unlimited
    
    @Column(name = "used_count")
    private Integer usedCount = 0;
    
    @Column(name = "min_order_amount", precision = 10, scale = 2)
    private BigDecimal minOrderAmount;
    
    @Column(name = "max_discount_amount", precision = 10, scale = 2)
    private BigDecimal maxDiscountAmount;
    
    @Column(name = "starts_at")
    private LocalDateTime startsAt;
    
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
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
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return "PROMO-" + uuid.toUpperCase().substring(0, 4);
    }
    
    public enum PromoType {
        PERCENTAGE, FIXED_AMOUNT, FREE_SHIPPING
    }
}
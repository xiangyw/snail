package com.snail.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "contents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Content {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private String title;
    
    @Lob
    @Column(columnDefinition = "TEXT")
    private String body;
    
    @Enumerated(EnumType.STRING)
    private ContentType type = ContentType.TEXT;
    
    @Enumerated(EnumType.STRING)
    private ContentStatus status = ContentStatus.PUBLISHED;
    
    @Column(name = "view_count")
    private Integer viewCount = 0;
    
    @Column(name = "like_count")
    private Integer likeCount = 0;
    
    @Column(name = "comment_count")
    private Integer commentCount = 0;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @Column(name = "published_at")
    private LocalDateTime publishedAt = LocalDateTime.now();
    
    public enum ContentType {
        TEXT, IMAGE, VIDEO, AUDIO, LINK
    }
    
    public enum ContentStatus {
        DRAFT, PUBLISHED, ARCHIVED, DELETED
    }
}
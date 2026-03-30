package com.snail.repository;

import com.snail.entity.Content;
import com.snail.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    List<Content> findByUserId(Long userId);
    List<Content> findByUser(User user);
    List<Content> findByStatus(Content.ContentStatus status);
    List<Content> findByTypeAndStatus(Content.ContentType type, Content.ContentStatus status);
    List<Content> findByStatusOrderByPublishedAtDesc(Content.ContentStatus status);
    
    // Admin content management methods
    Page<Content> findByStatus(Content.ContentStatus status, Pageable pageable);
    Page<Content> findByTitleContainingIgnoreCaseOrBodyContainingIgnoreCase(String title, String body, Pageable pageable);
    
    // Content statistics methods
    Long countByStatus(Content.ContentStatus status);
    Long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT COUNT(c) FROM Content c WHERE FUNCTION('DATE', c.createdAt) = FUNCTION('DATE', CURRENT_DATE)")
    Long countToday();
}
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
    List<Content> findByVisibility(Content.ContentVisibility visibility);
    List<Content> findByTypeAndVisibility(Content.ContentType type, Content.ContentVisibility visibility);
    List<Content> findByVisibilityOrderByPublishedAtDesc(Content.ContentVisibility visibility);
    
    // Admin content management methods
    Page<Content> findByVisibility(Content.ContentVisibility visibility, Pageable pageable);
    Page<Content> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content, Pageable pageable);
    
    // Content statistics methods
    Long countByVisibility(Content.ContentVisibility visibility);
    Long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT COUNT(c) FROM Content c WHERE FUNCTION('DATE', c.createdAt) = FUNCTION('DATE', CURRENT_DATE)")
    Long countToday();
}
package com.snail.admin.service;

import com.snail.entity.Content;
import com.snail.dto.ContentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AdminContentService {
    
    /**
     * 获取所有内容（分页）
     */
    Page<ContentDto> getAllContents(Pageable pageable);
    
    /**
     * 根据ID获取内容
     */
    Optional<Content> getContentById(Long id);
    
    /**
     * 审核内容（通过/拒绝）
     */
    Content reviewContent(Long contentId, String status, String reviewerNote);
    
    /**
     * 删除内容
     */
    Content deleteContent(Long contentId);
    
    /**
     * 根据状态获取内容
     */
    Page<ContentDto> getContentsByStatus(String status, Pageable pageable);
    
    /**
     * 搜索内容
     */
    Page<ContentDto> searchContents(String keyword, Pageable pageable);
    
    /**
     * 获取内容统计信息
     */
    ContentStatistics getContentStatistics();
    
    /**
     * 获取待审核内容数量
     */
    Long getPendingReviewCount();
    
    /**
     * 获取今日新增内容数
     */
    Long getTodayContentCount();
    
    /**
     * 获取总内容数
     */
    Long getTotalContentCount();
    
    class ContentStatistics {
        private Long totalContents;
        private Long publishedContents;
        private Long draftContents;
        private Long archivedContents;
        private Long pendingReviewContents;
        private Long deletedContents;
        
        public ContentStatistics(Long totalContents, Long publishedContents, Long draftContents, 
                                Long archivedContents, Long pendingReviewContents, Long deletedContents) {
            this.totalContents = totalContents;
            this.publishedContents = publishedContents;
            this.draftContents = draftContents;
            this.archivedContents = archivedContents;
            this.pendingReviewContents = pendingReviewContents;
            this.deletedContents = deletedContents;
        }
        
        // Getters and setters
        public Long getTotalContents() { return totalContents; }
        public void setTotalContents(Long totalContents) { this.totalContents = totalContents; }
        
        public Long getPublishedContents() { return publishedContents; }
        public void setPublishedContents(Long publishedContents) { this.publishedContents = publishedContents; }
        
        public Long getDraftContents() { return draftContents; }
        public void setDraftContents(Long draftContents) { this.draftContents = draftContents; }
        
        public Long getArchivedContents() { return archivedContents; }
        public void setArchivedContents(Long archivedContents) { this.archivedContents = archivedContents; }
        
        public Long getPendingReviewContents() { return pendingReviewContents; }
        public void setPendingReviewContents(Long pendingReviewContents) { this.pendingReviewContents = pendingReviewContents; }
        
        public Long getDeletedContents() { return deletedContents; }
        public void setDeletedContents(Long deletedContents) { this.deletedContents = deletedContents; }
    }
}
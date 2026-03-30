package com.snail.admin.service.impl;

import com.snail.admin.service.AdminContentService;
import com.snail.entity.Content;
import com.snail.entity.Content.ContentStatus;
import com.snail.dto.ContentDto;
import com.snail.repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AdminContentServiceImpl implements AdminContentService {
    
    @Autowired
    private ContentRepository contentRepository;
    
    @Override
    public Page<ContentDto> getAllContents(Pageable pageable) {
        Page<Content> contents = contentRepository.findAll(pageable);
        return contents.map(this::convertToDto);
    }
    
    @Override
    public Optional<Content> getContentById(Long id) {
        return contentRepository.findById(id);
    }
    
    @Override
    public Content reviewContent(Long contentId, String status, String reviewerNote) {
        Optional<Content> contentOpt = contentRepository.findById(contentId);
        if (contentOpt.isPresent()) {
            Content content = contentOpt.get();
            try {
                ContentStatus contentStatus = ContentStatus.valueOf(status.toUpperCase());
                content.setStatus(contentStatus);
                // 这里可以添加审核备注逻辑
                return contentRepository.save(content);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status: " + status);
            }
        } else {
            throw new RuntimeException("Content not found with id: " + contentId);
        }
    }
    
    @Override
    public Content deleteContent(Long contentId) {
        Optional<Content> contentOpt = contentRepository.findById(contentId);
        if (contentOpt.isPresent()) {
            Content content = contentOpt.get();
            // 软删除，将状态设置为DELETED
            content.setStatus(ContentStatus.DELETED);
            return contentRepository.save(content);
        } else {
            throw new RuntimeException("Content not found with id: " + contentId);
        }
    }
    
    @Override
    public Page<ContentDto> getContentsByStatus(String status, Pageable pageable) {
        try {
            ContentStatus contentStatus = ContentStatus.valueOf(status.toUpperCase());
            Page<Content> contents = contentRepository.findByStatus(contentStatus, pageable);
            return contents.map(this::convertToDto);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }
    
    @Override
    public Page<ContentDto> searchContents(String keyword, Pageable pageable) {
        Page<Content> contents = contentRepository.findByTitleContainingIgnoreCaseOrBodyContainingIgnoreCase(keyword, keyword, pageable);
        return contents.map(this::convertToDto);
    }
    
    @Override
    public ContentStatistics getContentStatistics() {
        Long totalContents = contentRepository.count();
        Long publishedContents = contentRepository.countByStatus(ContentStatus.PUBLISHED);
        Long draftContents = contentRepository.countByStatus(ContentStatus.DRAFT);
        Long archivedContents = contentRepository.countByStatus(ContentStatus.ARCHIVED);
        Long pendingReviewContents = contentRepository.countByStatus(ContentStatus.DRAFT); // Draft contents need review
        Long deletedContents = contentRepository.countByStatus(ContentStatus.DELETED);
        
        return new ContentStatistics(totalContents, publishedContents, draftContents, 
                                   archivedContents, pendingReviewContents, deletedContents);
    }
    
    @Override
    public Long getPendingReviewCount() {
        // Draft contents need review
        return contentRepository.countByStatus(ContentStatus.DRAFT);
    }
    
    @Override
    public Long getTodayContentCount() {
        // Count contents created today
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        return contentRepository.countByCreatedAtBetween(startOfDay, endOfDay);
    }
    
    @Override
    public Long getTotalContentCount() {
        return contentRepository.count();
    }
    
    private ContentDto convertToDto(Content content) {
        ContentDto dto = new ContentDto();
        dto.setId(content.getId());
        dto.setTitle(content.getTitle());
        dto.setBody(content.getBody());
        dto.setType(content.getType());
        dto.setStatus(content.getStatus());
        dto.setViewCount(content.getViewCount());
        dto.setLikeCount(content.getLikeCount());
        dto.setCommentCount(content.getCommentCount());
        dto.setUserId(content.getUser().getId()); // Assuming we only need the user ID
        dto.setCreatedAt(content.getCreatedAt());
        dto.setUpdatedAt(content.getUpdatedAt());
        dto.setPublishedAt(content.getPublishedAt());
        return dto;
    }
}
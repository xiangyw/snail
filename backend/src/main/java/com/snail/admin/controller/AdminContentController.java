package com.snail.admin.controller;

import com.snail.admin.annotation.RequireAdmin;
import com.snail.admin.service.AdminContentService;
import com.snail.dto.ContentDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin/contents")
@Tag(name = "Admin Contents", description = "管理员内容管理接口")
public class AdminContentController {
    
    @Autowired
    private AdminContentService adminContentService;
    
    @GetMapping
    @Operation(summary = "获取所有内容", description = "分页获取所有内容信息")
    @RequireAdmin
    public ResponseEntity<Page<ContentDto>> getAllContents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ContentDto> contents = adminContentService.getAllContents(pageable);
        return ResponseEntity.ok(contents);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取内容", description = "根据内容ID获取内容详细信息")
    @RequireAdmin
    public ResponseEntity<?> getContentById(@PathVariable Long id) {
        Optional<com.snail.entity.Content> content = adminContentService.getContentById(id);
        if (content.isPresent()) {
            ContentDto dto = convertToDto(content.get());
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/review")
    @Operation(summary = "审核内容", description = "审核内容状态（通过/拒绝）")
    @RequireAdmin
    public ResponseEntity<com.snail.entity.Content> reviewContent(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String reviewerNote) {
        com.snail.entity.Content updatedContent = adminContentService.reviewContent(id, status, reviewerNote);
        return ResponseEntity.ok(updatedContent);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "删除内容", description = "删除指定内容")
    @RequireAdmin
    public ResponseEntity<com.snail.entity.Content> deleteContent(@PathVariable Long id) {
        com.snail.entity.Content deletedContent = adminContentService.deleteContent(id);
        return ResponseEntity.ok(deletedContent);
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态获取内容", description = "根据内容状态获取内容列表")
    @RequireAdmin
    public ResponseEntity<Page<ContentDto>> getContentsByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ContentDto> contents = adminContentService.getContentsByStatus(status, pageable);
        return ResponseEntity.ok(contents);
    }
    
    @GetMapping("/search")
    @Operation(summary = "搜索内容", description = "根据关键词搜索内容")
    @RequireAdmin
    public ResponseEntity<Page<ContentDto>> searchContents(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ContentDto> contents = adminContentService.searchContents(keyword, pageable);
        return ResponseEntity.ok(contents);
    }
    
    @GetMapping("/stats")
    @Operation(summary = "获取内容统计", description = "获取内容统计信息")
    @RequireAdmin
    public ResponseEntity<AdminContentService.ContentStatistics> getContentStatistics() {
        AdminContentService.ContentStatistics stats = adminContentService.getContentStatistics();
        return ResponseEntity.ok(stats);
    }
    
    private ContentDto convertToDto(com.snail.entity.Content content) {
        ContentDto dto = new ContentDto();
        dto.setId(content.getId());
        dto.setTitle(content.getTitle());
        dto.setBody(content.getBody());
        dto.setType(content.getType());
        dto.setStatus(content.getStatus());
        dto.setViewCount(content.getViewCount());
        dto.setLikeCount(content.getLikeCount());
        dto.setCommentCount(content.getCommentCount());
        dto.setUserId(content.getUser().getId());
        dto.setCreatedAt(content.getCreatedAt());
        dto.setUpdatedAt(content.getUpdatedAt());
        dto.setPublishedAt(content.getPublishedAt());
        return dto;
    }
}
package com.snail.dto;

import com.snail.entity.Content;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentDto {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private Content.ContentType type;
    private Content.ContentVisibility visibility;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;
    
    public static ContentDto fromEntity(Content content) {
        ContentDto dto = new ContentDto();
        dto.setId(content.getId());
        dto.setUserId(content.getUser().getId());
        dto.setTitle(content.getTitle());
        dto.setContent(content.getContent());
        dto.setType(content.getType());
        dto.setVisibility(content.getVisibility());
        dto.setViewCount(content.getViewCount());
        dto.setLikeCount(content.getLikeCount());
        dto.setCommentCount(content.getCommentCount());
        dto.setCreatedAt(content.getCreatedAt());
        dto.setUpdatedAt(content.getUpdatedAt());
        dto.setPublishedAt(content.getPublishedAt());
        return dto;
    }
    
    public static Content toEntity(ContentDto dto) {
        Content content = new Content();
        content.setId(dto.getId());
        // Note: We'll set the user separately based on authenticated user
        content.setTitle(dto.getTitle());
        content.setContent(dto.getContent());
        content.setType(dto.getType());
        content.setVisibility(dto.getVisibility());
        return content;
    }
}
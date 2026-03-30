package com.snail.dto;

import com.snail.entity.Content;
import com.snail.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ContentDtoTest {

    @Test
    void testContentDtoCreation() {
        ContentDto dto = new ContentDto();
        dto.setId(1L);
        dto.setUserId(1L);
        dto.setTitle("Test Content");
        dto.setContent("This is the content body");
        dto.setType(Content.ContentType.TEXT);
        dto.setVisibility(Content.ContentVisibility.PUBLISHED);
        dto.setViewCount(10);
        dto.setLikeCount(5);
        dto.setCommentCount(2);
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());
        dto.setPublishedAt(LocalDateTime.now());

        assertEquals(1L, dto.getId());
        assertEquals(Long.valueOf(1L), dto.getUserId());
        assertEquals("Test Content", dto.getTitle());
        assertEquals("This is the content body", dto.getContent());
        assertEquals(Content.ContentType.TEXT, dto.getType());
        assertEquals(Content.ContentVisibility.PUBLISHED, dto.getVisibility());
        assertEquals(Integer.valueOf(10), dto.getViewCount());
        assertEquals(Integer.valueOf(5), dto.getLikeCount());
        assertEquals(Integer.valueOf(2), dto.getCommentCount());
        assertNotNull(dto.getCreatedAt());
        assertNotNull(dto.getUpdatedAt());
        assertNotNull(dto.getPublishedAt());
    }

    @Test
    void testContentDtoFromEntity() {
        Content content = new Content();
        User user = new User();
        user.setId(1L);
        
        content.setId(1L);
        content.setUser(user);
        content.setTitle("Entity Content");
        content.setContent("This content came from entity");
        content.setType(Content.ContentType.IMAGE);
        content.setVisibility(Content.ContentVisibility.DRAFT);
        content.setViewCount(0);
        content.setLikeCount(0);
        content.setCommentCount(0);

        ContentDto dto = ContentDto.fromEntity(content);

        assertEquals(1L, dto.getId());
        assertEquals(Long.valueOf(1L), dto.getUserId());
        assertEquals("Entity Content", dto.getTitle());
        assertEquals("This content came from entity", dto.getContent());
        assertEquals(Content.ContentType.IMAGE, dto.getType());
        assertEquals(Content.ContentVisibility.DRAFT, dto.getVisibility());
        assertEquals(Integer.valueOf(0), dto.getViewCount());
        assertEquals(Integer.valueOf(0), dto.getLikeCount());
        assertEquals(Integer.valueOf(0), dto.getCommentCount());
        assertNotNull(dto.getCreatedAt());
        assertNotNull(dto.getUpdatedAt());
        assertNotNull(dto.getPublishedAt());
    }

    @Test
    void testContentDtoToEntity() {
        ContentDto dto = new ContentDto();
        dto.setId(2L);
        dto.setTitle("DTO Content");
        dto.setContent("This content came from DTO");
        dto.setType(Content.ContentType.VIDEO);
        dto.setVisibility(Content.ContentVisibility.ARCHIVED);

        Content content = ContentDto.toEntity(dto);

        assertEquals(2L, content.getId());
        assertEquals("DTO Content", content.getTitle());
        assertEquals("This content came from DTO", content.getContent());
        assertEquals(Content.ContentType.VIDEO, content.getType());
        assertEquals(Content.ContentVisibility.ARCHIVED, content.getVisibility());
    }
}
package com.snail.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ContentTest {

    @Test
    void testContentCreation() {
        Content content = new Content();
        User user = new User();
        user.setId(1L);
        
        content.setId(1L);
        content.setUser(user);
        content.setTitle("Test Content");
        content.setContent("This is the content body");
        content.setType(Content.ContentType.TEXT);
        content.setVisibility(Content.ContentVisibility.PUBLISHED);
        content.setViewCount(10);
        content.setLikeCount(5);
        content.setCommentCount(2);

        assertEquals(1L, content.getId());
        assertEquals(user, content.getUser());
        assertEquals("Test Content", content.getTitle());
        assertEquals("This is the content body", content.getContent());
        assertEquals(Content.ContentType.TEXT, content.getType());
        assertEquals(Content.ContentVisibility.PUBLISHED, content.getVisibility());
        assertEquals(Integer.valueOf(10), content.getViewCount());
        assertEquals(Integer.valueOf(5), content.getLikeCount());
        assertEquals(Integer.valueOf(2), content.getCommentCount());
    }

    @Test
    void testContentDefaults() {
        Content content = new Content();
        
        assertNull(content.getId());
        assertNull(content.getUser());
        assertNull(content.getTitle());
        assertNull(content.getContent());
        assertEquals(Content.ContentType.TEXT, content.getType()); // Default type
        assertEquals(Content.ContentVisibility.PUBLISHED, content.getVisibility()); // Default visibility
        assertEquals(Integer.valueOf(0), content.getViewCount()); // Default view count
        assertEquals(Integer.valueOf(0), content.getLikeCount()); // Default like count
        assertEquals(Integer.valueOf(0), content.getCommentCount()); // Default comment count
        assertNotNull(content.getCreatedAt()); // Should have a creation time
        assertNotNull(content.getUpdatedAt()); // Should have an update time
        assertNotNull(content.getPublishedAt()); // Should have a publish time
    }

    @Test
    void testDifferentContentTypes() {
        Content content = new Content();
        
        // Test various content types
        content.setType(Content.ContentType.TEXT);
        assertEquals(Content.ContentType.TEXT, content.getType());
        
        content.setType(Content.ContentType.IMAGE);
        assertEquals(Content.ContentType.IMAGE, content.getType());
        
        content.setType(Content.ContentType.VIDEO);
        assertEquals(Content.ContentType.VIDEO, content.getType());
        
        content.setType(Content.ContentType.AUDIO);
        assertEquals(Content.ContentType.AUDIO, content.getType());
        
        content.setType(Content.ContentType.LINK);
        assertEquals(Content.ContentType.LINK, content.getType());
    }

    @Test
    void testDifferentContentVisibilities() {
        Content content = new Content();
        
        // Test various content visibilities
        content.setVisibility(Content.ContentVisibility.DRAFT);
        assertEquals(Content.ContentVisibility.DRAFT, content.getVisibility());
        
        content.setVisibility(Content.ContentVisibility.PUBLISHED);
        assertEquals(Content.ContentVisibility.PUBLISHED, content.getVisibility());
        
        content.setVisibility(Content.ContentVisibility.ARCHIVED);
        assertEquals(Content.ContentVisibility.ARCHIVED, content.getVisibility());
        
        content.setVisibility(Content.ContentVisibility.DELETED);
        assertEquals(Content.ContentVisibility.DELETED, content.getVisibility());
    }
}
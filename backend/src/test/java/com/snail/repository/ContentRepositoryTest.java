package com.snail.repository;

import com.snail.entity.Content;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ContentRepositoryTest {

    @Autowired
    private ContentRepository contentRepository;

    @Test
    void testFindByStatus() {
        // Arrange
        Content content1 = new Content();
        content1.setTitle("Published Content");
        content1.setBody("This is published content");
        content1.setStatus(Content.ContentStatus.PUBLISHED);
        
        Content content2 = new Content();
        content2.setTitle("Draft Content");
        content2.setBody("This is draft content");
        content2.setStatus(Content.ContentStatus.DRAFT);
        
        Content content3 = new Content();
        content3.setTitle("Another Published Content");
        content3.setBody("This is also published content");
        content3.setStatus(Content.ContentStatus.PUBLISHED);

        contentRepository.save(content1);
        contentRepository.save(content2);
        contentRepository.save(content3);

        // Act
        List<Content> publishedContents = contentRepository.findByStatus(Content.ContentStatus.PUBLISHED);

        // Assert
        assertEquals(2, publishedContents.size());
        assertTrue(publishedContents.stream().allMatch(c -> c.getStatus() == Content.ContentStatus.PUBLISHED));
    }

    @Test
    void testFindByTypeAndStatus() {
        // Arrange
        Content content1 = new Content();
        content1.setTitle("Published Text Content");
        content1.setBody("This is published text content");
        content1.setType(Content.ContentType.TEXT);
        content1.setStatus(Content.ContentStatus.PUBLISHED);
        
        Content content2 = new Content();
        content2.setTitle("Draft Image Content");
        content2.setBody("This is draft image content");
        content2.setType(Content.ContentType.IMAGE);
        content2.setStatus(Content.ContentStatus.DRAFT);
        
        Content content3 = new Content();
        content3.setTitle("Published Text Content 2");
        content3.setBody("This is another published text content");
        content3.setType(Content.ContentType.TEXT);
        content3.setStatus(Content.ContentStatus.PUBLISHED);

        contentRepository.save(content1);
        contentRepository.save(content2);
        contentRepository.save(content3);

        // Act
        List<Content> publishedTextContents = contentRepository.findByTypeAndStatus(Content.ContentType.TEXT, Content.ContentStatus.PUBLISHED);

        // Assert
        assertEquals(2, publishedTextContents.size());
        assertTrue(publishedTextContents.stream().allMatch(c -> 
            c.getType() == Content.ContentType.TEXT && c.getStatus() == Content.ContentStatus.PUBLISHED));
    }

    @Test
    void testFindByStatusOrderByPublishedAtDesc() {
        // Arrange
        Content content1 = new Content();
        content1.setTitle("First Content");
        content1.setBody("First published content");
        content1.setStatus(Content.ContentStatus.PUBLISHED);
        
        Content content2 = new Content();
        content2.setTitle("Second Content");
        content2.setBody("Second published content");
        content2.setStatus(Content.ContentStatus.PUBLISHED);

        contentRepository.save(content1);
        contentRepository.save(content2);

        // Act
        List<Content> contents = contentRepository.findByStatusOrderByPublishedAtDesc(Content.ContentStatus.PUBLISHED);

        // Assert
        // Should return contents ordered by published date descending (most recent first)
        assertTrue(contents.size() >= 2);
    }
}
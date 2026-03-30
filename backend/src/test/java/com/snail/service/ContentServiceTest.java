package com.snail.service;

import com.snail.dto.ContentDto;
import com.snail.entity.Content;
import com.snail.entity.User;
import com.snail.repository.ContentRepository;
import com.snail.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ContentServiceTest {

    @Mock
    private ContentRepository contentRepository;

    @Mock
    private UserRepository userRepository;

    private ContentService contentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        contentService = new ContentService();
        // Use reflection to inject dependencies
        org.springframework.test.util.ReflectionTestUtils.setField(contentService, "contentRepository", contentRepository);
        org.springframework.test.util.ReflectionTestUtils.setField(contentService, "userRepository", userRepository);
    }

    @Test
    void testGetAllContents() {
        // Arrange
        Content content1 = new Content();
        content1.setId(1L);
        content1.setTitle("Content 1");

        Content content2 = new Content();
        content2.setId(2L);
        content2.setTitle("Content 2");

        List<Content> contentList = Arrays.asList(content1, content2);
        when(contentRepository.findAll()).thenReturn(contentList);

        // Act
        List<ContentDto> result = contentService.getAllContents();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Content 1", result.get(0).getTitle());
        assertEquals("Content 2", result.get(1).getTitle());
        verify(contentRepository, times(1)).findAll();
    }

    @Test
    void testGetContentById() {
        // Arrange
        Content content = new Content();
        content.setId(1L);
        content.setTitle("Test Content");
        content.setBody("Test Body");
        
        when(contentRepository.findById(1L)).thenReturn(Optional.of(content));

        // Act
        ContentDto result = contentService.getContentById(1L);

        // Assert
        assertEquals(1L, result.getId());
        assertEquals("Test Content", result.getTitle());
        verify(contentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetContentByIdNotFound() {
        // Arrange
        when(contentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            contentService.getContentById(1L);
        });
        
        verify(contentRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateContent() {
        // Arrange
        User user = new User();
        user.setId(1L);

        ContentDto contentDto = new ContentDto();
        contentDto.setTitle("New Content");
        contentDto.setBody("New Content Body");
        contentDto.setType(Content.ContentType.TEXT);

        Content savedContent = new Content();
        savedContent.setId(1L);
        savedContent.setTitle("New Content");
        savedContent.setBody("New Content Body");
        savedContent.setUser(user);

        when(contentRepository.save(any(Content.class))).thenReturn(savedContent);

        // Act
        ContentDto result = contentService.createContent(contentDto, user);

        // Assert
        assertEquals("New Content", result.getTitle());
        assertEquals("New Content Body", result.getBody());
        verify(contentRepository, times(1)).save(any(Content.class));
    }

    @Test
    void testIncrementViewCount() {
        // Arrange
        Content content = new Content();
        content.setId(1L);
        content.setTitle("Test Content");
        content.setViewCount(5);

        when(contentRepository.findById(1L)).thenReturn(Optional.of(content));
        when(contentRepository.save(any(Content.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        contentService.incrementViewCount(1L);

        // Assert
        verify(contentRepository, times(1)).findById(1L);
        verify(contentRepository, times(1)).save(any(Content.class));
        assertEquals(6, content.getViewCount().intValue()); // Should be incremented
    }
}
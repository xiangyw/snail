package com.snail.admin.service;

import com.snail.admin.service.impl.AdminContentServiceImpl;
import com.snail.entity.Content;
import com.snail.entity.User;
import com.snail.repository.ContentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminContentServiceTest {

    @Mock
    private ContentRepository contentRepository;

    @InjectMocks
    private AdminContentServiceImpl adminContentService;

    private Content mockContent;
    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");

        mockContent = new Content();
        mockContent.setId(1L);
        mockContent.setTitle("Test Content");
        mockContent.setBody("Test Body");
        mockContent.setUser(mockUser);
        mockContent.setCreatedAt(LocalDateTime.now());
        mockContent.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testGetAllContents() {
        List<Content> contents = Arrays.asList(mockContent);
        Page<Content> contentPage = new PageImpl<>(contents);

        when(contentRepository.findAll(any(PageRequest.class))).thenReturn(contentPage);

        Page<com.snail.dto.ContentDto> result = adminContentService.getAllContents(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(contentRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void testGetContentById() {
        when(contentRepository.findById(eq(1L))).thenReturn(Optional.of(mockContent));

        Optional<Content> result = adminContentService.getContentById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Content", result.get().getTitle());
        verify(contentRepository, times(1)).findById(eq(1L));
    }

    @Test
    void testReviewContent() {
        when(contentRepository.findById(eq(1L))).thenReturn(Optional.of(mockContent));
        when(contentRepository.save(any(Content.class))).thenReturn(mockContent);

        Content result = adminContentService.reviewContent(1L, "PUBLISHED", "Approved");

        assertNotNull(result);
        assertEquals(Content.ContentStatus.PUBLISHED, result.getStatus());
        verify(contentRepository, times(1)).findById(eq(1L));
        verify(contentRepository, times(1)).save(any(Content.class));
    }

    @Test
    void testDeleteContent() {
        when(contentRepository.findById(eq(1L))).thenReturn(Optional.of(mockContent));
        when(contentRepository.save(any(Content.class))).thenReturn(mockContent);

        Content result = adminContentService.deleteContent(1L);

        assertNotNull(result);
        assertEquals(Content.ContentStatus.DELETED, result.getStatus());
        verify(contentRepository, times(1)).findById(eq(1L));
        verify(contentRepository, times(1)).save(any(Content.class));
    }

    @Test
    void testGetContentsByStatus() {
        List<Content> contents = Arrays.asList(mockContent);
        Page<Content> contentPage = new PageImpl<>(contents);

        when(contentRepository.findByStatus(eq(Content.ContentStatus.PUBLISHED), any(PageRequest.class)))
                .thenReturn(contentPage);

        Page<com.snail.dto.ContentDto> result = adminContentService.getContentsByStatus("PUBLISHED", PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(contentRepository, times(1))
                .findByStatus(eq(Content.ContentStatus.PUBLISHED), any(PageRequest.class));
    }

    @Test
    void testSearchContents() {
        List<Content> contents = Arrays.asList(mockContent);
        Page<Content> contentPage = new PageImpl<>(contents);

        when(contentRepository.findByTitleContainingIgnoreCaseOrBodyContainingIgnoreCase(
                eq("test"), eq("test"), any(PageRequest.class))).thenReturn(contentPage);

        Page<com.snail.dto.ContentDto> result = adminContentService.searchContents("test", PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(contentRepository, times(1))
                .findByTitleContainingIgnoreCaseOrBodyContainingIgnoreCase(
                        eq("test"), eq("test"), any(PageRequest.class));
    }

    @Test
    void testGetContentStatistics() {
        when(contentRepository.count()).thenReturn(100L);
        when(contentRepository.countByStatus(eq(Content.ContentStatus.PUBLISHED))).thenReturn(60L);
        when(contentRepository.countByStatus(eq(Content.ContentStatus.DRAFT))).thenReturn(20L);
        when(contentRepository.countByStatus(eq(Content.ContentStatus.ARCHIVED))).thenReturn(10L);
        when(contentRepository.countByStatus(eq(Content.ContentStatus.DELETED))).thenReturn(5L);

        AdminContentService.ContentStatistics stats = adminContentService.getContentStatistics();

        assertNotNull(stats);
        assertEquals(100L, stats.getTotalContents());
        assertEquals(60L, stats.getPublishedContents());
        verify(contentRepository, times(1)).count();
        verify(contentRepository, times(1)).countByStatus(eq(Content.ContentStatus.PUBLISHED));
        verify(contentRepository, times(1)).countByStatus(eq(Content.ContentStatus.DRAFT));
        verify(contentRepository, times(1)).countByStatus(eq(Content.ContentStatus.ARCHIVED));
        verify(contentRepository, times(1)).countByStatus(eq(Content.ContentStatus.DELETED));
    }
}
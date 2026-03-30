package com.snail.service;

import com.snail.dto.ContentDto;
import com.snail.entity.Content;
import com.snail.entity.User;
import com.snail.repository.ContentRepository;
import com.snail.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContentService {

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ContentDto> getAllContents() {
        return contentRepository.findAll().stream()
                .map(ContentDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ContentDto> getContentsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
                
        return contentRepository.findByUser(user).stream()
                .map(ContentDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ContentDto> getPublishedContents() {
        return contentRepository.findByVisibilityOrderByPublishedAtDesc(Content.ContentVisibility.PUBLISHED).stream()
                .map(ContentDto::fromEntity)
                .collect(Collectors.toList());
    }

    public ContentDto getContentById(Long id) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Content not found with id: " + id));
        return ContentDto.fromEntity(content);
    }

    public ContentDto createContent(ContentDto contentDto, User user) {
        Content content = ContentDto.toEntity(contentDto);
        content.setUser(user);

        Content savedContent = contentRepository.save(content);
        return ContentDto.fromEntity(savedContent);
    }

    public ContentDto updateContent(Long id, ContentDto contentDto, User user) {
        Content existingContent = contentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Content not found with id: " + id));

        // Verify that the user owns this content
        if (!existingContent.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You don't have permission to update this content");
        }

        existingContent.setTitle(contentDto.getTitle());
        existingContent.setContent(contentDto.getContent());
        existingContent.setType(contentDto.getType());
        existingContent.setVisibility(contentDto.getVisibility());

        Content updatedContent = contentRepository.save(existingContent);
        return ContentDto.fromEntity(updatedContent);
    }

    public void deleteContent(Long id, User user) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Content not found with id: " + id));

        // Verify that the user owns this content
        if (!content.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You don't have permission to delete this content");
        }

        contentRepository.delete(content);
    }

    public void incrementViewCount(Long id) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Content not found with id: " + id));

        content.setViewCount(content.getViewCount() + 1);
        contentRepository.save(content);
    }
}
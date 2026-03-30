package com.snail.controller;

import com.snail.dto.ContentDto;
import com.snail.entity.User;
import com.snail.service.ContentService;
import com.snail.util.AuthenticationFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contents")
@Tag(name = "Contents", description = "Content management endpoints")
public class ContentController {

    @Autowired
    private ContentService contentService;
    
    @Autowired
    private AuthenticationFacade authenticationFacade;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Get all contents", description = "Retrieve all published contents")
    public ResponseEntity<List<ContentDto>> getAllContents() {
        List<ContentDto> contents = contentService.getPublishedContents();
        return ResponseEntity.ok(contents);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Get my contents", description = "Retrieve contents created by the current user")
    public ResponseEntity<List<ContentDto>> getMyContents() {
        User currentUser = authenticationFacade.getCurrentUser();
        List<ContentDto> contents = contentService.getContentsByUser(currentUser.getId());
        return ResponseEntity.ok(contents);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Get content by ID", description = "Retrieve a specific content by ID")
    public ResponseEntity<ContentDto> getContentById(@PathVariable Long id) {
        ContentDto content = contentService.getContentById(id);
        contentService.incrementViewCount(id); // Increment view count when content is accessed
        return ResponseEntity.ok(content);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Create content", description = "Create a new content")
    public ResponseEntity<ContentDto> createContent(@RequestBody ContentDto contentDto) {
        User currentUser = authenticationFacade.getCurrentUser();
        ContentDto createdContent = contentService.createContent(contentDto, currentUser);
        return ResponseEntity.ok(createdContent);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == @authenticationFacade.getCurrentUserId()")
    @Operation(summary = "Update content", description = "Update a specific content")
    public ResponseEntity<ContentDto> updateContent(@PathVariable Long id, @RequestBody ContentDto contentDto) {
        User currentUser = authenticationFacade.getCurrentUser();
        ContentDto updatedContent = contentService.updateContent(id, contentDto, currentUser);
        return ResponseEntity.ok(updatedContent);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == @authenticationFacade.getCurrentUserId()")
    @Operation(summary = "Delete content", description = "Delete a specific content")
    public ResponseEntity<Void> deleteContent(@PathVariable Long id) {
        User currentUser = authenticationFacade.getCurrentUser();
        contentService.deleteContent(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}
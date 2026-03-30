package com.snail.admin.service;

import com.snail.admin.service.impl.AdminUserServiceImpl;
import com.snail.entity.User;
import com.snail.entity.UserRole;
import com.snail.repository.UserRepository;
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
class AdminUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminUserServiceImpl adminUserService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password");
        mockUser.setRole(UserRole.USER);
        mockUser.setIsActive(true);
        mockUser.setCreatedAt(LocalDateTime.now());
        mockUser.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testGetAllUsers() {
        List<User> users = Arrays.asList(mockUser);
        Page<User> userPage = new PageImpl<>(users);

        when(userRepository.findAll(any(PageRequest.class))).thenReturn(userPage);

        Page<com.snail.dto.UserDto> result = adminUserService.getAllUsers(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(userRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(mockUser));

        Optional<User> result = adminUserService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
        verify(userRepository, times(1)).findById(eq(1L));
    }

    @Test
    void testUpdateUserRole() {
        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User result = adminUserService.updateUserRole(1L, "ADMIN");

        assertNotNull(result);
        assertEquals(UserRole.ADMIN, result.getRole());
        verify(userRepository, times(1)).findById(eq(1L));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testToggleUserStatus() {
        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User result = adminUserService.toggleUserStatus(1L, false);

        assertNotNull(result);
        assertFalse(result.getIsActive());
        verify(userRepository, times(1)).findById(eq(1L));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testDeleteUser() {
        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User result = adminUserService.deleteUser(1L);

        assertNotNull(result);
        assertFalse(result.getIsActive());
        verify(userRepository, times(1)).findById(eq(1L));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testSearchUsers() {
        List<User> users = Arrays.asList(mockUser);
        Page<User> userPage = new PageImpl<>(users);

        when(userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                eq("test"), eq("test"), any(PageRequest.class))).thenReturn(userPage);

        Page<com.snail.dto.UserDto> result = adminUserService.searchUsers("test", PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(userRepository, times(1))
                .findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                        eq("test"), eq("test"), any(PageRequest.class));
    }

    @Test
    void testGetUserStatistics() {
        when(userRepository.count()).thenReturn(100L);
        when(userRepository.countByIsActiveTrue()).thenReturn(80L);
        when(userRepository.countByRole(eq(UserRole.ADMIN))).thenReturn(5L);
        when(userRepository.countByRole(eq(UserRole.MODERATOR))).thenReturn(10L);
        when(userRepository.countByRole(eq(UserRole.USER))).thenReturn(85L);

        AdminUserService.UserStatistics stats = adminUserService.getUserStatistics();

        assertNotNull(stats);
        assertEquals(100L, stats.getTotalUsers());
        assertEquals(80L, stats.getActiveUsers());
        assertEquals(5L, stats.getAdminUsers());
        verify(userRepository, times(1)).count();
        verify(userRepository, times(1)).countByIsActiveTrue();
        verify(userRepository, times(1)).countByRole(eq(UserRole.ADMIN));
        verify(userRepository, times(1)).countByRole(eq(UserRole.MODERATOR));
        verify(userRepository, times(1)).countByRole(eq(UserRole.USER));
    }
}
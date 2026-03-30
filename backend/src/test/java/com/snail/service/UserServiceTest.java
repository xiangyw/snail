package com.snail.service;

import com.snail.dto.RegisterRequest;
import com.snail.entity.User;
import com.snail.entity.UserRole;
import com.snail.repository.UserRepository;
import com.snail.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl();
        // Use reflection to inject mocks
        org.springframework.test.util.ReflectionTestUtils.setField(userService, "userRepository", userRepository);
        org.springframework.test.util.ReflectionTestUtils.setField(userService, "passwordEncoder", passwordEncoder);
    }

    @Test
    void registerUser_ShouldEncodePasswordAndSaveUser() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setFirstName("Test");
        request.setLastName("User");

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        User savedUser = new User("testuser", "test@example.com", "encodedPassword");
        savedUser.setId(1L);
        savedUser.setFirstName("Test");
        savedUser.setLastName("User");
        savedUser.setRole(UserRole.USER);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = userService.registerUser(request);

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("encodedPassword", result.getPassword()); // Should be encoded
        assertEquals("Test", result.getFirstName());
        assertEquals("User", result.getLastName());
        assertEquals(UserRole.USER, result.getRole());
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode("password");
    }

    @Test
    void findById_ShouldReturnUser_WhenUserExists() {
        // Arrange
        User expectedUser = new User("testuser", "test@example.com", "password");
        expectedUser.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(expectedUser));

        // Act
        User result = userService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(expectedUser.getId(), result.getId());
        assertEquals(expectedUser.getUsername(), result.getUsername());
    }

    @Test
    void findById_ShouldReturnNull_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        User result = userService.findById(999L);

        // Assert
        assertNull(result);
    }
}
package com.snail.service;

import com.snail.dto.AuthRequest;
import com.snail.dto.AuthResponse;
import com.snail.dto.RegisterRequest;
import com.snail.entity.User;
import com.snail.repository.UserRepository;
import com.snail.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthService();
        // Use reflection to inject dependencies
        org.springframework.test.util.ReflectionTestUtils.setField(authService, "authenticationManager", authenticationManager);
        org.springframework.test.util.ReflectionTestUtils.setField(authService, "userRepository", userRepository);
        org.springframework.test.util.ReflectionTestUtils.setField(authService, "jwtUtil", jwtUtil);
        org.springframework.test.util.ReflectionTestUtils.setField(authService, "passwordEncoder", passwordEncoder);
    }

    @Test
    void testAuthenticate() {
        // Arrange
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("testuser");
        authRequest.setPassword("password");

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("encoded_password");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(user)).thenReturn("jwt_token");
        when(passwordEncoder.encode(any(String.class))).thenReturn("encoded_password");

        // Act
        AuthResponse response = authService.authenticate(authRequest);

        // Assert
        assertNotNull(response);
        assertEquals("jwt_token", response.getToken());
        verify(authenticationManager, times(1))
            .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(jwtUtil, times(2)).generateToken(user); // Called twice - once for token, once for refresh
    }

    @Test
    void testRegister() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("newuser");
        registerRequest.setEmail("newuser@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setFirstName("New");
        registerRequest.setLastName("User");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("newuser");
        savedUser.setEmail("newuser@example.com");
        savedUser.setPassword("encoded_password");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("newuser@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(passwordEncoder.encode("password123")).thenReturn("encoded_password");
        when(jwtUtil.generateToken(savedUser)).thenReturn("jwt_token");

        // Act
        AuthResponse response = authService.register(registerRequest);

        // Assert
        assertNotNull(response);
        assertEquals("jwt_token", response.getToken());
        assertEquals("newuser", response.getUser().getUsername());
        assertEquals("newuser@example.com", response.getUser().getEmail());
        verify(userRepository, times(1)).existsByUsername("newuser");
        verify(userRepository, times(1)).existsByEmail("newuser@example.com");
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode("password123");
        verify(jwtUtil, times(2)).generateToken(savedUser); // Called twice - once for token, once for refresh
    }

    @Test
    void testRegisterUsernameExists() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("existinguser");
        registerRequest.setEmail("newuser@example.com");
        registerRequest.setPassword("password123");

        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            authService.register(registerRequest);
        });
        
        verify(userRepository, times(1)).existsByUsername("existinguser");
        verify(userRepository, never()).existsByEmail(any(String.class));
    }

    @Test
    void testRegisterEmailExists() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("newuser");
        registerRequest.setEmail("existing@example.com");
        registerRequest.setPassword("password123");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            authService.register(registerRequest);
        });
        
        verify(userRepository, times(1)).existsByUsername("newuser");
        verify(userRepository, times(1)).existsByEmail("existing@example.com");
    }
}
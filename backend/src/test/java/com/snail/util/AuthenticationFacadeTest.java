package com.snail.util;

import com.snail.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.TestAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationFacadeTest {

    private AuthenticationFacade authenticationFacade;

    @BeforeEach
    void setUp() {
        authenticationFacade = new AuthenticationFacade();
    }

    @Test
    void testGetAuthentication() {
        // Arrange
        Authentication expectedAuth = new TestAuthenticationToken("user", "password", "ROLE_USER");
        SecurityContextHolder.getContext().setAuthentication(expectedAuth);

        // Act
        Authentication actualAuth = authenticationFacade.getAuthentication();

        // Assert
        assertEquals(expectedAuth, actualAuth);
    }

    @Test
    void testGetCurrentUserId() {
        // Arrange
        User user = new User();
        user.setId(123L);
        user.setUsername("testuser");
        
        Authentication auth = new TestAuthenticationToken(user, "password", "ROLE_USER");
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Act
        Long userId = authenticationFacade.getCurrentUserId();

        // Assert
        assertEquals(Long.valueOf(123L), userId);
    }

    @Test
    void testGetCurrentUser() {
        // Arrange
        User expectedUser = new User();
        expectedUser.setId(123L);
        expectedUser.setUsername("testuser");
        
        Authentication auth = new TestAuthenticationToken(expectedUser, "password", "ROLE_USER");
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Act
        User actualUser = authenticationFacade.getCurrentUser();

        // Assert
        assertEquals(expectedUser, actualUser);
        assertEquals(Long.valueOf(123L), actualUser.getId());
        assertEquals("testuser", actualUser.getUsername());
    }

    @Test
    void testIsAdminTrue() {
        // Arrange
        User user = new User();
        user.setRole(User.UserRole.ADMIN);
        
        Authentication auth = new TestAuthenticationToken(user, "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Act
        boolean isAdmin = authenticationFacade.isAdmin();

        // Assert
        assertTrue(isAdmin);
    }

    @Test
    void testIsAdminFalse() {
        // Arrange
        User user = new User();
        user.setRole(User.UserRole.USER);
        
        Authentication auth = new TestAuthenticationToken(user, "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Act
        boolean isAdmin = authenticationFacade.isAdmin();

        // Assert
        assertFalse(isAdmin);
    }

    @Test
    void testGetCurrentUserIdNoAuthentication() {
        // Arrange
        SecurityContextHolder.getContext().setAuthentication(null);

        // Act
        Long userId = authenticationFacade.getCurrentUserId();

        // Assert
        assertNull(userId);
    }

    @Test
    void testGetCurrentUserNoAuthentication() {
        // Arrange
        SecurityContextHolder.getContext().setAuthentication(null);

        // Act
        User user = authenticationFacade.getCurrentUser();

        // Assert
        assertNull(user);
    }
}
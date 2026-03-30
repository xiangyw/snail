package com.snail.entity;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserCreation() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(User.UserRole.USER);
        user.setPoints(100);

        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals(User.UserRole.USER, user.getRole());
        assertEquals(Integer.valueOf(100), user.getPoints());
    }

    @Test
    void testUserAuthorities() {
        User user = new User();
        user.setRole(User.UserRole.ADMIN);

        Collection authorities = user.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    void testUserEnabledStatus() {
        User user = new User();
        user.setActive(true);
        assertTrue(user.isEnabled());

        user.setActive(false);
        assertFalse(user.isEnabled());
    }

    @Test
    void testUserDefaults() {
        User user = new User();
        
        assertNull(user.getId());
        assertNull(user.getUsername());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertEquals(User.UserRole.USER, user.getRole()); // Default role
        assertTrue(user.getIsActive()); // Default active status
        assertEquals(Integer.valueOf(0), user.getPoints()); // Default points
        assertNotNull(user.getCreatedAt()); // Should have a creation time
    }
}
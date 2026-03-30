package com.snail.dto;

import com.snail.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserDtoTest {

    @Test
    void testUserDtoCreation() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("testuser");
        userDto.setEmail("test@example.com");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setRole(User.UserRole.USER);
        userDto.setIsActive(true);
        userDto.setPoints(100);
        userDto.setCreatedAt(LocalDateTime.now());
        userDto.setUpdatedAt(LocalDateTime.now());

        assertEquals(1L, userDto.getId());
        assertEquals("testuser", userDto.getUsername());
        assertEquals("test@example.com", userDto.getEmail());
        assertEquals("John", userDto.getFirstName());
        assertEquals("Doe", userDto.getLastName());
        assertEquals(User.UserRole.USER, userDto.getRole());
        assertTrue(userDto.getIsActive());
        assertEquals(Integer.valueOf(100), userDto.getPoints());
        assertNotNull(userDto.getCreatedAt());
        assertNotNull(userDto.getUpdatedAt());
    }

    @Test
    void testUserDtoFromEntity() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setRole(User.UserRole.ADMIN);
        user.setIsActive(false);
        user.setPoints(250);

        UserDto userDto = UserDto.fromEntity(user);

        assertEquals(1L, userDto.getId());
        assertEquals("testuser", userDto.getUsername());
        assertEquals("test@example.com", userDto.getEmail());
        assertEquals("John", userDto.getFirstName());
        assertEquals("Doe", userDto.getLastName());
        assertEquals(User.UserRole.ADMIN, userDto.getRole());
        assertFalse(userDto.getIsActive());
        assertEquals(Integer.valueOf(250), userDto.getPoints());
    }
}
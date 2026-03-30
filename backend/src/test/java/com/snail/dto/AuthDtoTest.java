package com.snail.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthDtoTest {

    @Test
    void testAuthRequestCreation() {
        AuthRequest request = new AuthRequest();
        request.setUsername("testuser");
        request.setPassword("password123");

        assertEquals("testuser", request.getUsername());
        assertEquals("password123", request.getPassword());
    }

    @Test
    void testAuthResponseCreation() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("testuser");
        userDto.setEmail("test@example.com");

        AuthResponse response = new AuthResponse();
        response.setToken("jwt_token_here");
        response.setRefreshToken("refresh_token_here");
        response.setTokenType("Bearer");
        response.setExpiresIn(86400L);
        response.setUser(userDto);

        assertEquals("jwt_token_here", response.getToken());
        assertEquals("refresh_token_here", response.getRefreshToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals(Long.valueOf(86400L), response.getExpiresIn());
        assertEquals(userDto, response.getUser());
    }

    @Test
    void testRegisterRequestCreation() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setEmail("newuser@example.com");
        request.setPassword("password123");
        request.setFirstName("New");
        request.setLastName("User");

        assertEquals("newuser", request.getUsername());
        assertEquals("newuser@example.com", request.getEmail());
        assertEquals("password123", request.getPassword());
        assertEquals("New", request.getFirstName());
        assertEquals("User", request.getLastName());
    }
}
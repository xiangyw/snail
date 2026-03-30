package com.snail.util;

import com.snail.entity.User;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private User testUser;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "SECRET_KEY", "snail-app-secret-key-for-jwt-token-generation-and-verification-purpose-only");
        ReflectionTestUtils.setField(jwtUtil, "JWT_EXPIRATION", 86400000L); // 24 hours

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password");
    }

    @Test
    void testGenerateToken() {
        String token = jwtUtil.generateToken(testUser);
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void testExtractUsernameFromToken() {
        String token = jwtUtil.generateToken(testUser);
        String username = jwtUtil.extractUsername(token);
        assertEquals("testuser", username);
    }

    @Test
    void testValidateToken() {
        String token = jwtUtil.generateToken(testUser);
        boolean isValid = jwtUtil.validateToken(token, testUser);
        assertTrue(isValid);
    }

    @Test
    void testValidateTokenWithWrongUser() {
        String token = jwtUtil.generateToken(testUser);
        
        User differentUser = new User();
        differentUser.setUsername("differentuser");
        differentUser.setPassword("password");
        
        boolean isValid = jwtUtil.validateToken(token, differentUser);
        assertFalse(isValid);
    }
}
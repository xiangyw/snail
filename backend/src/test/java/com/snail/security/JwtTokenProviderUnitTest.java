package com.snail.security;

import com.snail.entity.User;
import com.snail.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Method;
import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderUnitTest {

    private JwtTokenProvider jwtTokenProvider;
    private User testUser;
    private String testSecret;
    private int testExpiration;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        testUser = new User("testuser", "test@example.com", "password");
        testUser.setRole(UserRole.USER);
        
        testSecret = "mySecretKeyForSnailProjectThatShouldBeLongEnoughAndSecure";
        testExpiration = 3600; // 1 hour
        
        // Use reflection to set the values for testing
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", testSecret);
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpiration", testExpiration);
    }

    @Test
    void generateToken_ShouldCreateValidToken() {
        String token = jwtTokenProvider.generateToken(testUser);
        
        assertNotNull(token);
        assertFalse(token.isEmpty());
        
        // Verify the token can be parsed
        Claims claims = getClaimsFromToken(token);
        assertEquals(testUser.getUsername(), claims.getSubject());
    }

    @Test
    void getUsernameFromToken_ShouldReturnCorrectUsername() {
        String token = jwtTokenProvider.generateToken(testUser);
        
        String username = jwtTokenProvider.getUsernameFromToken(token);
        
        assertEquals(testUser.getUsername(), username);
    }

    @Test
    void getExpirationDateFromToken_ShouldReturnCorrectExpiration() {
        String token = jwtTokenProvider.generateToken(testUser);
        
        Date expiration = jwtTokenProvider.getExpirationDateFromToken(token);
        
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void validateToken_ShouldReturnTrue_ForValidToken() {
        String token = jwtTokenProvider.generateToken(testUser);
        
        boolean isValid = jwtTokenProvider.validateToken(token, testUser);
        
        assertTrue(isValid);
    }

    @Test
    void validateToken_ShouldReturnFalse_ForInvalidToken() {
        String token = jwtTokenProvider.generateToken(testUser);
        
        // Change the secret to invalidate the token
        String wrongSecret = "wrongSecretKeyThatIsDifferent";
        String wrongToken = Jwts.builder()
                .setSubject(testUser.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(Keys.hmacShaKeyFor(wrongSecret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
        
        boolean isValid = jwtTokenProvider.validateToken(wrongToken, testUser);
        
        assertFalse(isValid);
    }

    @Test
    void isTokenExpired_ShouldReturnTrue_ForExpiredToken() throws Exception {
        // Create an expired token manually
        String expiredToken = Jwts.builder()
                .setSubject(testUser.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis() - 2 * 60 * 60 * 1000)) // 2 hours ago
                .setExpiration(new Date(System.currentTimeMillis() - 1 * 60 * 60 * 1000)) // 1 hour ago
                .signWith(Keys.hmacShaKeyFor(testSecret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
        
        boolean isExpired = (Boolean) invokePrivateMethod("isTokenExpired", expiredToken);
        
        assertTrue(isExpired);
    }

    @Test
    void isTokenExpired_ShouldReturnFalse_ForValidToken() throws Exception {
        String validToken = jwtTokenProvider.generateToken(testUser);
        
        boolean isExpired = (Boolean) invokePrivateMethod("isTokenExpired", validToken);
        
        assertFalse(isExpired);
    }

    // Helper methods
    private Claims getClaimsFromToken(String token) {
        Key signingKey = Keys.hmacShaKeyFor(testSecret.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Object invokePrivateMethod(String methodName, Object... args) throws Exception {
        Method method = JwtTokenProvider.class.getDeclaredMethod(methodName, String.class);
        method.setAccessible(true);
        return method.invoke(jwtTokenProvider, args);
    }
}
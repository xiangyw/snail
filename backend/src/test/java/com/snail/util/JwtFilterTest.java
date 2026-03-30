package com.snail.util;

import com.snail.service.UserDetailsServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class JwtFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    private JwtFilter jwtFilter;
    private SecretKey secretKey;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtFilter = new JwtFilter();
        
        // Use reflection to inject dependencies
        org.springframework.test.util.ReflectionTestUtils.setField(jwtFilter, "jwtUtil", jwtUtil);
        org.springframework.test.util.ReflectionTestUtils.setField(jwtFilter, "userDetailsService", userDetailsService);
        
        // Create a secret key for testing
        secretKey = Keys.hmacShaKeyFor("snail-app-secret-key-for-jwt-token-generation-and-verification-purpose-only".getBytes());
    }

    @Test
    void testDoFilterInternalWithValidToken() throws Exception {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
        
        String username = "testuser";
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
        
        request.addHeader("Authorization", "Bearer " + token);
        
        // Mock JWT util behavior
        when(jwtUtil.extractUsername(eq(token))).thenReturn(username);
        when(jwtUtil.validateToken(eq(token), any(org.springframework.security.core.userdetails.UserDetails.class))).thenReturn(true);
        
        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);
        
        // Assert
        // Verify that the authentication was set in the security context
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals(username, authentication.getName());
    }

    @Test
    void testDoFilterInternalWithoutAuthHeader() throws Exception {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
        
        // No Authorization header
        
        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);
        
        // Assert
        // Verify that the filter chain continues without setting authentication
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
    }

    @Test
    void testDoFilterInternalWithInvalidToken() throws Exception {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
        
        String token = "invalid.token.here";
        request.addHeader("Authorization", "Bearer " + token);
        
        // Mock JWT util to return false for validation
        when(jwtUtil.extractUsername(eq(token))).thenReturn("testuser");
        when(jwtUtil.validateToken(eq(token), any(org.springframework.security.core.userdetails.UserDetails.class))).thenReturn(false);
        
        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);
        
        // Assert
        // Verify that authentication was not set in the security context
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
    }

    @Test
    void testDoFilterInternalWithMalformedAuthHeader() throws Exception {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
        
        request.addHeader("Authorization", "InvalidFormatToken");
        
        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);
        
        // Assert
        // Verify that the filter chain continues without processing the token
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
    }
}
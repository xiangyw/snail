package com.snail.service.impl;

import com.snail.dto.AuthRequest;
import com.snail.dto.AuthResponse;
import com.snail.entity.User;
import com.snail.repository.UserRepository;
import com.snail.security.JwtTokenProvider;
import com.snail.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();

        String token = tokenProvider.generateToken(user);

        return new AuthResponse(
                token,
                null, // Refresh token not implemented
                user.getId(),
                user.getUsername(),
                user.getRole(),
                (long) 3600L // 1 hour in seconds
        );
    }

    @Override
    public String refreshToken(String refreshToken) {
        // For simplicity, we're not implementing refresh tokens in this basic version
        // In a production system, you would validate the refresh token and issue a new access token
        return null;
    }

    @Override
    public void logout(String token) {
        // In a stateless JWT system, logout typically means removing the token from client-side storage
        // Server doesn't store JWT tokens, so there's nothing to invalidate server-side
        // However, you could implement a token blacklist if needed
    }
}
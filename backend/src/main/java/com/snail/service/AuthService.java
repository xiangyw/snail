package com.snail.service;

import com.snail.dto.AuthRequest;
import com.snail.dto.AuthResponse;

public interface AuthService {
    AuthResponse login(AuthRequest authRequest);
    String refreshToken(String refreshToken);
    void logout(String token);
}
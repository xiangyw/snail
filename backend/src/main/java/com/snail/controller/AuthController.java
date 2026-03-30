package com.snail.controller;

import com.snail.dto.AuthResponse;
import com.snail.dto.LoginRequest;
import com.snail.dto.RegisterRequest;
import com.snail.entity.User;
import com.snail.security.JwtTokenProvider;
import com.snail.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "认证API", description = "用户注册、登录等认证相关操作")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "新用户注册账户")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            User user = userService.registerUser(registerRequest);
            
            // Generate JWT token
            String token = tokenProvider.generateToken(user);
            
            AuthResponse response = new AuthResponse(
                token, 
                null, // Refresh token not implemented
                user.getId(),
                user.getUsername(),
                user.getRole(),
                (long) 3600 // 1 hour in seconds
            );
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "已注册用户登录系统")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = (User) authentication.getPrincipal();
            
            String token = tokenProvider.generateToken(user);
            
            AuthResponse response = new AuthResponse(
                token, 
                null, // Refresh token not implemented
                user.getId(),
                user.getUsername(),
                user.getRole(),
                (long) 3600 // 1 hour in seconds
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "用户登出系统")
    public ResponseEntity<?> logout() {
        // In a stateless JWT system, we typically just remove the token from client-side storage
        // Server-side doesn't maintain session state
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged out successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @Operation(summary = "获取当前用户信息", description = "获取当前已认证用户的信息")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(user);
    }

    @PutMapping("/profile")
    @Operation(summary = "更新用户资料", description = "更新当前用户的个人资料")
    public ResponseEntity<?> updateProfile(@RequestBody com.snail.dto.UserProfileDTO profileDTO, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        
        try {
            User updatedUser = userService.updateUserProfile(currentUser.getId(), profileDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}
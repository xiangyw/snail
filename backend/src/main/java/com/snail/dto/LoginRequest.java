package com.snail.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "用户登录请求")
public class LoginRequest {
    
    @Schema(description = "用户名或邮箱", example = "john_doe", required = true)
    private String username;
    
    @Schema(description = "密码", example = "password123", required = true)
    private String password;

    // Constructors
    public LoginRequest() {}

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
package com.snail.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "用户注册请求")
public class RegisterRequest {
    
    @Schema(description = "用户名", example = "john_doe", required = true)
    private String username;
    
    @Schema(description = "邮箱", example = "john@example.com", required = true)
    private String email;
    
    @Schema(description = "密码", example = "password123", required = true)
    private String password;
    
    @Schema(description = "姓氏", example = "John")
    private String firstName;
    
    @Schema(description = "名字", example = "Doe")
    private String lastName;

    // Constructors
    public RegisterRequest() {}

    public RegisterRequest(String username, String email, String password, String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
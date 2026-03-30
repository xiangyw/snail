package com.snail.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "用户资料DTO")
public class UserProfileDTO {
    
    @Schema(description = "姓氏", example = "John")
    private String firstName;
    
    @Schema(description = "名字", example = "Doe")
    private String lastName;

    // Constructors
    public UserProfileDTO() {}

    public UserProfileDTO(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Getters and Setters
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
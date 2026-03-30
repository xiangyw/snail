package com.snail.service;

import com.snail.dto.RegisterRequest;
import com.snail.dto.UserProfileDTO;
import com.snail.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User registerUser(RegisterRequest registerRequest);
    User findByUsername(String username);
    User findByEmail(String email);
    User findById(Long id);
    User updateUserProfile(Long userId, UserProfileDTO userProfileDTO);
    List<User> findAllUsers();
    void deleteUser(Long id);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
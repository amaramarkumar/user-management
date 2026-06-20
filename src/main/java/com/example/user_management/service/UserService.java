package com.example.user_management.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.user_management.dto.UserRequest;
import com.example.user_management.dto.UserResponse;
import com.example.user_management.entity.User;
import com.example.user_management.exception.DuplicateResourceException;
import com.example.user_management.exception.ResourceNotFoundException;
import com.example.user_management.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse create(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("email already in use: " + request.getEmail());
        }
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();
        return toResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    @Transactional
    public UserResponse update(Long id, UserRequest request) {
        User user = getOrThrow(id);
        if (!user.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("email already in use: " + request.getEmail());
        }
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        return toResponse(userRepository.save(user));
    }

    @Transactional
    public void delete(Long id) {
        User user = getOrThrow(id);
        userRepository.delete(user);
    }

    private User getOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found with id: " + id));
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}

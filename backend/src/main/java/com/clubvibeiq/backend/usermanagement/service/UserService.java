package com.clubvibeiq.backend.usermanagement.service;

import com.clubvibeiq.backend.usermanagement.dto.UserRequestDto;
import com.clubvibeiq.backend.usermanagement.dto.UserResponseDto;
import com.clubvibeiq.backend.usermanagement.entity.User;
import com.clubvibeiq.backend.usermanagement.mapper.UserMapper;
import com.clubvibeiq.backend.usermanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponseDto registerUser(UserRequestDto requestDto) {
        try {
            User user = userMapper.toEntity(requestDto);
            User savedUser = userRepository.save(user);
            return userMapper.toResponseDto(savedUser);
        } catch (Exception e) {
            log.error("Error registering user{}", e.getMessage());
            throw new RuntimeException("Failed to register user.");
        }
    }
}

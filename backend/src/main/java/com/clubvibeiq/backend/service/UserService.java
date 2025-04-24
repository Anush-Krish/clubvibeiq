package com.clubvibeiq.backend.service;

import com.clubvibeiq.backend.dto.UserRequestDto;
import com.clubvibeiq.backend.dto.UserResponseDto;
import com.clubvibeiq.backend.entity.User;
import com.clubvibeiq.backend.mapper.UserMapper;
import com.clubvibeiq.backend.repository.UserRepository;
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

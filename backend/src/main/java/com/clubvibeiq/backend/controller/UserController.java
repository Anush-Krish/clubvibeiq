package com.clubvibeiq.backend.controller;

import com.clubvibeiq.backend.dto.UserRequestDto;
import com.clubvibeiq.backend.dto.UserResponseDto;
import com.clubvibeiq.backend.service.UserService;
import com.clubvibeiq.backend.utils.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    public ResponseEntity<GenericResponse<UserResponseDto>> registerUser(@RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).
                body(GenericResponse.
                        success(userService.registerUser(userRequestDto)));
    }
}

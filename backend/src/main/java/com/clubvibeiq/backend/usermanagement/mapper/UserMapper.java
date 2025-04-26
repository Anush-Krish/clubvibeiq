package com.clubvibeiq.backend.usermanagement.mapper;

import com.clubvibeiq.backend.usermanagement.dto.UserRequestDto;
import com.clubvibeiq.backend.usermanagement.dto.UserResponseDto;
import com.clubvibeiq.backend.usermanagement.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    User toEntity(UserRequestDto requestDto);

    UserResponseDto toResponseDto(User user);
}

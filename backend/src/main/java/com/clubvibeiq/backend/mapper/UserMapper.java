package com.clubvibeiq.backend.mapper;

import com.clubvibeiq.backend.dto.UserRequestDto;
import com.clubvibeiq.backend.dto.UserResponseDto;
import com.clubvibeiq.backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    User toEntity(UserRequestDto requestDto);

    UserResponseDto toResponseDto(User user);
}

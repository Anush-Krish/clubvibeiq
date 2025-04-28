package com.clubvibeiq.backend.userpreference.mapper;

import com.clubvibeiq.backend.userpreference.dto.UserPreferenceDto;
import com.clubvibeiq.backend.userpreference.entity.UserPreference;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserPreferenceMapper {
    UserPreference toEntity(UserPreferenceDto preferenceDto);
}

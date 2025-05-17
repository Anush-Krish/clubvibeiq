package com.clubvibeiq.backend.preference.mapper;

import com.clubvibeiq.backend.preference.dto.UserPreferenceDto;
import com.clubvibeiq.backend.preference.entity.UserPreference;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserPreferenceMapper {
    UserPreference toEntity(UserPreferenceDto preferenceDto);
}

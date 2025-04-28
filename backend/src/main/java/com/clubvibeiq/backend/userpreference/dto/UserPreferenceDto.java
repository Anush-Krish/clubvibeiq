package com.clubvibeiq.backend.userpreference.dto;

import com.clubvibeiq.backend.utils.model.BaseDto;
import com.clubvibeiq.backend.utils.model.MusicLibrary;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserPreferenceDto extends BaseDto {
    private UUID preferenceId;

    private UUID userId;
    private UUID clubId;
    private Boolean isActive; //if the user is in club currently

    private MusicLibrary musicLibrary;
}

package com.clubvibeiq.backend.userpreference.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PreferenceResponseDto {
    private List<String> suggestedTracks;
    private String message;
}

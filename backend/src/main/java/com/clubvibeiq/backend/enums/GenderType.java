package com.clubvibeiq.backend.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GenderType {
    MALE("MALE"),
    FEMALE("FEMALE"),
    NON_BINARY("NON_BINARY");
    
    private final String displayName;
}

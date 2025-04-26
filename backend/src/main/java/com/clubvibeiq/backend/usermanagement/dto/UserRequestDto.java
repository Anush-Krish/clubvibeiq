package com.clubvibeiq.backend.usermanagement.dto;

import com.clubvibeiq.backend.enums.GenderType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {

    private String name;
    private String email;
    @NonNull
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private GenderType gender;
}

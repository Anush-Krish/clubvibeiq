package com.clubvibeiq.backend.preference.controller;

import com.clubvibeiq.backend.preference.dto.PreferenceResponseDto;
import com.clubvibeiq.backend.preference.service.CrowdPreferenceService;
import com.clubvibeiq.backend.utils.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequestMapping("/infer")
@RequiredArgsConstructor
public class CrowdPreferenceController {

    private final CrowdPreferenceService crowdPreferenceService;

    /**
     * fetch crowd preference by dj.
     *
     * @return -> Object of crowd preference.
     */
    @GetMapping("/{clubId}")
    public ResponseEntity<GenericResponse<PreferenceResponseDto>> fetchCrowdPreference(@PathVariable UUID clubId) {
        return ResponseEntity.status(HttpStatus.OK).
                body(GenericResponse.success(crowdPreferenceService.fetchCrowdPreferences(clubId)));
    }
}

package com.clubvibeiq.backend.preference.service;

import com.clubvibeiq.backend.preference.dto.PreferenceResponseDto;
import com.clubvibeiq.backend.preference.entity.UserPreference;
import com.clubvibeiq.backend.preference.repository.UserPreferenceRepository;
import com.clubvibeiq.backend.utils.ExteralApiUtil;
import com.clubvibeiq.backend.utils.model.MusicLibrary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrowdPreferenceService {
    private final ExteralApiUtil exteralApiUtil;
    private final UserPreferenceRepository userPreferenceRepository;
    // this method makes external api call to ml-engine
    //to fetch current crowd preference using the data
    public PreferenceResponseDto fetchCrowdPreferences(UUID clubId) {
        try {
            List<UserPreference> preferenceList = userPreferenceRepository.
                    findByClubIdAndIsActive(clubId, Boolean.TRUE);
            List<MusicLibrary> musicLibraryList = new ArrayList<>();

            preferenceList.forEach(userPreference ->
                    musicLibraryList.add(userPreference.getMusicLibrary()));

            //make external api call to fetch preference by sending musicLibraryList
            return exteralApiUtil.callMlInferenceApi(musicLibraryList);

        } catch (Exception e) {
            log.error("Error fetching crowd preference for clubId{}", clubId);
            throw new RuntimeException("Error fetching crowd preference");
        }
    }
}

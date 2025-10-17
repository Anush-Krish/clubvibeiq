package com.clubvibeiq.backend.utils;

import com.clubvibeiq.backend.preference.dto.PreferenceResponseDto;
import com.clubvibeiq.backend.utils.model.MusicLibrary;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExteralApiUtil {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;
    @Value("${ml.url}")
    private String mlUrl;

    public PreferenceResponseDto callMlInferenceApi(List<MusicLibrary> musicLibraryList) {
        String url = mlUrl + "/infer/enhanced";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<MusicLibrary>> request = new HttpEntity<>(musicLibraryList, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        try {
            return objectMapper.
                    readValue(response.getBody(), PreferenceResponseDto.class);
        } catch (JsonProcessingException e) {
            log.error("Error parsing ml inference response{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

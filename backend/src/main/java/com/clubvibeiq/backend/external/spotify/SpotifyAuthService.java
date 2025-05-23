package com.clubvibeiq.backend.external.spotify;

import com.clubvibeiq.backend.preference.service.UserPreferenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpotifyAuthService {

    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.client-secret}")
    private String clientSecret;

    @Value("${spotify.redirect-uri}")
    private String redirectUri;

    private final WebClient webClient;
    private final UserPreferenceService preferenceService;

    public String buildAuthorizationUrl() {
        try {
            String scopes = URLEncoder.encode(
                    "user-read-private user-top-read user-read-email playlist-read-private playlist-read-collaborative",
                    StandardCharsets.UTF_8
            );
            return "https://accounts.spotify.com/authorize?" +
                    "client_id=" + clientId +
                    "&response_type=code" +
                    "&redirect_uri=" + redirectUri +
                    "&scope=" + scopes;
        } catch (Exception e) {
            log.error("Failed to authorize via spotify{}", e.getMessage());
            throw new RuntimeException("Error in spotify authorization");
        }
    }

    public Mono<Map<String, Object>> exchangeCodeForAccessToken(String code) {
        String credentials = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("code", code);
        formData.add("redirect_uri", redirectUri);

        return webClient.post()
                .uri("https://accounts.spotify.com/api/token")
                .header("Authorization", "Basic " + credentials)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(formData)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException("Spotify API error: " +
                                        response.statusCode() + " - " + body))))
                .bodyToMono(Map.class)
                .flatMap(response -> {
                    String accessToken = (String) response.get("access_token");
                    if (accessToken != null) {
                        return preferenceService.fetchUserLibrary(accessToken)
                                .map(playlists -> {
                                    Map<String, Object> result = new HashMap<>();
                                    result.put("playlists", playlists);
                                    return result;
                                });
                    } else {
                        return Mono.error(new RuntimeException("Access token not found in response"));
                    }
                })
                .onErrorMap(e -> new RuntimeException("Error processing the token exchange or fetching playlist data: "
                        + e.getMessage()));
    }


}


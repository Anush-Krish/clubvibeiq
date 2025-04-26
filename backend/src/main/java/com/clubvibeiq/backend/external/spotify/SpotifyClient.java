package com.clubvibeiq.backend.external.spotify;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class SpotifyClient {

    private final WebClient webClient = WebClient.create();

    public String fetchUserPlaylists(String accessToken) {
        return webClient.get()
                .uri("https://api.spotify.com/v1/me/playlists")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}


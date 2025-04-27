package com.clubvibeiq.backend.external.spotify;


import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Component
public class SpotifyClient {

    private final WebClient webClient;

    public SpotifyClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.spotify.com/v1").build();
    }

    // Fetch the user's playlists from Spotify
    public Mono<String> fetchUserPlaylists(String accessToken) {
        return webClient.get()
                .uri("/me/playlists?offset=0&limit=20") // Use /me/playlists instead of hardcoded user ID
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> Mono.error(new RuntimeException("Error fetching playlists: " + response.statusCode())))
                .bodyToMono(String.class);
    }
}



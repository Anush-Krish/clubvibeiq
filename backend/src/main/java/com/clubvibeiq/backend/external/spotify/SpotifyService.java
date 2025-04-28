package com.clubvibeiq.backend.external.spotify;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;


@Service
public class SpotifyService {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    private final WebClient webClient;

    public SpotifyService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.spotify.com/v1").build();
    }

    // Fetch the user's playlists from Spotify
    public Mono<String> fetchUserPlaylists(String accessToken) {
        return webClient.get()
                .uri("/me/playlists?offset=0&limit=20")
                .header(AUTHORIZATION, BEARER + accessToken)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> Mono.error(new RuntimeException("Error fetching playlists: " +
                                response.statusCode())))
                .bodyToMono(String.class);
    }

    public Mono<String> fetchUserTopTracks(String accessToken) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/me/top/tracks")
                        .queryParam("limit", 20)
                        .build())
                .header(AUTHORIZATION, BEARER + accessToken)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> Mono.error(new RuntimeException("Error fetching top tracks: " +
                                response.statusCode())))
                .bodyToMono(String.class);
    }

    public Mono<List<String>> fetchPlaylistTracks(String accessToken, String playlistId, int limit) {
        return webClient.get()
                .uri("/playlists/" + playlistId + "/tracks?limit=" + limit)
                .header(AUTHORIZATION, BEARER + accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .handle((response, sink) -> {
                    List<String> trackNames = new ArrayList<>();
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode root = mapper.readTree(response);
                        JsonNode items = root.path("items");

                        if (items.isArray()) {
                            for (JsonNode item : items) {
                                JsonNode track = item.path("track");
                                String trackName = track.path("name").asText();
                                trackNames.add(trackName);
                            }
                        }
                    } catch (Exception e) {
                        sink.error(new RuntimeException("Failed to parse playlist tracks", e));
                        return;
                    }
                    sink.next(trackNames);
                });
    }


}



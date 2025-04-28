package com.clubvibeiq.backend.userpreference.service;

import com.clubvibeiq.backend.external.spotify.SpotifyService;
import com.clubvibeiq.backend.userpreference.entity.UserPreference;
import com.clubvibeiq.backend.userpreference.mapper.UserPreferenceMapper;
import com.clubvibeiq.backend.userpreference.repository.UserPreferenceRepository;
import com.clubvibeiq.backend.utils.model.MusicLibrary;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * this will be storing user music library,
 * fetched from spotify api
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserPreferenceService {
    private final SpotifyService spotifyService;
    private final UserPreferenceRepository userPreferenceRepository;


    /**
     * this method fetches playlistIds, topTracks , and 10 songs from each playlist.
     *
     * @param accessToken-> spotify access Token
     * @return -> top tracks, songs from playlist(10)
     */
    public Mono<Map<String, List<String>>> fetchUserLibrary(String accessToken) {
        Mono<String> playlistsMono = spotifyService.fetchUserPlaylists(accessToken);

        UserPreference preference = new UserPreference();
        preference.setIsActive(true);

        return playlistsMono
                .publishOn(Schedulers.boundedElastic())
                .flatMap(playlistsResponse -> {
                    ObjectMapper mapper = new ObjectMapper();
                    List<String> playlistSongs = new ArrayList<>();

                    try {
                        JsonNode playlistsRoot = mapper.readTree(playlistsResponse);
                        JsonNode playlistsItems = playlistsRoot.path("items");

                        if (playlistsItems.isArray()) {
                            List<Mono<List<String>>> songsMonos = new ArrayList<>();

                            for (JsonNode playlist : playlistsItems) {
                                String playlistId = playlist.path("id").asText();
                                songsMonos.add(spotifyService.fetchPlaylistTracks(accessToken, playlistId, 10));
                            }

                            return Flux.merge(songsMonos)
                                    .collectList()
                                    .flatMap(allSongsLists -> {
                                        for (List<String> songs : allSongsLists) {
                                            playlistSongs.addAll(songs);
                                        }

                                        MusicLibrary musicLibrary = new MusicLibrary();
                                        musicLibrary.setPlaylistSongs(playlistSongs);
                                        preference.setMusicLibrary(musicLibrary);

                                        // Save using JPA repo inside fromCallable
                                        return Mono.fromCallable(() -> userPreferenceRepository.save(preference))
                                                .subscribeOn(Schedulers.boundedElastic())
                                                .thenReturn(Collections.singletonMap("playlistSongs", playlistSongs));
                                    });
                        } else {
                            return Mono.fromCallable(() -> userPreferenceRepository.save(preference))
                                    .subscribeOn(Schedulers.boundedElastic())
                                    .thenReturn(Collections.singletonMap("playlistSongs", Collections.emptyList()));
                        }
                    } catch (Exception e) {
                        return Mono.error(new RuntimeException("Failed to parse Spotify playlists response", e));
                    }
                });
    }


}

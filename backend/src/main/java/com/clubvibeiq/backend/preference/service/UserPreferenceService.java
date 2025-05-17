package com.clubvibeiq.backend.preference.service;

import com.clubvibeiq.backend.external.spotify.SpotifyService;
import com.clubvibeiq.backend.preference.entity.UserPreference;
import com.clubvibeiq.backend.preference.repository.UserPreferenceRepository;
import com.clubvibeiq.backend.utils.model.MusicLibrary;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Instant;
import java.util.*;


/**
 * this will be storing user music library,
 * fetched from spotify api
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserPreferenceService {
    public static final String ITEMS = "items";
    private final SpotifyService spotifyService;
    private final UserPreferenceRepository userPreferenceRepository;


    /**
     * this method fetches playlistIds, topTracks , and 10 songs from each playlist.
     * top artists and their genres
     *
     * @param accessToken-> spotify access Token
     * @return -> top tracks, songs from playlist(10), top artist
     */
    public Mono<Map<String, Object>> fetchUserLibrary(String accessToken) {
        Mono<String> playlistsMono = spotifyService.fetchUserPlaylists(accessToken);
        Mono<String> topTracksMono = spotifyService.fetchUserTopTracks(accessToken);
        Mono<String> topArtistsMono = spotifyService.fetchUserTopArtists(accessToken);

        UserPreference preference = new UserPreference();
        preference.setIsActive(true);
        preference.setCreatedAt(Date.from(Instant.now()));
        preference.setUpdatedAt(Date.from(Instant.now()));

        return Mono.zip(playlistsMono, topTracksMono, topArtistsMono)
                .publishOn(Schedulers.boundedElastic())
                .flatMap(tuple -> {
                    String playlistsResponse = tuple.getT1();
                    String topTracksResponse = tuple.getT2();
                    String topArtistsResponse = tuple.getT3();

                    ObjectMapper mapper = new ObjectMapper();
                    List<String> playlistSongs = new ArrayList<>();
                    List<String> topTracks = new ArrayList<>();
                    List<String> topArtists = new ArrayList<>();
                    Map<String, List<String>> artistGenres = new HashMap<>();

                    try {
                        // Parse playlists
                        JsonNode playlistsRoot = mapper.readTree(playlistsResponse);
                        JsonNode playlistsItems = playlistsRoot.path(ITEMS);

                        List<Mono<List<String>>> songsMonos = new ArrayList<>();

                        if (playlistsItems.isArray()) {
                            for (JsonNode playlist : playlistsItems) {
                                String playlistId = playlist.path("id").asText();
                                songsMonos.add(spotifyService.fetchPlaylistTracks(accessToken, playlistId, 10));
                            }
                        }

                        // Parse top tracks
                        JsonNode topTracksRoot = mapper.readTree(topTracksResponse);
                        JsonNode topTracksItems = topTracksRoot.path(ITEMS);

                        if (topTracksItems.isArray()) {
                            for (JsonNode track : topTracksItems) {
                                String trackName = track.path("name").asText();
                                topTracks.add(trackName);
                            }
                        }

                        // Parse top artists and genres
                        JsonNode topArtistsRoot = mapper.readTree(topArtistsResponse);
                        JsonNode topArtistsItems = topArtistsRoot.path(ITEMS);

                        if (topArtistsItems.isArray()) {
                            for (JsonNode artist : topArtistsItems) {
                                String artistName = artist.path("name").asText();
                                topArtists.add(artistName);

                                // Get genres for the artist
                                JsonNode genresNode = artist.path("genres");
                                if (genresNode.isArray()) {
                                    List<String> genres = new ArrayList<>();
                                    for (JsonNode genreNode : genresNode) {
                                        genres.add(genreNode.asText());
                                    }
                                    artistGenres.put(artistName, genres); // Map artist name to genres
                                }
                            }
                        }

                        return mergeSongs(songsMonos, playlistSongs, topTracks, topArtists, artistGenres, preference);
                    } catch (Exception e) {
                        return Mono.error(new RuntimeException("Failed to parse Spotify responses", e));
                    }
                });
    }

    private Mono<Map<String, Object>> mergeSongs(List<Mono<List<String>>> songsMonos,
                                                 List<String> playlistSongs, List<String> topTracks,
                                                 List<String> topArtists, Map<String, List<String>> artistGenres,
                                                 UserPreference preference) {
        return Flux.merge(songsMonos)
                .collectList()
                .flatMap(allSongsLists -> {
                    for (List<String> songs : allSongsLists) {
                        playlistSongs.addAll(songs);
                    }

                    MusicLibrary musicLibrary = new MusicLibrary();
                    musicLibrary.setPlaylistSongs(playlistSongs);
                    musicLibrary.setTopTracks(topTracks);
                    musicLibrary.setTopArtists(topArtists);
                    musicLibrary.setArtistGenres(artistGenres); // Set genres for each artist
                    preference.setMusicLibrary(musicLibrary);

                    return Mono.fromCallable(() -> userPreferenceRepository.save(preference))
                            .subscribeOn(Schedulers.boundedElastic())
                            .thenReturn(Map.of(
                                    "playlistSongs", playlistSongs,
                                    "topTracks", topTracks,
                                    "topArtists", topArtists,
                                    "artistGenres", artistGenres
                            ));
                });
    }

}

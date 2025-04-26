package com.clubvibeiq.backend.utils.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MusicLibrary implements Serializable {

    private List<MusicTrack> topTracks;
    private List<MusicArtist> topArtists;
    private List<MusicTrack> recentlyPlayed;
    private List<String> genres;
    private List<MusicPlaylist> playlists;
    private List<String> language;
}
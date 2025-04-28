package com.clubvibeiq.backend.utils.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MusicLibrary implements Serializable {
    private List<String> topTracks;
    private List<String> playlistSongs;
    private List<String> topArtists;
    private Map<String, List<String>> artistGenres;
}
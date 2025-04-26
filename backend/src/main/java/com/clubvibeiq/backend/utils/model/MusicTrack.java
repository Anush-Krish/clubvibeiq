package com.clubvibeiq.backend.utils.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MusicTrack implements Serializable {
    private String trackId;
    private String name;
    private String album;
    private List<String> artists;
    private int durationMs;
    private String previewUrl;
}

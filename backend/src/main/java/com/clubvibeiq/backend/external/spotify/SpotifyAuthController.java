package com.clubvibeiq.backend.external.spotify;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
public class SpotifyAuthController {

    private final SpotifyAuthService spotifyAuthService;

    @GetMapping("/spotify/login")
    public String loginToSpotify() {
        return spotifyAuthService.buildAuthorizationUrl();
    }

    @GetMapping("/spotify/callback")
    public String spotifyCallback(@RequestParam String code) {
        return spotifyAuthService.exchangeCodeForAccessToken(code);
    }
}


package com.clubvibeiq.backend.external.spotify;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class SpotifyAuthController {

    private final SpotifyAuthService spotifyAuthService;

    @GetMapping("/spotify/login")
    public String loginToSpotify() {
        return spotifyAuthService.buildAuthorizationUrl();
    }

    @GetMapping("/spotify/callback")
    public Mono<String> spotifyCallback(@RequestParam String code) {
        return spotifyAuthService.exchangeCodeForAccessToken(code);
    }
}


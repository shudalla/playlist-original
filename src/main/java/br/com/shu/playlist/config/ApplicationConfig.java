package br.com.shu.playlist.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class ApplicationConfig {

    @Value("${openWeather.api-url}")
    private String openWeatherUrl;

    @Value("${openWeather.api-key}")
    private String openWeatherKey;

    @Value("${spotify.api-url}")
    private String spotifyUrl;

    @Value("${spotify.api-url-playlists}")
    private String spotifyUrlPlaylists;

    @Value("${spotify.api-url-access-token}")
    private String spotifyUrlAccessToken;

    @Value("${spotify.app-client-id}")
    private String spotifyClientId;

    @Value("${spotify.app-client-secret}")
    private String spotifyClientSecret;
}

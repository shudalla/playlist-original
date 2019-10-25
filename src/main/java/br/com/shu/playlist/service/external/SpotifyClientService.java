package br.com.shu.playlist.service.external;

import java.net.MalformedURLException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.shu.playlist.config.ApplicationConfig;
import br.com.shu.playlist.exception.ErrorSpotifyClientException;
import br.com.shu.playlist.response.external.AccessTokenSpotifyClientResponse;
import br.com.shu.playlist.response.external.PlaylistSpotifyClientResponse;
import br.com.shu.playlist.response.external.TracksSpotifyClientResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpotifyClientService {

    private final RestTemplate restTemplate;
    private final ApplicationConfig applicationConfig;

    public List<TracksSpotifyClientResponse> searchPlaylists(String category, String country) {
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + getSpotfyAccessToken());
            HttpEntity<Object> requestEntity = new HttpEntity<>(headers);

            log.info("[PlaylistsClientResponse request entity {}", requestEntity);

            StringBuilder params = new StringBuilder();
            params.append("limit=2");

            if (country != null) {
                params.append("&country=").append(country);
            }

            URI uri = UriComponentsBuilder.fromUriString(applicationConfig.getSpotifyUrlPlaylists())
                    .queryParam(params.toString())
                    .buildAndExpand(category)
                    .toUri();

            PlaylistSpotifyClientResponse playlistSpotifyClientResponse = restTemplate.exchange(
                    uri.toURL().toString(),
                    HttpMethod.GET,
                    requestEntity,
                    PlaylistSpotifyClientResponse.class).getBody();

            log.info("[TracksClientResponse request entity {}", requestEntity);

            List<TracksSpotifyClientResponse> tracksSelectedSpotifyClientResponse = buildTracksSpotityClient(
                    playlistSpotifyClientResponse, requestEntity);

            return tracksSelectedSpotifyClientResponse;

        } catch (RestClientException e) {
            log.error(e.getMessage());
            throw new ErrorSpotifyClientException(e.getMessage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new ErrorSpotifyClientException(e.getMessage());
        }
    }

    public String getSpotfyAccessToken() {
        log.info("[Getting Spotify Token ]");

        StringBuilder bodyLoginSpotify = new StringBuilder();
        bodyLoginSpotify.append("grant_type=").append("client_credentials");

        String basicAuth = getAuthenticationBase64(applicationConfig.getSpotifyClientId(),
                applicationConfig.getSpotifyClientSecret());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + basicAuth);
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<Object> requestTokenSpotify = new HttpEntity<>(bodyLoginSpotify.toString(), headers);
        AccessTokenSpotifyClientResponse responseToken = restTemplate
                .postForEntity(applicationConfig.getSpotifyUrlAccessToken(), requestTokenSpotify,
                        AccessTokenSpotifyClientResponse.class)
                .getBody();

        return responseToken.getAccessToken();
    }

    public String getAuthenticationBase64(String tokenA, String tokenB) {
        return Base64.getEncoder().encodeToString((tokenA + ":" + tokenB).getBytes(StandardCharsets.UTF_8));
    }

    public List<TracksSpotifyClientResponse> buildTracksSpotityClient(
            PlaylistSpotifyClientResponse playlistSpotifyClientResponse,
            HttpEntity<Object> requestEntity) {
        List<TracksSpotifyClientResponse> tracksSpotifyClientResponse = new ArrayList<>();

        if (!playlistSpotifyClientResponse.getPlaylists().getItems().isEmpty()) {
            playlistSpotifyClientResponse.getPlaylists().getItems().forEach(item -> {

                TracksSpotifyClientResponse track = restTemplate.exchange(
                        item.getTracks().getHref(),
                        HttpMethod.GET,
                        requestEntity,
                        TracksSpotifyClientResponse.class).getBody();

                tracksSpotifyClientResponse.add(track);
            });
        }
        return tracksSpotifyClientResponse;

    }

}
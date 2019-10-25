package br.com.shu.playlist.service.external;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.shu.playlist.config.ApplicationConfig;
import br.com.shu.playlist.exception.ErrorOpenWeatherClientException;
import br.com.shu.playlist.response.external.WeatherClientResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherClientService {

    private final RestTemplate restTemplate;
    private final ApplicationConfig applicationConfig;

    public WeatherClientResponse searchByCityName(String cityName) {
        try {

            URI uri = UriComponentsBuilder.fromUriString(applicationConfig.getOpenWeatherUrl())
                    .queryParam("q", cityName)
                    .queryParam("appid", applicationConfig.getOpenWeatherKey())
                    .queryParam("units", "metric")
                    .buildAndExpand(applicationConfig.getOpenWeatherUrl())
                    .toUri();

            WeatherClientResponse response = new WeatherClientResponse();
            response = restTemplate.getForEntity(uri, WeatherClientResponse.class).getBody();

            return response;
        } catch (HttpClientErrorException e) {
            validateResponse(e);
            return null;
        }
    }

    public WeatherClientResponse searchByLatitudeAndLongitude(Double latitude, Double longitude) {
        try {

            URI uri = UriComponentsBuilder.fromUriString(applicationConfig.getOpenWeatherUrl())
                    .queryParam("lat", latitude)
                    .queryParam("lon", longitude)
                    .queryParam("appid", applicationConfig.getOpenWeatherKey())
                    .queryParam("units", "metric")
                    .buildAndExpand(applicationConfig.getOpenWeatherUrl())
                    .toUri();

            WeatherClientResponse response = restTemplate.getForEntity(uri, WeatherClientResponse.class).getBody();
            return response;
        } catch (HttpClientErrorException e) {
            validateResponse(e);
            return null;
        }
    }

    private void validateResponse(HttpClientErrorException e) {
        log.error(e.getMessage());
        if (e.getRawStatusCode() != HttpStatus.NOT_FOUND.value()) {
            throw new ErrorOpenWeatherClientException(e.getMessage());
        }
    }
}

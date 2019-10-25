package br.com.shu.playlist.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.net.URI;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.shu.playlist.config.ApplicationConfig;
import br.com.shu.playlist.response.external.CoordWeatherClientResponse;
import br.com.shu.playlist.response.external.MainWeatherClientResponse;
import br.com.shu.playlist.response.external.SysWeatherClientResponse;
import br.com.shu.playlist.response.external.WeatherClientResponse;
import br.com.shu.playlist.service.external.WeatherClientService;

@RunWith(MockitoJUnitRunner.Silent.class)
public class WeatherClientServiceTest {

    @Mock
    private PlaylistService playlistService;

    @Mock
    private ApplicationConfig applicationConfig;

    @InjectMocks
    private WeatherClientService weatherClientService;

    @Mock
    private RestTemplate restTemplate;

    WeatherClientResponse weatherClientResponse;

    WeatherClientResponse weatherResponse;

    private String cityName;

    private Double latitude;

    private Double longitude;

    private final static String WEATHER_API_KEY = "b9dd2b170c010a3288f8e0eda6eb6284";

    private final static String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather";

    @Before
    public void setup() {

        when(applicationConfig.getOpenWeatherKey()).thenReturn(WEATHER_API_KEY);

        when(applicationConfig.getOpenWeatherUrl()).thenReturn(WEATHER_API_URL);
    }

    @Test
    public void shouldFindByNameACityReturnSuccessfully() {
        givenCityName();
        givenWeatherAPIReturnSucess();
        whenCallSearchByCityName();
        thenExpectNotNullResult();
    }

    @Test
    public void shouldFindByLatLongCityReturnSuccessfully() {
        givenLatitudeAndLongitudeValid();
        givenWeatherAPILatLongReturnSucess();
        whenCallSearchByLatLong();
        thenExpectNotNullResult();
    }

    /*
     * given methods
     */

    private void givenCityName() {
        cityName = "Campinas";
    }

    private void givenInvalidCityName() {
        cityName = "Campinasx";
    }

    private void givenLatitudeAndLongitudeValid() {
        latitude = 10.0;
        longitude = 10.0;
    }

    public void givenWeatherAPIReturnSucess() {

        URI uri = UriComponentsBuilder.fromUriString(applicationConfig.getOpenWeatherUrl())
                .queryParam("q", cityName)
                .queryParam("appid", applicationConfig.getOpenWeatherKey())
                .queryParam("units", "metric")
                .buildAndExpand(applicationConfig.getOpenWeatherUrl())
                .toUri();

        when(restTemplate.getForEntity(uri,
                WeatherClientResponse.class))
                        .thenReturn(new ResponseEntity<>(WeatherClientResponse.builder()
                                .main(MainWeatherClientResponse.builder().build())
                                .coord(CoordWeatherClientResponse.builder().build())
                                .name(cityName)
                                .sys(SysWeatherClientResponse.builder().build())
                                .build(), HttpStatus.OK));

    }

    public void givenWeatherAPILatLongReturnSucess() {

        URI uri = UriComponentsBuilder.fromUriString(applicationConfig.getOpenWeatherUrl())
                .queryParam("lat", latitude)
                .queryParam("lon", longitude)
                .queryParam("appid", applicationConfig.getOpenWeatherKey())
                .queryParam("units", "metric")
                .buildAndExpand(applicationConfig.getOpenWeatherUrl())
                .toUri();

        when(restTemplate.getForEntity(uri,
                WeatherClientResponse.class))
                        .thenReturn(new ResponseEntity<>(WeatherClientResponse.builder()
                                .main(MainWeatherClientResponse.builder().build())
                                .coord(CoordWeatherClientResponse.builder()
                                        .lat(latitude)
                                        .lon(longitude).build())
                                .sys(SysWeatherClientResponse.builder().build())
                                .build(), HttpStatus.OK));

    }

    /*
     * when methods
     */

    private void whenCallSearchByCityName() {
        weatherClientResponse = weatherClientService.searchByCityName(cityName);
    }

    private void whenCallSearchByLatLong() {
        weatherClientResponse = weatherClientService.searchByLatitudeAndLongitude(latitude, longitude);
    }

    /*
     * then methods
     */

    private void thenExpectNotNullResult() {
        assertThat(weatherClientResponse).isNotNull();
    }

}

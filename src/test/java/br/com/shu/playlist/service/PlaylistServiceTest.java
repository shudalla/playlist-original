package br.com.shu.playlist.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.shu.enumeration.CategoryPlaylisSpotifyEnum;
import br.com.shu.playlist.controller.PlaylistController;
import br.com.shu.playlist.response.PlaylistResponse;
import br.com.shu.playlist.response.Track;
import br.com.shu.playlist.response.TrackName;
import br.com.shu.playlist.response.external.CoordWeatherClientResponse;
import br.com.shu.playlist.response.external.ItemTrackSpotifyClientResponse;
import br.com.shu.playlist.response.external.MainWeatherClientResponse;
import br.com.shu.playlist.response.external.SysWeatherClientResponse;
import br.com.shu.playlist.response.external.TrackSpotifyClientResponse;
import br.com.shu.playlist.response.external.TracksSpotifyClientResponse;
import br.com.shu.playlist.response.external.WeatherClientResponse;
import br.com.shu.playlist.service.external.SpotifyClientService;
import br.com.shu.playlist.service.external.WeatherClientService;

@RunWith(MockitoJUnitRunner.Silent.class)
public class PlaylistServiceTest {

    @InjectMocks
    private PlaylistService playlistService;

    @Mock
    private PlaylistController playlistController;

    @Mock
    private WeatherClientService weatherClientService;

    @Mock
    private SpotifyClientService spotifyClientService;

    private Double temperature;

    private PlaylistResponse playlistResponse;

    private PlaylistResponse playlistResponseBuilt;

    private String cityName;

    private String countryCode;

    private Double latitude;

    private Double longitude;

    private String category;

    List<TracksSpotifyClientResponse> tracksSpotifyClientResponse;

    @Test
    public void shouldFindByNameAPartyPlaylistReturnSuccessfully() {
        givenPartyTemperatura();
        givenCityName();
        givenCountryCode();
        givenPartyCategory();
        givenWeatherClientServiceReturnValue();
        givenASpotifyClientServiceReturnValue();
        givenBuiltlaylistResponseReturnValue();
        whenCallFindPlaylists();
        thenExpectPlaylistBuilt();
        thenExpectNotNullResult();
        thenExpectWeatherClientServiceCall();
    }

    @Test
    public void shouldFindByNameAPopPlaylistReturnSuccessfully() {
        givenPopTemperatura();
        givenCityName();
        givenCountryCode();
        givenPopCategory();
        givenWeatherClientServiceReturnValue();
        givenASpotifyClientServiceReturnValue();
        givenBuiltlaylistResponseReturnValue();
        whenCallFindPlaylists();
        thenExpectPlaylistBuilt();
        thenExpectNotNullResult();
        thenExpectWeatherClientServiceCall();
    }

    @Test
    public void shouldFindByNameARockPlaylistReturnSuccessfully() {
        givenRockTemperatura();
        givenCityName();
        givenCountryCode();
        givenRockCategory();
        givenWeatherClientServiceReturnValue();
        givenASpotifyClientServiceReturnValue();
        givenBuiltlaylistResponseReturnValue();
        whenCallFindPlaylists();
        thenExpectPlaylistBuilt();
        thenExpectNotNullResult();
        thenExpectWeatherClientServiceCall();
    }

    @Test
    public void shouldFindByNameAClassicalPlaylistReturnSuccessfully() {
        givenClassicalTemperatura();
        givenCityName();
        givenCountryCode();
        givenClassicalCategory();
        givenWeatherClientServiceReturnValue();
        givenASpotifyClientServiceReturnValue();
        givenBuiltlaylistResponseReturnValue();
        whenCallFindPlaylists();
        thenExpectPlaylistBuilt();
        thenExpectNotNullResult();
        thenExpectWeatherClientServiceCall();
    }

    @Test
    public void shouldFindByNameAndInvalidCountryAClassicalPlaylistReturnSuccessfully() {
        givenClassicalTemperatura();
        givenCityName();
        givenCountryCodeNotAllowed();
        givenClassicalCategory();
        givenWeatherClientServiceReturnValue();
        givenASpotifyClientServiceReturnValue();
        givenBuiltlaylistResponseReturnValue();
        whenCallFindPlaylists();
        thenExpectPlaylistBuilt();
        thenExpectNotNullResult();
        thenExpectWeatherClientServiceCall();
    }

    @Test
    public void shouldnotFindByNameAnyPlaylistReturnEmpt() {
        givenInvalidCityName();
        givenWeatherClientReturnNull();
        whenCallFindPlaylists();
        thenExpectNullResult();
        thenExpectWeatherClientServiceCall();
    }

    @Test
    public void shouldFindByLatAndLonAPartyPlaylistReturnSuccessfully() {
        givenPartyTemperatura();
        givenLatitudeAndLongitudeValid();
        givenPartyCategory();
        givenWeatherClientServiceByLatAndLonReturnValue();
        givenASpotifyClientServiceReturnValue();
        givenBuiltlaylistResponseReturnValue();
        whenCallFindPlaylists();
        thenExpectPlaylistBuilt();
        thenExpectNotNullResult();
        thenExpectWeatherClientServiceByLatLonCall();
    }

    /*
     * given methods
     */

    private void givenPartyTemperatura() {
        temperature = 31.0;
    }

    private void givenPopTemperatura() {
        temperature = 25.0;
    }

    private void givenRockTemperatura() {
        temperature = 12.0;
    }

    private void givenClassicalTemperatura() {
        temperature = 9.0;
    }

    private void givenPartyCategory() {
        category = CategoryPlaylisSpotifyEnum.PARTY.name;
    }

    private void givenPopCategory() {
        category = CategoryPlaylisSpotifyEnum.POP.name;
    }

    private void givenRockCategory() {
        category = CategoryPlaylisSpotifyEnum.ROCK.name;
    }

    private void givenClassicalCategory() {
        category = CategoryPlaylisSpotifyEnum.CLASSICAL.name;
    }

    private void givenCityName() {
        cityName = "Campinas";
    }

    private void givenCountryCode() {
        countryCode = "BR";
    }

    private void givenInvalidCityName() {
        cityName = "Campinasx";
    }

    private void givenCountryCodeNotAllowed() {
        cityName = "NG";
    }

    private void givenLatitudeAndLongitudeValid() {
        latitude = 10.0;
        longitude = 10.0;
    }

    public void givenWeatherClientServiceReturnValue() {
        WeatherClientResponse weatherClientResponse = WeatherClientResponse.builder()
                .main(MainWeatherClientResponse.builder()
                        .temp(temperature)
                        .tempMax(temperature + 1)
                        .tempMin(temperature - 1)
                        .build())
                .coord(CoordWeatherClientResponse.builder()
                        .lat(latitude)
                        .lon(longitude)
                        .build())
                .name(cityName)
                .sys(SysWeatherClientResponse.builder()
                        .country(countryCode)
                        .build())
                .build();

        doReturn(weatherClientResponse).when(weatherClientService).searchByCityName(anyString());
    }

    public void givenWeatherClientReturnNull() {
        doReturn(null).when(weatherClientService).searchByCityName(anyString());
    }

    public void givenWeatherClientServiceByLatAndLonReturnValue() {
        WeatherClientResponse weatherClientResponse = WeatherClientResponse.builder()
                .main(MainWeatherClientResponse.builder()
                        .temp(temperature)
                        .tempMax(temperature + 1)
                        .tempMin(temperature - 1)
                        .build())
                .coord(CoordWeatherClientResponse.builder()
                        .lat(latitude)
                        .lon(longitude)
                        .build())
                .name(cityName)
                .sys(SysWeatherClientResponse.builder()
                        .country(countryCode)
                        .build())
                .build();

        doReturn(weatherClientResponse).when(weatherClientService).searchByLatitudeAndLongitude(any(),
                any());
    }

    public void givenASpotifyClientServiceReturnValue() {

        tracksSpotifyClientResponse = List.of(TracksSpotifyClientResponse.builder()
                .items(List.of(ItemTrackSpotifyClientResponse.builder()
                        .track(TrackSpotifyClientResponse.builder()
                                .name("MUSIC")
                                .build())
                        .build()))
                .build());

        doReturn(tracksSpotifyClientResponse).when(spotifyClientService).searchPlaylists(anyString(), anyString());

    }

    public void givenBuiltlaylistResponseReturnValue() {

        playlistResponseBuilt = PlaylistResponse.builder()
                .city(cityName)
                .temperature(temperature)
                .tracks(new ArrayList<>())
                .build();

        // percorrendo a lista de playlists com tracks
        tracksSpotifyClientResponse
                .forEach(trackSelected -> {

                    Track track = Track.builder()
                            .playlistCategory(category)
                            .trackNames(new ArrayList<>())
                            .build();

                    // preenchendo as tracks em cada playlist
                    trackSelected.getItems()
                            .forEach(item -> {
                                track.getTrackNames().add(TrackName.builder()
                                        .name(item.getTrack().getName())
                                        .build());
                            });

                    playlistResponseBuilt.getTracks().add(track);
                });

    }

    /*
     * when methods
     */

    private void whenCallFindPlaylists() {
        playlistResponse = playlistService.findPlaylists(cityName, latitude, longitude);
    }

    /*
     * then methods
     */

    private void thenExpectPlaylistBuilt() {
        assertThat(playlistResponse).isNotNull();
        assertThat(playlistResponseBuilt).isNotNull();
    }

    private void thenExpectNotNullResult() {
        assertThat(playlistResponse).isNotNull();
    }

    private void thenExpectNullResult() {
        assertThat(playlistResponse).isNull();
    }

    private void thenExpectWeatherClientServiceCall() {
        verify(weatherClientService, times(1)).searchByCityName(anyString());
    }

    private void thenExpectWeatherClientServiceByLatLonCall() {
        verify(weatherClientService, times(1)).searchByLatitudeAndLongitude(any(), any());
    }

}

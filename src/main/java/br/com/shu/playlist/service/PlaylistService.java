package br.com.shu.playlist.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import br.com.shu.enumeration.CategoryPlaylisSpotifyEnum;
import br.com.shu.enumeration.CitySpotifyEnum;
import br.com.shu.playlist.response.PlaylistResponse;
import br.com.shu.playlist.response.Track;
import br.com.shu.playlist.response.TrackName;
import br.com.shu.playlist.response.external.TracksSpotifyClientResponse;
import br.com.shu.playlist.response.external.WeatherClientResponse;
import br.com.shu.playlist.service.external.SpotifyClientService;
import br.com.shu.playlist.service.external.WeatherClientService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final WeatherClientService weatherClientService;
    private final SpotifyClientService spotifyClientService;

    public PlaylistResponse findPlaylists(String cityName, Double latitude, Double longitude) {

        List<TracksSpotifyClientResponse> tracksSpotifyClientResponse = new ArrayList<>();
        WeatherClientResponse weatherClientResponse = new WeatherClientResponse();

        if (!StringUtils.isBlank(cityName)) {
            weatherClientResponse = weatherClientService.searchByCityName(cityName);
        } else {
            weatherClientResponse = weatherClientService.searchByLatitudeAndLongitude(latitude, longitude);
        }

        if (weatherClientResponse != null) {
            if (!CitySpotifyEnum.isSpotifyAvailable(weatherClientResponse.getSys().getCountry())) {
                weatherClientResponse.getSys().setCountry(null);
            }

            String selectedCategory = "";

            if (weatherClientResponse.getMain().getTemp() > 30) {
                selectedCategory = CategoryPlaylisSpotifyEnum.PARTY.name;

            } else if (weatherClientResponse.getMain().getTemp() <= 30
                    && weatherClientResponse.getMain().getTemp() >= 15) {
                selectedCategory = CategoryPlaylisSpotifyEnum.POP.name;

            } else if (weatherClientResponse.getMain().getTemp() <= 14
                    && weatherClientResponse.getMain().getTemp() >= 10) {
                selectedCategory = CategoryPlaylisSpotifyEnum.ROCK.name;

            } else {
                selectedCategory = CategoryPlaylisSpotifyEnum.CLASSICAL.name;
            }

            tracksSpotifyClientResponse = spotifyClientService.searchPlaylists(selectedCategory,
                    weatherClientResponse
                            .getSys()
                            .getCountry());

            return buildTracks(tracksSpotifyClientResponse, selectedCategory, weatherClientResponse.getName(),
                    weatherClientResponse.getMain().getTemp());
        }
        return null;
    }

    private PlaylistResponse buildTracks(List<TracksSpotifyClientResponse> tracksSpotifyClientResponse,
            String category, String cityName, Double temperature) {

        PlaylistResponse playlistResponse = PlaylistResponse.builder()
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

                    playlistResponse.getTracks().add(track);
                });

        return playlistResponse;
    }
}

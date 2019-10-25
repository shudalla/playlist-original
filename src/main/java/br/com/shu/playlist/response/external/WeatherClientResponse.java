package br.com.shu.playlist.response.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherClientResponse {

    private String name;

    private CoordWeatherClientResponse coord;

    private MainWeatherClientResponse main;

    private SysWeatherClientResponse sys;

}

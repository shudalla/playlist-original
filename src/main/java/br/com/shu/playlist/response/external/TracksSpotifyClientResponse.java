package br.com.shu.playlist.response.external;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TracksSpotifyClientResponse {

    private List<ItemTrackSpotifyClientResponse> items;
}

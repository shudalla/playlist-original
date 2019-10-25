package br.com.shu.playlist.response.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemPlaylistSpotifyClientResponse {

    private String id;
    private String name;
    private HRefTracksSpotifyClientResponse tracks;

}

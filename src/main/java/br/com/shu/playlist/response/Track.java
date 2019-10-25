package br.com.shu.playlist.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Track {
    private String playlistCategory;
    private List<TrackName> trackNames;
}

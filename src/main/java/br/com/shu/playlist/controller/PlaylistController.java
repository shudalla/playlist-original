package br.com.shu.playlist.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.shu.playlist.response.PlaylistResponse;
import br.com.shu.playlist.service.PlaylistService;
import br.com.shu.playlist.validator.ParamsValidator;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<PlaylistResponse> findPlaylists(
            @RequestParam(required = false, name = "cityName") String cityName,
            @RequestParam(required = false, name = "latitude") Double latidude,
            @RequestParam(required = false, name = "longitude") Double longitude) {
        ParamsValidator.validate(cityName, latidude, longitude);
        return ResponseEntity.ok()
                .body(playlistService.findPlaylists(cityName, latidude, longitude));
    }

}

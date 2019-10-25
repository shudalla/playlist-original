package br.com.shu.playlist.exception;

public class ErrorSpotifyClientException extends RuntimeException {

    private static final long serialVersionUID = -854785865810559120L;

    public ErrorSpotifyClientException(String msg) {
        super(msg);
    }

}

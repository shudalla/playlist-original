package br.com.shu.playlist.exception;

public class ErrorOpenWeatherClientException extends RuntimeException {

    private static final long serialVersionUID = -854785865810559120L;

    public ErrorOpenWeatherClientException(String msg) {
        super(msg);
    }

}

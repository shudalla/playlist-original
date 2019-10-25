package br.com.shu.playlist.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1527545976609832638L;

    private final HttpStatus status;

    public NotFoundException() {
        this.status = HttpStatus.NOT_FOUND;
    }

    public HttpStatus getStatus() {
        return status;
    }

}

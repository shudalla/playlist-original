package br.com.shu.playlist.exception;

import java.util.List;

import org.springframework.http.HttpStatus;

public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = -9024693929177149671L;

    private final HttpStatus status;
    private final List<MessageError> errors;

    public BadRequestException(MessageError error) {
        this.status = HttpStatus.BAD_REQUEST;
        this.errors = List.of(error);
    }

    public BadRequestException(List<MessageError> errors) {
        this.status = HttpStatus.BAD_REQUEST;
        this.errors = errors;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public List<MessageError> getErrors() {
        return errors;
    }

}

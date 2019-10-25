package br.com.shu.playlist.controller.advice;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import br.com.shu.playlist.exception.BadRequestException;
import br.com.shu.playlist.exception.ForbiddenException;
import br.com.shu.playlist.exception.MessageError;
import br.com.shu.playlist.exception.Messages;
import br.com.shu.playlist.exception.NotFoundException;
import br.com.shu.playlist.exception.TooManyRequestsException;
import br.com.shu.playlist.exception.UnauthorizedException;
import br.com.shu.playlist.exception.UnprocessableEntityException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<MessageError>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException methodArgumentNotValidException) {
        List<MessageError> messageErrors = Optional.ofNullable(methodArgumentNotValidException)
                .filter(argumentNotValidException -> !ObjectUtils.isEmpty(argumentNotValidException.getBindingResult()))
                .map(MethodArgumentNotValidException::getBindingResult)
                .filter(bindingResult -> !ObjectUtils.isEmpty(bindingResult.getAllErrors()))
                .map(BindingResult::getAllErrors)
                .stream()
                .flatMap(Collection::stream)
                .filter(objectError -> !ObjectUtils.isEmpty(objectError))
                .map(o -> new MessageError(o.getDefaultMessage(), ((FieldError) o).getField()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(messageErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<List<MessageError>> handleBindException(BindException bindException) {
        List<MessageError> messageErrors = Optional.ofNullable(bindException)
                .filter(bind -> !ObjectUtils.isEmpty(bind.getBindingResult()))
                .map(BindException::getBindingResult)
                .filter(bindingResult -> !ObjectUtils.isEmpty(bindingResult.getAllErrors()))
                .map(BindingResult::getAllErrors)
                .stream()
                .flatMap(Collection::stream)
                .filter(objectError -> !ObjectUtils.isEmpty(objectError))
                .map(o -> new MessageError(Messages.INVALID_FORMAT_ERROR, ((FieldError) o).getField()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(messageErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<List<MessageError>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException httpMessageNotReadableException) {
        List<MessageError> errors = Arrays.asList(new MessageError(Messages.INVALID_BODY_ERROR));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<List<MessageError>> handleBadRequestException(
            BadRequestException badRequestException) {
        return new ResponseEntity<>(badRequestException.getErrors(), badRequestException.getStatus());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Void> handleNotFoundException(NotFoundException notFoundException) {
        return new ResponseEntity<>(notFoundException.getStatus());
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<MessageError> handleUnprocessableEntityException(UnprocessableEntityException unpEx) {
        return new ResponseEntity<>(new MessageError(unpEx.getCode(), unpEx.getAttribute()), unpEx.getStatus());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Void> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException) {
        return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Void> handleHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException httpMediaTypeNotSupportedException) {
        return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<List<MessageError>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException methodArgumentTypeMismatchException) {
        return ResponseEntity.badRequest().body(List
                .of(new MessageError(Messages.INVALID_FORMAT_ERROR, methodArgumentTypeMismatchException.getName())));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageError> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new MessageError(Messages.DEFAULT_INTERNAL_SERVER_ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Void> handleUnauthorizedException(UnauthorizedException unauthorizedException) {
        log.error(unauthorizedException.getMsg(), unauthorizedException);
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Void> handleForbiddenException(ForbiddenException forbiddenException) {
        log.error(forbiddenException.getMsg(), forbiddenException);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(TooManyRequestsException.class)
    public ResponseEntity<Void> handleTooManyRequestsException(TooManyRequestsException tooManyRequestsException) {
        log.error(tooManyRequestsException.getMsg(), tooManyRequestsException);
        return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
    }

}

package com.csye6225.webapp.exceptions;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Collections;


@ControllerAdvice
public class GlobalExceptionHandler {

    private final HttpHeaders headers;
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler() {
        this.headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().mustRevalidate());
        headers.setPragma("no-cache");
        headers.add("X-Content-Type-Options", "nosniff");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleMethodNotAllowedException() {
        logger.error("Wrong Method Given");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).headers(headers).build();
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NoResourceFoundException ex) {
        logger.error("No Resource Found Exception: "+ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(headers).build();
    }

    @ExceptionHandler(UserNotCreatedException.class)
    public ResponseEntity<Object> handleUserNotCreatedException(UserNotCreatedException e) {
        logger.error("UserNotCreatedException: " + e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(Collections.singletonMap("error", e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException e) {
        logger.error("UserNotFoundException: " + e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).headers(headers).body(Collections.singletonMap("error", e.getMessage()));
    }

    @ExceptionHandler(UserNotUpdatedException.class)
    public ResponseEntity<Object> handleNotUpdatedException(UserNotUpdatedException e) {
        logger.error("handleNotUpdatedException: " + e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(Collections.singletonMap("error", e.getMessage()));
    }

    @ExceptionHandler(UserNotVerifiedException.class)
    public ResponseEntity<Object> handleNotVerifiedException(UserNotVerifiedException e) {
        logger.error("handleNotVerifiedException: " + e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(headers).body(Collections.singletonMap("verificationStatus", e.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleWrongRequestException(HttpMessageNotReadableException e) {
        logger.error("handleWrongRequestException: " + e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(Collections.singletonMap("error", "Wrong fields in request"));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Object> handleWrongHeadersException(MissingRequestHeaderException e) {
        logger.error("handleWrongHeadersException: " + e);
        if (e.getHeaderName().equals("authorization")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).headers(headers).body(Collections.singletonMap("error", "Auth is empty"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(Collections.singletonMap("error", "Headers is missing"));
        }
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<String> handleMediaTypeException(HttpMediaTypeNotSupportedException e) {
        logger.error("Exception e" + e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        logger.error("Exception e" + e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).build();
    }
}

package com.csye6225.webapp.exceptions;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;


@ControllerAdvice
public class GlobalExceptionHandler {

   private final HttpHeaders headers;
   public GlobalExceptionHandler() {
       this.headers = new HttpHeaders();
       headers.setCacheControl(CacheControl.noCache().mustRevalidate());
       headers.setPragma("no-cache");
       headers.add("X-Content-Type-Options", "nosniff");
   }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleMethodNotAllowedException() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).headers(headers).build();
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NoResourceFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(headers).build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).headers(headers).build();
    }
}

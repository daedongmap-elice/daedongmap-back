package com.daedongmap.daedongmap.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<String> customException(CustomException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getErrorCode().getMessage());
    }
}

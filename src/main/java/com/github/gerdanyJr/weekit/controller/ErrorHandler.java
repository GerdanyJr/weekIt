package com.github.gerdanyJr.weekit.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.github.gerdanyJr.weekit.model.exceptions.ConflictException;
import com.github.gerdanyJr.weekit.model.res.ErrorResponse;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> conflictExceptionHandler(ConflictException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        e.getMessage(),
                        HttpStatus.CONFLICT,
                        HttpStatus.CONFLICT.value(),
                        LocalDateTime.now()));
    }

}

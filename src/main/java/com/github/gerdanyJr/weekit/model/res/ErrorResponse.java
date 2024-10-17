package com.github.gerdanyJr.weekit.model.res;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
        String message,
        HttpStatus status,
        Integer code,
        LocalDateTime timestamp) {

}

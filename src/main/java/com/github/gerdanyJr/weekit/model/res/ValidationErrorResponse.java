package com.github.gerdanyJr.weekit.model.res;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;

public record ValidationErrorResponse(
        Map<String, String> errors,
        HttpStatus status,
        Integer code,
        LocalDateTime timestamp) {

}

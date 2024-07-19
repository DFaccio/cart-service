package com.cart_service.util.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
public class StandardError {

    private LocalDateTime time;

    private int statusCode;

    private String error;

    private String message;

    private String path;

    public StandardError(int statusCode, String error, String message, String path) {
        this.statusCode = statusCode;
        this.error = error;
        this.message = message;
        this.path = path;
        this.time = LocalDateTime.now()
                .atZone(ZoneId.of("America/Sao_Paulo"))
                .toLocalDateTime();
    }
}
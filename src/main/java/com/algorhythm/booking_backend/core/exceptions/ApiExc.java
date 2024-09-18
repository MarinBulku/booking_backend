package com.algorhythm.booking_backend.core.exceptions;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class ApiExc {

    /*
    * The format of api exception to be thrown in controller layer
    * */
    private final String message;
    private final HttpStatus status;
    private final ZonedDateTime timestamp;

    public ApiExc(String message, HttpStatus status, ZonedDateTime timestamp) {
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }
}

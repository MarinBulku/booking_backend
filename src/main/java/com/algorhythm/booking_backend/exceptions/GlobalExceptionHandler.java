package com.algorhythm.booking_backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {
            EntityNotFoundException.class,
            ImageTooLargeException.class,
            IncorrectFileTypeException.class
    })
    public ResponseEntity<ApiException> handleNotFoundExceptions(Exception e) {


        ApiException exceptionToBeThrown = new ApiException(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(exceptionToBeThrown, HttpStatus.BAD_REQUEST);
    }
}
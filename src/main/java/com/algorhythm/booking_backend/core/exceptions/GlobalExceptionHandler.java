package com.algorhythm.booking_backend.core.exceptions;

import jakarta.persistence.EntityExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /*
    * How exceptions are handled
    * */
    @ExceptionHandler(value = {
            EntityNotFoundException.class,
            ImageTooLargeException.class,
            IncorrectFileTypeException.class,
            MethodArgumentNotValidException.class,
            EntityExistsException.class,
    })
    public ResponseEntity<ApiExc> handleNotFoundExceptions(Exception e) {


        ApiExc exceptionToBeThrown = new ApiExc(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(exceptionToBeThrown, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {
            IOException.class,
            RuntimeException.class,
    })
    public ResponseEntity<ApiExc> handleServerExceptions(Exception e) {
        ApiExc exception = new ApiExc(
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
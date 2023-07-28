package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.errorhandling;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Response> myRuntimeException(RuntimeException exception){
        return ResponseEntity
                .status(500)
                .body(new Response("some error was occurred: "+exception.getMessage()));
    }
}

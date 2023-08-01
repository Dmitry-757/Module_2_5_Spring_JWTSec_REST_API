package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.errorhandling;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Response> myAppException(ApiException exception){
        return ResponseEntity
                .status(exception.getErrorCode())
                .body(new Response(exception.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> myAccessDeniedException(AccessDeniedException exception){
        return new ResponseEntity<>(
                "Access denied ", new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> myAuthenticationException(AuthenticationException exception) throws JsonProcessingException {
        Map<String, Object> data = new HashMap<>();
        data.put(
                "exception",
                exception.getMessage());

        return new ResponseEntity<>(
                objectMapper.writeValueAsString(data), new HttpHeaders(), HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Response> myRuntimeException(RuntimeException exception){
        return ResponseEntity
                .status(500)
                .body(new Response("some error was occurred: "+exception.getMessage()));
    }


}

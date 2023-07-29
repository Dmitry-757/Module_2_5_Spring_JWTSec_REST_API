package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.errorhandling;

import lombok.Getter;

public class ApiException extends RuntimeException{
    @Getter
    protected int errorCode;

    public ApiException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}

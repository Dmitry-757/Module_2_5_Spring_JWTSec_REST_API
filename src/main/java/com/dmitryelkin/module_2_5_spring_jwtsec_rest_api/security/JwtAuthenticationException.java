package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.security;

import org.springframework.security.core.AuthenticationException;

import java.io.Serial;

public class JwtAuthenticationException extends AuthenticationException {
    @Serial
    private static final long serialVersionUID = -7215416619632429583L;

    public JwtAuthenticationException(String msg) {
        super(msg);
    }
}

package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.DTO.ErrorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthFilter extends OncePerRequestFilter {


    private final UserAuthenticationProvider userAuthenticationProvider;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(); //**** DI ****

    public JwtAuthFilter(UserAuthenticationProvider userAuthenticationProvider) {
        this.userAuthenticationProvider = userAuthenticationProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {
            String[] authElements = header.split(" ");

            if (authElements.length == 2
                    && "Bearer".equals(authElements[0])) {
                try {
                    String token = authElements[1];
                    Authentication authentication = userAuthenticationProvider.getAuthentication(token);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                catch (JWTVerificationException e){
                    SecurityContextHolder.clearContext();

                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                    OBJECT_MAPPER.writeValue(response.getOutputStream(), new ErrorDto("JWT token is expired or invalid"));

                    throw new JWTVerificationException("JWT token is expired or invalid");
                }

                catch (RuntimeException e) {
                    SecurityContextHolder.clearContext();
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                    OBJECT_MAPPER.writeValue(response.getOutputStream(), new ErrorDto("JWT token is expired or invalid"));

                    throw e;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}

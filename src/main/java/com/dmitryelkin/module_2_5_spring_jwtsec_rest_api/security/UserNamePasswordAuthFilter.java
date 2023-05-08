package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.security;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.DTO.CredentialsDTO;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.DTO.ErrorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class UserNamePasswordAuthFilter extends OncePerRequestFilter {
    private final ObjectMapper MAPPER = new ObjectMapper();
    private final UserAuthenticationProvider userAuthenticationProvider;

    public UserNamePasswordAuthFilter(UserAuthenticationProvider userAuthenticationProvider) {
        this.userAuthenticationProvider = userAuthenticationProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if ("/api/v1/signIn/**".equals(request.getServletPath())
                && HttpMethod.POST.matches(request.getMethod())) {
            CredentialsDTO credentialsDto = MAPPER.readValue(request.getInputStream(), CredentialsDTO.class);

            try {
                SecurityContextHolder.getContext().setAuthentication(
                        userAuthenticationProvider.getAuthentication(credentialsDto));
            } catch (RuntimeException e) {
                SecurityContextHolder.clearContext();

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
//                MAPPER.writeValue(response.getOutputStream(), new ErrorDto("Unauthorized path"));
                MAPPER.writeValue(response.getOutputStream(), new ErrorDto(e.getMessage()));

                throw e;
            }
        }

        filterChain.doFilter(request, response);
    }
}

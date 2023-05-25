package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.DTO.CredentialsDTO;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.User;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.service.UserServiceI;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class UserAuthenticationProvider {
    @Value("${jwt.token.secret}")
    private String secretKey;

    @Value("${jwt.token.expiredPeriod}")
    private long expiredPeriod;

    private final UserServiceI userService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Autowired
    public UserAuthenticationProvider(UserServiceI userService) {
        this.userService = userService;
    }

    @PostConstruct
    protected void init() {
        // this is to avoid having the raw secret key available in the JVM
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // validate credentials
    public Authentication getAuthentication(CredentialsDTO credentialsDto) {
        User user = userService.getByName(credentialsDto.getLogin());
        if (user == null){
            log.info("Invalid login in credentialsDto");
            throw new UsernameNotFoundException("Invalid login");
        }

        String dtoPassword = credentialsDto.getPassword();

        String encodedUserPassword = passwordEncoder.encode(user.getPassword());
//        String encodedUserPassword = user.getPassword();
        if (passwordEncoder.matches(dtoPassword, encodedUserPassword)){
            UserDetails userDetails = JwtUserFactory.create(user);
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        }
        log.info("Invalid password in credentialsDto");
        throw new RuntimeException("Invalid password");
    }

    // validate token
    public Authentication getAuthentication(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        try {
            DecodedJWT decoded = verifier.verify(token);

            if (decoded.getExpiresAt().before(new Date())) {
                log.info("JWT token is expired");
                throw new JwtAuthenticationException("JWT token is expired");
            }

            User user = userService.getByName(decoded.getIssuer());
            UserDetails userDetails = JwtUserFactory.create(user);

            //UsernamePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities)
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        } catch (JWTVerificationException e){
            log.info("JWT token is expired or invalid");
            throw new JwtAuthenticationException("JWT token is expired or invalid");
        }
    }


    public String createToken(String userName) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + expiredPeriod); // 1 hour

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.create()
                .withIssuer(userName)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .sign(algorithm);
    }




}

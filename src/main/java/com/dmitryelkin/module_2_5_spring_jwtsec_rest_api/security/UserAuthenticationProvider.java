package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.DTO.CredentialsDTO;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.DTO.UserDTO;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.User;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.security.jwt_old.JwtAuthenticationException;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.service.UserServiceI;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;

@Component
public class UserAuthenticationProvider {
    @Value("${jwt.token.secret}")
    private String secretKey;

    private final PasswordEncoder passwordEncoder;
    private final UserServiceI userService;

    @Autowired
    public UserAuthenticationProvider(PasswordEncoder passwordEncoder, UserServiceI userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @PostConstruct
    protected void init() {
        // this is to avoid having the raw secret key available in the JVM
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public Authentication validateCredentials(CredentialsDTO credentialsDto) {
        UserDTO user = authenticate(credentialsDto);

        //UsernamePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities)
        return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
    }

    public UserDTO authenticate(CredentialsDTO credentialsDto) {

        String dtoPassword = credentialsDto.getPassword();
        String dtoUserName = credentialsDto.getLogin();

        User user = userService.getByName(dtoUserName);
        if (user == null){
            throw new RuntimeException("Invalid login");
        }

//        String encodedUserPassword = passwordEncoder.encode(user.getPassword());
        String encodedUserPassword = user.getPassword();
        if (passwordEncoder.matches(dtoPassword, encodedUserPassword)){
            //return new UserDto(1L, "Sergio", "Lema", "login", "token");
            return new UserDTO(user.getName(), createToken(user.getName()));
        }

        throw new RuntimeException("Invalid password");
    }

    public String createToken(String userName) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 3600000); // 1 hour

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.create()
                .withIssuer(userName)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .sign(algorithm);
    }
    public Authentication validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        try {
            DecodedJWT decoded = verifier.verify(token);

            User user = userService.getByName(decoded.getIssuer());
            if (decoded.getExpiresAt().before(new Date())) {
                throw new JwtAuthenticationException("JWT token is expired or invalid");
            }

            return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
        } catch (JWTVerificationException e){
            throw new JwtAuthenticationException("JWT token is expired or invalid");
        }
    }

//    public boolean validateToken(String token){
//        try {
//            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
//
//            if (claims.getBody().getExpiration().before(new Date())) {
//                return false;
//            }
//
//            return true;
//        } catch (JwtException | IllegalArgumentException e) {
//            throw new JwtAuthenticationException("JWT token is expired or invalid");
//        }
//    }


}

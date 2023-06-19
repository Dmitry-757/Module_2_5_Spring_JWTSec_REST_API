package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.DTO.CredentialsDTO;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class authIntegrationTest {
    @LocalServerPort
    int port;

    @Value("${jwt.token.secret}")
    private String secretKey;

    @Test
    void auth() {

        RestAssured.port = port;

        CredentialsDTO credentialsDTO = new CredentialsDTO("user_3", "pass345");
        Response response = RestAssured
                .given()
                .log().all()
//                .auth().basic("user_3", "pass345")
                .contentType("application/json")
                .urlEncodingEnabled(false)
                .and()
                .body(credentialsDTO)
                .when()
                .request("POST", "/api/v1/auth/signIn")
                .then()
                .log().all()
                .extract()
                .response()
                ;
        String userName = response.jsonPath().get("name");
        String token = response.jsonPath().get("token");

        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        DecodedJWT decoded = verifier.verify(token);
        String userNameByToken = decoded.getIssuer();

        assertEquals(HttpStatus.OK.value(), response.statusCode());
        assertEquals(userName, credentialsDTO.getLogin());
        assertEquals(userName, userNameByToken);
    }

}

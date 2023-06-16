package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.controller;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.DTO.CredentialsDTO;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class authTest {
    @LocalServerPort
    int port;

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
        assertEquals(HttpStatus.OK.value(), response.statusCode());
        assertEquals(userName, credentialsDTO.getLogin());
    }

}

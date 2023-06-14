package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.DTO.CredentialsDTO;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableWebSecurity(debug = true)
@SpringBootApplication
public class Module25SpringJwtSecRestApiApplication {


    public static void main(String[] args) {
        SpringApplication.run(Module25SpringJwtSecRestApiApplication.class, args);

        RestAssured.port = 8090;

        CredentialsDTO credentialsDTO = new CredentialsDTO("user_3", "pass345");
        Response response = RestAssured
                .given()
                .contentType("application/json")
                .and()
                .body(credentialsDTO)
                .when()
                .request("POST", "/api/v1/auth/signIn/")
                .then()
                .extract()
                .response();
        System.out.println(response);
    }

}

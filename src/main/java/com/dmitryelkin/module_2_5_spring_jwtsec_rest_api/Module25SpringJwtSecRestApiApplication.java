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

        myTest();
    }

    public static void myTest(){
        RestAssured.port = 8090;

        CredentialsDTO credentialsDTO = new CredentialsDTO("user_3", "pass345");
        Response response = RestAssured
                .given()
                .log().all()
//                .auth().basic("user_3", "pass345")
                .contentType("application/json")
                .urlEncodingEnabled(false)
                .and()
                .body(credentialsDTO)
//                .body("""
//{"login":"user_3",
//"password":"pass345"
//}
//""")
                .when()
                .request("POST", "/api/v1/auth/signIn")
                .then()
                .log().all()
                .extract()
                .response()
                ;
        System.out.println("Status code: "+response.getStatusCode());
    }

}

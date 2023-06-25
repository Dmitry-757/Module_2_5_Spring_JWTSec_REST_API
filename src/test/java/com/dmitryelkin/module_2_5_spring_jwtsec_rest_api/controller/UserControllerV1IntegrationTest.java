package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.controller;


import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.Role;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.Status;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.User;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.repository.UserRepositoryI;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerV1IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserRepositoryI mockRepository;

    @LocalServerPort
    int port;

    @BeforeEach
    void setup() {
        mockRepository.deleteAll();
        RestAssured.port = port;
    }

    @Test
//    @WithMockUser(authorities="ADMIN")
//    @WithMockUser(value = "user1Name", password = "123")
    @WithMockUser()
    public void getAllUsers_whenMockMVC_thenVerifyResponse() throws Exception {

        var users = List.of(
                new User("user1Name", "123"),
                new User("user2Name", "321"),
                new User("user3Name", "213")
        );

//        BDDMockito.given(this.mockRepository.findAll()).willReturn(users);
        Mockito.doReturn(users).when(mockRepository).findAll();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0].name", Matchers.is("user1Name")))
                .andExpect(content().json("""
                           [
                                    {"name":"user1Name","token":null},
                                    {"name":"user2Name","token":null},
                                    {"name":"user3Name","token":null}
                           ]
                        """))
        ;
    }


    @Test
//    @WithMockUser(value = "user1Name", password = "123")
    @WithMockUser()
    void getById_ReturnsValidResponseEntity() throws Exception {
        // given
        User user = new User(123L, "user1Name", "pass1", new ArrayList<>(), Status.ACTIVE, Role.USER);
        Mockito.doReturn(Optional.of(user)).when(mockRepository).findById(user.getId());

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json("""
                                    {"name":"user1Name","token":null}
                        """));

    }


    @Test
//    @WithMockUser(value = "user1Name", password = "123")
    @WithMockUser()
    void getByName_ReturnsValidResponseEntity() throws Exception {
        // given
        User user = new User(123L, "user1Name", "pass1", new ArrayList<>(), Status.ACTIVE, Role.USER);
        Mockito.doReturn(Optional.of(user)).when(mockRepository).findByName(user.getName());

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/search/user1Name"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json("""
                            {"name":"user1Name", "token": null}
                        """));
    }

    @Test
    void createItem_ReturnsValidResponseEntity() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        // given
        User user = new User(444L, "user4Name", "pass4", new ArrayList<>(), Status.ACTIVE, Role.USER);
        Mockito.doReturn(user).when(mockRepository).saveAndFlush(user);
        MockMvcResponse response = RestAssuredMockMvc
                .given()
                    .auth().with(SecurityMockMvcRequestPostProcessors.user("user").password("pass345").roles("ADMIN"))
                    .contentType(ContentType.JSON)
                    .body(user)
                .when()
                    .request("POST", "/api/v1/users/")
                .then()
                .extract()
                .response();

        assertEquals(201, response.statusCode());
        assertEquals(response.as(User.class), user);
    }


    @Test
    void update() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        // given
        User user = new User(444L, "user4Name", "pass4", new ArrayList<>(), Status.ACTIVE, Role.USER);
        Mockito.doReturn(user).when(mockRepository).saveAndFlush(user);
        Mockito.doReturn(true).when(mockRepository).existsById(444L);

        User nonExistentUser = new User(445L, "nonExistentUser", "pass", new ArrayList<>(), Status.ACTIVE, Role.USER);
        Mockito.doReturn(user).when(mockRepository).saveAndFlush(nonExistentUser);

        // when
        MockMvcResponse response = RestAssuredMockMvc
                .given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("user").password("pass345").roles("ADMIN"))
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .request("PUT", "/api/v1/users/")
                .then()
                .extract()
                .response();
        // then
        assertEquals(200, response.statusCode());
        assertEquals(response.as(User.class), user);

        // when
        response = RestAssuredMockMvc
                .given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("user").password("pass345").roles("ADMIN"))
                .contentType(ContentType.JSON)
                .body(nonExistentUser)
                .when()
                .request("PUT", "/api/v1/users/")
                .then()
                .extract()
                .response();

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
        assertEquals(response.getBody().asString(),"No such item for update");

    }

    @Test
    void delete() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        // given
        User user = new User(444L, "user4Name", "pass4", new ArrayList<>(), Status.ACTIVE, Role.USER);
        User deletedUser = new User(444L, "user4Name", "pass4", new ArrayList<>(), Status.DELETED, Role.USER);
        Mockito.doReturn(deletedUser).when(mockRepository).saveAndFlush(user);
        Mockito.doReturn(Optional.of(user)).when(mockRepository).findById(444L);


        // when
//        MockMvcResponse response = RestAssuredMockMvc
//                .given()
//                .auth().with(SecurityMockMvcRequestPostProcessors.user("user").password("pass345").roles("ADMIN"))
//                .contentType(ContentType.JSON)
//                .body(user)
//                .when()
//                .request("DELETE", "/api/v1/users/444")
//                .then()
//                .extract()
//                .response();
//        // then
//        assertEquals(response.statusCode(),HttpStatus.OK.value());
//        assertEquals(response.as(User.class), deletedUser);


        // given
        Mockito.doReturn(Optional.empty()).when(mockRepository).findById(445L);

        // when
        MockMvcResponse response = RestAssuredMockMvc
                .given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("user").password("pass345").roles("ADMIN"))
                .contentType(ContentType.JSON)
//                .body(user)
                .when()
                .request("DELETE", "/api/v1/users/445")
                .then()
                .extract()
                .response();
        // then
        assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode());
        assertEquals(response.getBody().asString(),"No such item for deleting");


    }
}
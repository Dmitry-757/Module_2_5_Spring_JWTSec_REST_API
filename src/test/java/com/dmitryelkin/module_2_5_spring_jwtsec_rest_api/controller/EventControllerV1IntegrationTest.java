package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.controller;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.DTO.EventDTO;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.*;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.repository.EventRepositoryI;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.repository.UserRepositoryI;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.service.EventServiceI;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.service.EventServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:application.properties"})
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude=FlywayAutoConfiguration.class)
class EventControllerV1IntegrationTest extends SpringBootApplicationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    EventRepositoryI mockRepository;

    @MockBean
    UserRepositoryI mockUserRepository;

//    @MockBean
//    EventServiceI mockEventService;

    @InjectMocks
    private EventServiceImpl service;


    @LocalServerPort
    int port;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockRepository.deleteAll();
        RestAssured.port = port;
    }

    @Test
    @WithMockUser()
    void getAll() throws Exception {
        var items = List.of(
                new Event(123L, LocalDateTime.now(), new User("userName1", "pass1"), new File(), Status.ACTIVE, TypeOfEvent.FORTEST),
                new Event(124L, LocalDateTime.now(), new User("userName2", "pass2"), new File(), Status.ACTIVE, TypeOfEvent.FORTEST),
                new Event(125L, LocalDateTime.now(), new User("userName3", "pass3"), new File(), Status.ACTIVE, TypeOfEvent.FORTEST)
        );

//        BDDMockito.given(this.mockRepository.findAll()).willReturn(items);
        Mockito.doReturn(items).when(mockRepository).findAll();
            List<EventDTO> itemsDTO = items.stream()
                    .map(EventDTO::new)
                    .toList();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/events/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemsDTO)))
        ;

    }

    @Test
    @WithMockUser()
    void getById() throws Exception {
        // given
        Event item = new Event(123L, LocalDateTime.now(), new User("userName1", "pass1"), new File(), Status.ACTIVE, TypeOfEvent.FORTEST);
        EventDTO expectedItem = new EventDTO(item);
        Mockito.doReturn(Optional.of(item)).when(mockRepository).findById(item.getId());

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/events/"+item.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedItem))
                );
    }

    @Test
    void createItem() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        // given
        User user = new User("userName1", "pass1");
        Event item = new Event(123L, LocalDateTime.now(), user, new File(), Status.ACTIVE, TypeOfEvent.FORTEST);
        Mockito.doReturn(item).when(mockRepository).saveAndFlush(item);

        MockMvcResponse response = RestAssuredMockMvc
                .given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("user").password("pass345").roles("ADMIN"))
                .contentType(ContentType.JSON)
                .body(item)
                .when()
                .request("POST", "/api/v1/events/")
                .then()
                .extract()
                .response();

        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
        assertEquals(response.as(Event.class), item);

    }

    @Test
    void update() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        // given
        Event item = new Event(123L, LocalDateTime.now(), new User("userName1", "pass1"),
                new File(), Status.ACTIVE, TypeOfEvent.FORTEST);
        Mockito.doReturn(item).when(mockRepository).saveAndFlush(item);
        Mockito.doReturn(true).when(mockRepository).existsById(item.getId());

        Event nonExistentItem = new Event(321L, LocalDateTime.now(), new User("nonExistentUser", "pass1"),
                new File(), Status.ACTIVE, TypeOfEvent.FORTEST);
        Mockito.doReturn(null).when(mockRepository).saveAndFlush(nonExistentItem);

        // when
        MockMvcResponse response = RestAssuredMockMvc
                .given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("user").password("pass345").roles("ADMIN"))
                .contentType(ContentType.JSON)
                .body(item)
                .when()
                .request("PUT", "/api/v1/events/")
                .then()
                .extract()
                .response();
        // then
        assertEquals(200, response.statusCode());
        assertEquals(response.as(Event.class), item);

        // when
        response = RestAssuredMockMvc
                .given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("user").password("pass345").roles("ADMIN"))
                .contentType(ContentType.JSON)
                .body(nonExistentItem)
                .when()
                .request("PUT", "/api/v1/events/")
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
        Event item = new Event(123L, LocalDateTime.now(), new User("userName1", "pass1"),
                new File(), Status.ACTIVE, TypeOfEvent.FORTEST);
        Event deletedItem = new Event(123L, LocalDateTime.now(), new User("userName1", "pass1"),
                new File(), Status.DELETED, TypeOfEvent.FORTEST);

        Mockito.doReturn(deletedItem).when(mockRepository).saveAndFlush(item);
        Mockito.doReturn(Optional.of(item)).when(mockRepository).findById(item.getId());

        // given
        Mockito.doReturn(Optional.empty()).when(mockRepository).findById(321L);

        // when
        MockMvcResponse response = RestAssuredMockMvc
                .given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("user").password("pass345").roles("ADMIN"))
                .contentType(ContentType.JSON)
//                .body(user)
                .when()
                .request("DELETE", "/api/v1/events/444")
                .then()
                .extract()
                .response();
        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
        assertEquals("No such item for deleting", response.getBody().asString());

    }
}
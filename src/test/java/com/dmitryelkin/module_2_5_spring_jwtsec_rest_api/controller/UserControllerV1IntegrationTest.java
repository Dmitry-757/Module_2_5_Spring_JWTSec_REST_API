package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.controller;


import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.DTO.UserDTO;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.Role;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.Status;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.User;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.repository.UserRepositoryI;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.service.UserServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
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

    @BeforeEach
    void setup(){
        mockRepository.deleteAll();
    }

    @Test
//    @WithMockUser(authorities="ADMIN")
    @WithMockUser(value = "user1Name", password = "123")
    public void getAllUsers_whenMockMVC_thenVerifyResponse() throws Exception {

        var users = List.of(
                new User("user1Name","123"),
                new User("user2Name","321"),
                new User("user3Name","213")
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
    void getAllUsers_ReturnsValidResponseEntity() {

        UserRepositoryI repository = Mockito.mock(UserRepositoryI.class);
        UserServiceImpl service = new UserServiceImpl(repository);
        UserControllerV1 controller = new UserControllerV1(service);

        // given
        var users = List.of(
                new User("user1Name","123"),
                new User("user2Name","321"),
                new User("user3Name","213")
        );
        var userDtos = users.stream()
                .map(u->(new UserDTO(u.getName())))
                .toList();
        Mockito.doReturn(users).when(repository).findAll();

        // when
        var responseEntity = controller.getAll();

        // then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userDtos, responseEntity.getBody());
    }

    @Test
    @WithMockUser(value = "user1Name", password = "123")
    void getById_ReturnsValidResponseEntity() throws Exception {
        // given
        User user = new User(123L,"user1Name", "pass1", new ArrayList<>(), Status.ACTIVE, Role.USER);
        Mockito.doReturn(Optional.of(user)).when(mockRepository).findById(123L);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json("""
                        {"name":"user1Name","token":null}
            """));

    }

    @Test
    void getByName() {
    }

    @Test
    void createItem() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}
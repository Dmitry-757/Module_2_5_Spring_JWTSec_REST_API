package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.controller;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.DTO.UserDTO;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.User;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserControllerV1Test {
    @Mock
    UserServiceImpl service;

    @InjectMocks
    UserControllerV1 controller;


    @Test
    void getAllUsers_ReturnsValidResponseEntity() {
        // given
        var users = List.of(
                new User("user1Name","123"),
                new User("user2Name","321"),
                new User("user3Name","213")
        );
        var userDtos = users.stream()
                .map(u->(new UserDTO(u.getName())))
                .toList();
        Mockito.doReturn(users).when(this.service).getAll();

        // when
        var responseEntity = this.controller.getAll();

        // then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userDtos, responseEntity.getBody());
    }

    @Test
    void getById() {
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
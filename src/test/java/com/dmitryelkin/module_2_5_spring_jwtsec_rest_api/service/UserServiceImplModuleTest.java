package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.service;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.Role;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.Status;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.User;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.repository.UserRepositoryI;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class UserServiceImplModuleTest {

    @Mock
    private UserRepositoryI repository = Mockito.mock(UserRepositoryI.class);

    @InjectMocks
    private UserServiceImpl service;

    @Test
    void create() {

//        UserRepositoryI repository = Mockito.mock(UserRepositoryI.class);
//        UserServiceImpl service = new UserServiceImpl(repository);

        // given
        User newUser1 = new User("userName1", "pass1");
        User newUser2 = new User(1L,"userName1", "pass1", new ArrayList<>(), Status.ACTIVE, Role.USER);
        Mockito.doReturn(newUser2).when(repository).saveAndFlush(newUser1);

        // when
        var responseEntity = service.create(newUser1);

        // then
        assertNotNull(responseEntity);
        assertEquals(newUser2, responseEntity);

    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void getAll() {
    }

    @Test
    void getById() {
    }

    @Test
    void getByName() {
    }
}
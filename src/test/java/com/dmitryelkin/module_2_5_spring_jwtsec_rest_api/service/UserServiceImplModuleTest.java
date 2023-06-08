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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserServiceImplModuleTest {

    @Mock
    private UserRepositoryI repository = Mockito.mock(UserRepositoryI.class);

    @InjectMocks
    private UserServiceImpl service;

//    @BeforeEach
//    void init(){
//
//    }

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
        assertEquals(newUser2, responseEntity);
    }

    @Test
    void delete() {

        // given
        User user1 = new User(1L,"userName1", "pass1", new ArrayList<>(), Status.ACTIVE, Role.USER);
        User user2 = new User(1L,"userName1", "pass1", new ArrayList<>(), Status.DELETED, Role.USER);
        Mockito.doReturn(Optional.of(user2)).when(repository).findById(user1.getId());

        // when
        var responseEntity = service.delete(user1.getId());

        // then
        assertEquals(user2, responseEntity);
    }

    @Test
    void getAll() {
        // given
        var users = List.of(
                new User(1L,"userName1", "pass1", new ArrayList<>(), Status.ACTIVE, Role.USER),
                new User(2L,"userName2", "pass2", new ArrayList<>(), Status.ACTIVE, Role.USER),
                new User(3L,"userName3", "pass3", new ArrayList<>(), Status.ACTIVE, Role.USER)
        );
        Mockito.doReturn(users).when(repository).findAll();

        // when
        var responseEntity = service.getAll();

        // then
        assertEquals(users, responseEntity);
    }

    @Test
    void getById() {
        // given
        User user = new User(1L,"userName1", "pass1", new ArrayList<>(), Status.ACTIVE, Role.USER);
        Mockito.doReturn(Optional.of(user)).when(repository).findById(user.getId());

        // when
        var responseEntity = service.getById(user.getId());

        // then
        assertEquals(user, responseEntity);
    }

    @Test
    void getByName() {
        // given
        User user = new User(1L,"userName1", "pass1", new ArrayList<>(), Status.ACTIVE, Role.USER);
        Mockito.doReturn(Optional.of(user)).when(repository).findByName(user.getName());

        // when
        var responseEntity = service.getByName(user.getName());

        // then
        assertEquals(user, responseEntity);
    }
}
package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.service;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.Event;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.File;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.Status;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.User;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.repository.EventRepositoryI;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
@ExtendWith(MockitoExtension.class)
class EventServiceImplModuleTest {

    @Mock
    private EventRepositoryI repository = Mockito.mock(EventRepositoryI.class);

    @InjectMocks
    private EventServiceImpl service;

    @Test
    void create() {
        // given
        Event item1 = new Event(123L, LocalDateTime.now(), new User("userName1", "pass1"), new File(), Status.ACTIVE);
        Mockito.doReturn(item1).when(repository).saveAndFlush(item1);

        // when
        var responseEntity = service.create(item1);

        // then
        assertEquals(item1, responseEntity);
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
        // given
        Event item1 = new Event(123L, LocalDateTime.now(), new User("userName1", "pass1"), new File(), Status.ACTIVE);
        Event item2 = new Event(123L, LocalDateTime.now(), new User("userName1", "pass1"), new File(), Status.DELETED);
        Mockito.doReturn(Optional.of(item2)).when(repository).findById(item1.getId());

        // when
        var responseEntity = service.delete(item1.getId());

        // then
        assertEquals(item2, responseEntity);

    }

    @Test
    void getAll() {
        // given
        var users = List.of(
                new Event(123L, LocalDateTime.now(), new User("userName1", "pass1"), new File(), Status.ACTIVE),
                new Event(123L, LocalDateTime.now(), new User("userName2", "pass2"), new File(), Status.ACTIVE),
                new Event(123L, LocalDateTime.now(), new User("userName3", "pass3"), new File(), Status.ACTIVE)
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
        Event item = new Event(123L, LocalDateTime.now(), new User("userName1", "pass1"), new File(), Status.ACTIVE);
        Mockito.doReturn(Optional.of(item)).when(repository).findById(item.getId());

        // when
        var responseEntity = service.getById(item.getId());

        // then
        assertEquals(item, responseEntity);

    }

    @Test
    void getByUser() {
        // given
        Event item = new Event(123L, LocalDateTime.now(), new User("userName1", "pass1"), new File(), Status.ACTIVE);
        Mockito.doReturn(Optional.of(item)).when(repository).findByUser(item.getUser());

        // when
        var responseEntity = service.getByUser(item.getUser());

        // then
        assertEquals(item, responseEntity);
    }
}
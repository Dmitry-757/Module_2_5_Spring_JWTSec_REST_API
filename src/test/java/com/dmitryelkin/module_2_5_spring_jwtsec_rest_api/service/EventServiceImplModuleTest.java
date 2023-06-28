package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.service;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.*;
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
        Event item1 = new Event(123L, LocalDateTime.now(), new User("userName1", "pass1"), new File(), Status.ACTIVE, TypeOfEvent.FORTEST);
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
        Event item1 = new Event(123L, LocalDateTime.now(), new User("userName1", "pass1"), new File(), Status.ACTIVE, TypeOfEvent.FORTEST);
        Event item2 = new Event(123L, LocalDateTime.now(), new User("userName1", "pass1"), new File(), Status.DELETED, TypeOfEvent.FORTEST);
        Mockito.doReturn(Optional.of(item2)).when(repository).findById(item1.getId());

        // when
        var responseEntity = service.delete(item1.getId());

        // then
        assertEquals(item2, responseEntity);

    }

    @Test
    void getAll() {
        // given
        var items = List.of(
                new Event(123L, LocalDateTime.now(), new User("userName1", "pass1"), new File(), Status.ACTIVE, TypeOfEvent.FORTEST),
                new Event(124L, LocalDateTime.now(), new User("userName2", "pass2"), new File(), Status.ACTIVE, TypeOfEvent.FORTEST),
                new Event(125L, LocalDateTime.now(), new User("userName3", "pass3"), new File(), Status.ACTIVE, TypeOfEvent.FORTEST)
        );
        Mockito.doReturn(items).when(repository).findAll();

        // when
        var responseEntity = service.getAll();

        // then
        assertEquals(items, responseEntity);

    }

    @Test
    void getById() {
        // given
        Event item = new Event(123L, LocalDateTime.now(), new User("userName1", "pass1"), new File(), Status.ACTIVE, TypeOfEvent.FORTEST);
        Mockito.doReturn(Optional.of(item)).when(repository).findById(item.getId());

        // when
        var responseEntity = service.getById(item.getId());

        // then
        assertEquals(item, responseEntity);

    }

    @Test
    void getByUser() {
        // given
        Event item = new Event(123L, LocalDateTime.now(), new User("userName1", "pass1"), new File(), Status.ACTIVE, TypeOfEvent.FORTEST);
        Mockito.doReturn(Optional.of(item)).when(repository).findByUser(item.getUser());

        // when
        var responseEntity = service.getByUser(item.getUser());

        // then
        assertEquals(item, responseEntity);
    }
}
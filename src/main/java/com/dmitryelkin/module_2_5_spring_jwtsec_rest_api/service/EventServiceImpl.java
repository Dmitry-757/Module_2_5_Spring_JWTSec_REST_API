package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.service;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.*;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.repository.EventRepositoryI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class EventServiceImpl implements EventServiceI{
    private final EventRepositoryI repository;
    private final UserServiceI userService;

    @Autowired
    public EventServiceImpl(EventRepositoryI repository, UserServiceI userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Override
    public Event create(Event item) {
        return repository.saveAndFlush(item);
    }

    @Override
    public Event update(Event item) {
        return repository.saveAndFlush(item);
    }

    @Override
    public Event delete(long id) {
        Event item = repository.findById(id).orElse(null);
        if (item!=null){
            item.setStatus(Status.DELETED);
            repository.saveAndFlush(item);
        }
        return item;
    }

    @Override
    public List<Event> getAll() {
        return repository.findAll();
    }

    @Override
    public Event getById(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Event getByUser(User user) {
        return repository.findByUser(user).orElse(null);
    }

    public void setNewEvent(String fileName){
        Principal principal = (Principal) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        User user = userService.getByName(principal.getName());
        Event event = new Event(user, new File(fileName), TypeOfEvent.DELETE);
        create(event);
    }
}

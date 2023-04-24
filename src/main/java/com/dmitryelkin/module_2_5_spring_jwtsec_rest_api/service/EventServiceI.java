package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.service;



import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.Event;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.User;

import java.util.List;

public interface EventServiceI {
    Event create(Event item);
    Event update(Event item);
    Event delete(long id);

    List<Event> getAll();
    Event getById(long id);
    Event getByUser(User user);
}

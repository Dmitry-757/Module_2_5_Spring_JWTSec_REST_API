package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.service;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.User;

import java.util.List;

public interface UserServiceI {

    User create(User item);
    User update(User item);
    User delete(long id);

    List<User> getAll();
    User getById(long id);
    User getByName(String name);
}

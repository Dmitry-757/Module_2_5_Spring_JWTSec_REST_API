package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.service;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.User;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.repository.UserRepositoryI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserServiceI{

    private final UserRepositoryI repository;

    @Autowired
    public UserServiceImpl(UserRepositoryI repository) {
        this.repository = repository;
    }

    @Override
    public User create(User item) {
        return repository.saveAndFlush(item);
    }

    @Override
    public User update(User item) {
        return repository.saveAndFlush(item);
    }

    @Override
    public User delete(long id) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public User getById(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public User getByName(String name) {
        return repository.findByName(name).orElse(null);
    }
}

package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.service;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.Status;
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
        if (repository.existsById(item.getId())) {
            return repository.saveAndFlush(item);
        } else {
            return null;
        }
    }

    @Override
    public User delete(long id) {
        User item = repository.findById(id).orElse(null);
        if (item!=null){
            item.setStatus(Status.DELETED);
            repository.saveAndFlush(item);
        }
        return item;
    }

    @Override
    public List<User> getAll() {
        return repository.findAll();
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

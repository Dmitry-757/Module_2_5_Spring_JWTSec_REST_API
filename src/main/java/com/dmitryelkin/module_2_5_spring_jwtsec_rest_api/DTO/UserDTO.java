package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.DTO;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.User;

public class UserDTO {
    private final long id;
    private final String name;

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
    }


    public User getUser(){
        return new User(id, name);
    }
}

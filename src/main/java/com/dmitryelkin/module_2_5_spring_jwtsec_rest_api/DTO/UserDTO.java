package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.DTO;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.User;

public class UserDTO {
    private final int id;
    private final String name;

    public UserDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public User toUser(){
        return new User(id, name);
    }
}

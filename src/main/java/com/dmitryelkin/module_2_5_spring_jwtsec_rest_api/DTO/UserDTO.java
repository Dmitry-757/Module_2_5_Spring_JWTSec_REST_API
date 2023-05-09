package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.DTO;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.User;

public class UserDTO {
//    private long id;
    private final String name;

    private String token;

    public UserDTO(User user) {
//        this.id = user.getId();
        this.name = user.getName();
    }

    public UserDTO(String name) {
        this.name = name;
    }

    public UserDTO(String name, String token) {
        this.name = name;
        this.token = token;
    }

//    public User getUser(){
//        return new User(id, name);
//    }

//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

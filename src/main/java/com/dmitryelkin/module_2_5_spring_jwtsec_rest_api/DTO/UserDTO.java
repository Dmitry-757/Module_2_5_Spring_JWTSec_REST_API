package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.DTO;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.User;
import lombok.Getter;

@Getter
public class UserDTO {
    private final String name;

    private String token;

    public UserDTO(User user) {
        this.name = user.getName();
    }

    public UserDTO(String name) {
        this.name = name;
    }

//    public UserDTO(String name, String token) {
//        this.name = name;
//        this.token = token;
//    }

//    public User getUser(){
//        return new User(id, name);
//    }


//    public String getName() {
//        return name;
//    }

//    public String getToken() {
//        return token;
//    }

    public void setToken(String token) {
        this.token = token;
    }
}

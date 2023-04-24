package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.controller;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.DTO.UserDTO;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.User;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users/" )
public class UserControllerV1 {
    private final UserServiceImpl service;

    @Autowired
    public UserControllerV1(UserServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll(){
        List<User> users = service.getAll();

        if(!users.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(users);
        } else{
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();

        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable long id){
        User user = service.getById(id);
        if(user!= null) {
            UserDTO dto = new UserDTO(user);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(dto);
        } else{
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();

        }
    }



}

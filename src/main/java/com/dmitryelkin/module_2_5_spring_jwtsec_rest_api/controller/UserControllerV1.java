package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.controller;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.DTO.UserDTO;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.User;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<UserDTO>> getAll(){
        List<User> users = service.getAll();
        if((users != null) && (!users.isEmpty())) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            users.stream()
                                    .map(UserDTO::new)
                                    .toList()
                    );
        } else{
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();

        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id){
        User item;
        if( (id != null) && ( (item = service.getById(id)) != null) ) {
            UserDTO dto = new UserDTO(item);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(dto);
        } else{
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();

        }
    }
    @GetMapping("/{name}")
    public ResponseEntity<UserDTO> getById(@PathVariable String name){
        User item = service.getByName(name);
        if(item != null) {
            UserDTO dto = new UserDTO(item);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(dto);
        } else{
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();

        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<User> createItem(@RequestBody User item){
        if (item != null){
            User newItem = service.create(item);
//            UserDTO dto = new UserDTO(newItem);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(newItem);
        } else {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }
    }
    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<User> update(@RequestBody User item){
        if (item != null && item.getId() != 0){
            User updatingItem = service.update(item);
//            UserDTO dto = new UserDTO(updatingItem);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(updatingItem);
        } else {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<User> delete(@PathVariable Long id){
        if( id != null ) {

            User deletingItem = service.delete(id);
//            UserDTO dto = new UserDTO(deletingItem);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(deletingItem);
        } else {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }
    }



}

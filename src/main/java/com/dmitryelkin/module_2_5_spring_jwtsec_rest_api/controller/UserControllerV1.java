package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.controller;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.DTO.UserDTO;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.errorhandling.ApiException;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.User;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users/")
public class UserControllerV1 {
    private final UserServiceImpl service;

    @Autowired
    public UserControllerV1(UserServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        List<User> users = service.getAll();
        if ((users != null) && (!users.isEmpty())) {

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            users.stream()
                                    .map(u -> (new UserDTO(u.getName())))
                                    .toList()
                    );
        } else {
            throw new ApiException("users was not found!",HttpStatus.NOT_FOUND.value());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        User item;
        if ((id != null) && ((item = service.getById(id)) != null)) {
            UserDTO dto = new UserDTO(item);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(dto);
        } else {
            throw new ApiException("user was not found!",HttpStatus.NOT_FOUND.value());
        }
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<UserDTO> getByName(@PathVariable String name) {
        User item = service.getByName(name);
        if (item != null) {
            UserDTO dto = new UserDTO(item);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(dto);
        } else {
            throw new ApiException("user was not found!",HttpStatus.BAD_REQUEST.value());
        }
    }


    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<User> createItem(@RequestBody User item) {
        if (item != null) {
            User newItem = service.create(item);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(newItem);
        } else {
            throw new ApiException("bad item was  passed!",HttpStatus.BAD_REQUEST.value());
        }
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> update(@RequestBody User item) {
        if (item != null && item.getId() != 0) {
            User updatingItem = service.update(item);
            if (updatingItem != null) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(updatingItem);
            } else {
                throw new ApiException("No such item for update!",HttpStatus.BAD_REQUEST.value());
            }

        } else {
            throw new ApiException("bad item was  passed!",HttpStatus.BAD_REQUEST.value());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (id != null) {

            User deletingItem = service.delete(id);
            if (deletingItem != null) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(deletingItem);
            } else {
                throw new ApiException("No such item for deleting!", HttpStatus.NOT_FOUND.value());
            }

        } else {
            throw new ApiException("id is not correct!",HttpStatus.BAD_REQUEST.value());
        }
    }

}

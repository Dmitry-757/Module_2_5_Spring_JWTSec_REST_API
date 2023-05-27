package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.controller;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.DTO.CredentialsDTO;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.DTO.UserDTO;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.User;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.security.UserAuthenticationProvider;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.service.UserServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthenticationController {
    private final UserAuthenticationProvider userAuthenticationProvider;
    private final UserServiceI userService;


    @Autowired
    public AuthenticationController(UserAuthenticationProvider userAuthenticationProvider, UserServiceI userService) {
        this.userAuthenticationProvider = userAuthenticationProvider;
        this.userService = userService;
    }


    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody CredentialsDTO credentialsDto){
        User user = userService.getByName(credentialsDto.getLogin());
        if (user == null){
            SecurityContextHolder.clearContext();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("user was not found");
        }

        String token = userAuthenticationProvider.createToken(user.getName());
        UserDTO userDTO = new UserDTO(user.getName());
        userDTO.setToken(token);

        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/signOut")
    public ResponseEntity<String> signOut() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("user signed out");
    }
}

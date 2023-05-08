package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.security.jwt_old;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.User;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.security.JwtUserDetailsImpl;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.security.JwtUserFactory;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.service.UserServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {
    private final UserServiceI userService;

    @Autowired
    public JwtUserDetailsServiceImpl(UserServiceI userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getByName(username);
        if (user == null){
            throw new UsernameNotFoundException("User with name "+username+" not found");
        }

        JwtUserDetailsImpl jwtUser = JwtUserFactory.create(user);

        return jwtUser;
    }
}

package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.security;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.Role;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.Status;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public class JwtUserFactory {
    public JwtUserFactory() {
    }

    public static JwtUserDetailsImpl create(User user) {
        return new JwtUserDetailsImpl(user.getId(),
                user.getName(),
                user.getPassword(),
                user.getStatus().equals(Status.ACTIVE),
                mapToGrantedAuthorities( List.of(user.getRole() )
                )
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> userRoles) {

        return userRoles.stream()
                //new SimpleGrantedAuthority("ROLE_"+role.name()) - ROLE_ because requestMatchers().hasRole("ROLE_"+auth)
                // == requestMatchers().hasAuthority(auth)
                .map(role -> new SimpleGrantedAuthority("ROLE_"+role.name()))
                .collect(Collectors.toList());
    }
}

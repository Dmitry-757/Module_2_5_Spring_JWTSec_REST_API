package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.security;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    @Qualifier("delegatedAuthenticationEntryPoint")
//    @Qualifier("customAuthenticationEntryPoint")
    AuthenticationEntryPoint authEntryPoint;
    private final UserAuthenticationProvider userAuthenticationProvider;

    public SecurityConfig(UserAuthenticationProvider userAuthenticationProvider) {
        this.userAuthenticationProvider = userAuthenticationProvider;
    }


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
//                .httpBasic(basic -> basic.disable())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sessionManagement ->
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) )
                .addFilterBefore(new UserNamePasswordAuthFilter(userAuthenticationProvider), BasicAuthenticationFilter.class)//впендюриваем наш фильтр перед BasicAuthenticationFilter
                .addFilterBefore(new JwtAuthFilter(userAuthenticationProvider), UserNamePasswordAuthFilter.class)//а этот перед UsernamePasswordAuthFilter
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/signIn/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/error").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/users/**").hasAnyRole(
                                Role.ADMIN.name(),
                                Role.MODERATOR.name(),
                                Role.USER.name()
                        )
                        .requestMatchers(HttpMethod.POST, "/api/v1/users/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/api/v1/users/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasRole(Role.ADMIN.name())

                        .requestMatchers(HttpMethod.GET, "/api/v1/events/**").hasAnyRole(
                                Role.ADMIN.name(),
                                Role.MODERATOR.name(),
                                Role.USER.name()
                        )
                        .requestMatchers(HttpMethod.POST, "/api/v1/events/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/api/v1/events/**").hasAnyRole(
                                Role.ADMIN.name(),
                                Role.MODERATOR.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/events/**").hasAnyRole(
                                Role.ADMIN.name(),
                                Role.MODERATOR.name()
                        )

                        .requestMatchers(HttpMethod.GET, "/api/v1/files/**").hasAnyRole(
                                Role.ADMIN.name(),
                                Role.MODERATOR.name(),
                                Role.USER.name()
                        )
                        .requestMatchers(HttpMethod.POST, "/api/v1/files/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/api/v1/files/**").hasAnyRole(
                                Role.ADMIN.name(),
                                Role.MODERATOR.name()
                        )
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/files/**").hasAnyRole(
                                Role.ADMIN.name(),
                                Role.MODERATOR.name()
                        )

                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(((request, response, accessDeniedException) ->
                        {accessDeniedException.printStackTrace();
                            System.out.println(accessDeniedException.getMessage());}))
//                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
//                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .authenticationEntryPoint(authEntryPoint)
                )
        ;
        return http.build();
    }

}

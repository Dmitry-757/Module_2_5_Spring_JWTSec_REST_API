package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.security;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

//    private final UserAuthenticationEntryPoint userAuthenticationEntryPoint;
    private final UserAuthenticationProvider userAuthenticationProvider;

//    public SecurityConfig(UserAuthenticationEntryPoint userAuthenticationEntryPoint,
//                          UserAuthenticationProvider userAuthenticationProvider) {
//        this.userAuthenticationEntryPoint = userAuthenticationEntryPoint;
//        this.userAuthenticationProvider = userAuthenticationProvider;
//    }
public SecurityConfig(UserAuthenticationProvider userAuthenticationProvider) {
    this.userAuthenticationProvider = userAuthenticationProvider;
}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
//                .exceptionHandling().authenticationEntryPoint(userAuthenticationEntryPoint)
                .addFilterBefore(new UserNamePasswordAuthFilter(userAuthenticationProvider), BasicAuthenticationFilter.class)//впендюриваем наш фильтр перед BasicAuthenticationFilter
                .addFilterBefore(new JwtAuthFilter(userAuthenticationProvider), UserNamePasswordAuthFilter.class)//а этот перед UsernamePasswordAuthFilter
//                .authorizeHttpRequests(request -> request
//                        .requestMatchers(HttpMethod.POST,"/api/v1/signIn").permitAll()
//                        .anyRequest().authenticated()
                .authorizeHttpRequests()
//                .requestMatchers("/").permitAll()
                .requestMatchers("/","/api/v1/signIn/**").permitAll()

                //***** "/api/v1/users/**"
                .requestMatchers(HttpMethod.GET, "/api/v1/users/**").hasAnyRole(
                        Role.ADMIN.name(),
                        Role.MODERATOR.name(),
                        Role.USER.name())
                .requestMatchers(HttpMethod.POST, "/api/v1/users/**").hasRole(Role.ADMIN.name())
                .requestMatchers(HttpMethod.PUT, "/api/v1/users/**").hasRole(Role.ADMIN.name())
                .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasRole(Role.ADMIN.name())

                //***** "/api/v1/events/**"
                .requestMatchers(HttpMethod.GET, "/api/v1/events/**").hasAnyRole(
                        Role.ADMIN.name(),
                        Role.MODERATOR.name(),
                        Role.USER.name())
                .requestMatchers(HttpMethod.POST, "/api/v1/events/**").hasRole(Role.ADMIN.name())
                .requestMatchers(HttpMethod.PUT, "/api/v1/events/**").hasAnyRole(
                        Role.ADMIN.name(),
                        Role.MODERATOR.name())
                .requestMatchers(HttpMethod.DELETE, "/api/v1/events/**").hasAnyRole(
                        Role.ADMIN.name(),
                        Role.MODERATOR.name()
                )

                //***** "/api/v1/files/**"
                .requestMatchers(HttpMethod.GET, "/api/v1/files/**").hasAnyRole(
                        Role.ADMIN.name(),
                        Role.MODERATOR.name(),
                        Role.USER.name())
                .requestMatchers(HttpMethod.POST, "/api/v1/files/**").hasRole(Role.ADMIN.name())
                .requestMatchers(HttpMethod.PUT, "/api/v1/files/**").hasAnyRole(
                        Role.ADMIN.name(),
                        Role.MODERATOR.name())
                .requestMatchers(HttpMethod.DELETE, "/api/v1/files/**").hasAnyRole(
                        Role.ADMIN.name(),
                        Role.MODERATOR.name()
                )

                .anyRequest().authenticated()
        ;
        return http.build();
    };

}

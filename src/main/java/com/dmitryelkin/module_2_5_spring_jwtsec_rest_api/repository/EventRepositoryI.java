package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.repository;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.Event;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventRepositoryI extends JpaRepository<Event, Long> {
    Optional<Event> findByUser(User user);
}

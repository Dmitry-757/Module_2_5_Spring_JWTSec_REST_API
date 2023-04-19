package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.repository;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepositoryI extends JpaRepository<Event, Long> {
}

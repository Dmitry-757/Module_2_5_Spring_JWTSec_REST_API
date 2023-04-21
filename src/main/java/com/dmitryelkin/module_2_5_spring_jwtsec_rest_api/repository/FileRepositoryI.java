package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.repository;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.File;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepositoryI extends JpaRepository<File, Long> {
    Optional<File> findByName(String name);
}

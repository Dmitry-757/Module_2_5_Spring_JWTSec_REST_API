package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.repository;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepositoryI extends JpaRepository<File, Long> {
}

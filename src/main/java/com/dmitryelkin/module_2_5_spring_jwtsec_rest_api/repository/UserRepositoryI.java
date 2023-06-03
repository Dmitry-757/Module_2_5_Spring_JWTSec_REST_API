package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.repository;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.Status;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepositoryI extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);

    @Query("select u from User u where u.status = :param")
    Optional<User> findByParamStatus(@Param("param") Status param);
}

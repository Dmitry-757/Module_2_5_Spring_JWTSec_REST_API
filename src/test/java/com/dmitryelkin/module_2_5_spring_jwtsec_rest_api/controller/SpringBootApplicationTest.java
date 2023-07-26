package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

/**
 * General class for test containers.
 */
@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(initializers = {SpringBootApplicationTest.Initializer.class})
@TestPropertySource(properties = {"spring.config.location=classpath:application-properties.yml"})
public class SpringBootApplicationTest {

    private static final String DATABASE_NAME = "spring-app";

    @Container
    public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:latest").asCompatibleSubstituteFor("mysql"))
            .withReuse(true)
            .withDatabaseName(DATABASE_NAME);

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "CONTAINER.USERNAME=" + mySQLContainer.getUsername(),
                    "CONTAINER.PASSWORD=" + mySQLContainer.getPassword(),
                    "CONTAINER.URL=" + mySQLContainer.getJdbcUrl()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
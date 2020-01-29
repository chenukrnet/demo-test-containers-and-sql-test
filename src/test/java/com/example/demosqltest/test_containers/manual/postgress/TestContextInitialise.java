package com.example.demosqltest.test_containers.manual.postgress;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Map;

public class TestContextInitialise implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer()
            .withDatabaseName("pulsar")
            .withUsername("demo")
            .withPassword("password")
            ;


    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        postgreSQLContainer.start();
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        propertySources.addFirst(getOverrideDatabaseConnection());



    }

    private PropertySource<?> getOverrideDatabaseConnection() {
        Map<String, Object> of = Map.of(
                "spring.datasource.url", postgreSQLContainer.getJdbcUrl(),
                "spring.datasource.username", postgreSQLContainer.getUsername(),
                "spring.datasource.password", postgreSQLContainer.getPassword(),
                "spring.datasource.driver-class-name", postgreSQLContainer.getDriverClassName()
        );
        return new MapPropertySource("test-container", of);
    }
}

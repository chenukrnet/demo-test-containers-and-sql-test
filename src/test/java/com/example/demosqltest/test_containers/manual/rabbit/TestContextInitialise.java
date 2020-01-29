package com.example.demosqltest.test_containers.manual.rabbit;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.testcontainers.containers.RabbitMQContainer;

import java.util.Map;

public class TestContextInitialise implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final String PASSWORD = "password";
    private static final String USER = "user";
    private static final String V_HOST = "test";
    public static final String MY_QUEUE = "my_queue";
    public static final String MY_EXCHANGE = "my_exchange";
    RabbitMQContainer container = new RabbitMQContainer()
            .withVhost(V_HOST,true)
            .withQueue(MY_QUEUE)
            .withExchange(MY_EXCHANGE, "direct")
//            .withBinding(MY_EXCHANGE, MY_QUEUE)
            .withUser(USER, PASSWORD)
            .withPermission(V_HOST, USER, ".*", ".*", ".*")
            ;



    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        container.start();


        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        propertySources.addFirst(getOverrideDatabaseConnection());


    }

    private PropertySource<?> getOverrideDatabaseConnection() {
        String containerIpAddress = container.getContainerIpAddress();
        Integer amqpPort = container.getAmqpPort();

        Map<String, Object> of = Map.of(
                "spring.rabbitmq.addresses", containerIpAddress + ":" + amqpPort,
                "spring.rabbitmq.host", containerIpAddress,
                "spring.rabbitmq.port", amqpPort,
                "spring.rabbitmq.username", USER,
                "spring.rabbitmq.password", PASSWORD,
                "spring.rabbitmq.virtual-host", V_HOST
        );
        return new MapPropertySource("test-container", of);
    }
}

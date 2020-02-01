package com.example.demosqltest.test_containers.manual.generic;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.testcontainers.containers.GenericContainer;

import java.util.Map;

public class TestContextInitialise implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final String PASSWORD = "password";
    private static final String USER = "user";
    private static final String V_HOST = "test";
    public static final String MY_QUEUE = "my_queue";
    public static final String MY_EXCHANGE = "my_exchange";
    //    https://www.rabbitmq.com/management-cli.html
    GenericContainer container = new GenericContainer("rabbitmq:3.7-management-alpine")
            .withExposedPorts(5672)
            .withCommand(new String[]{
                    "rabbitmqadmin declare vhost name="+V_HOST+" tracing=true",
                    "rabbitmqadmin declare user name="+USER+ " password="+PASSWORD,
                    "rabbitmqadmin declare permission vhost=" + V_HOST +" user="+USER+" configure=.* write=.* read=.*"
            });




    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        container.start();

        magicOfCreationOnSpecialVHost();

        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        propertySources.addFirst(getOverrideDatabaseConnection());
    }

    // Есть пул риквест на добавление такой возмоности через dsl
    private void magicOfCreationOnSpecialVHost() {
        String containerIpAddress = container.getContainerIpAddress();
        Integer amqpPort = container.getMappedPort(5672);
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(containerIpAddress, amqpPort);
        connectionFactory.setVirtualHost(V_HOST);
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        AmqpAdmin rabbitAdmin= new RabbitAdmin(rabbitTemplate);
        rabbitAdmin.declareExchange(new DirectExchange(MY_EXCHANGE));
        rabbitAdmin.declareQueue(new Queue(MY_QUEUE));
        Binding binding = new Binding(MY_QUEUE, Binding.DestinationType.QUEUE, MY_EXCHANGE, MY_QUEUE, Map.of());
        rabbitAdmin.declareBinding(binding);
    }

    private PropertySource<?> getOverrideDatabaseConnection() {
        String containerIpAddress = container.getContainerIpAddress();
        Integer amqpPort = container.getMappedPort(5672);

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

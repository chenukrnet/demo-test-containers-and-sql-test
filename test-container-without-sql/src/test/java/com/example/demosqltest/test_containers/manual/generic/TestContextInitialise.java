package com.example.demosqltest.test_containers.manual.generic;

import lombok.extern.slf4j.Slf4j;
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
import org.testcontainers.containers.Container;
import org.testcontainers.containers.GenericContainer;

import java.util.List;
import java.util.Map;

@Slf4j
public class TestContextInitialise implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final String PASSWORD = "password";
    private static final String USER = "user";
    private static final String V_HOST = "test";
    public static final String MY_QUEUE = "my_queue";
    public static final String MY_EXCHANGE = "my_exchange";
    //    https://www.rabbitmq.com/management-cli.html
    GenericContainer container = new GenericContainer("rabbitmq:3.7-management-alpine")
            .withExposedPorts(5672);


    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        container.start();
        try {
            String rabbitAdminPath = "./usr/local/bin/";
            Container.ExecResult declareVHostResult = container.execInContainer(rabbitAdminPath + "rabbitmqadmin", "declare", "vhost", "name=" + V_HOST, "tracing=true");
            log.info(declareVHostResult.getStdout());
            Container.ExecResult declareUserResult = container.execInContainer(rabbitAdminPath + "rabbitmqadmin", "declare", "user", "name=" + USER, "password=" + PASSWORD, "tags=" + USER);
            log.info(declareUserResult.getStdout());
            Container.ExecResult declarePermissionResult = container.execInContainer(rabbitAdminPath + "rabbitmqadmin", "declare", "permission", "vhost=" + V_HOST, "user=" + USER, "configure=.*", "write=.*", "read=.*");
            log.info(declarePermissionResult.getStdout());
            Container.ExecResult declareExchangeResult = container.execInContainer(rabbitAdminPath + "rabbitmqadmin", "declare", "exchange", "name=" + MY_EXCHANGE, "type=direct", "--vhost=" + V_HOST);
            log.info(declareExchangeResult.getStdout());
            Container.ExecResult declareQueueResult = container.execInContainer(rabbitAdminPath + "rabbitmqadmin", "declare", "queue", "name=" + MY_QUEUE, "--vhost=" + V_HOST);
            log.info(declareQueueResult.getStdout());
            Container.ExecResult declareBindingResult = container.execInContainer(rabbitAdminPath + "rabbitmqadmin", "declare", "binding", "source=" + MY_EXCHANGE, "destination_type=queue", "destination=" + MY_QUEUE, "routing_key=" + MY_QUEUE, "--vhost=" + V_HOST);
            log.info(declareBindingResult.getStdout());
        } catch (Exception e) {
            log.error("Ошика при выполении коммант после старта контейнера",e);
            throw new RuntimeException(e);
        }

        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        propertySources.addFirst(getOverrideDatabaseConnection());
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

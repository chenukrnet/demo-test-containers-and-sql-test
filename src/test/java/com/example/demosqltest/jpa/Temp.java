//package com.example.demosqltest.jpa;
//
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.util.TestPropertyValues;
//import org.springframework.context.ApplicationContextInitializer;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.test.context.ContextConfiguration;
//import org.testcontainers.containers.RabbitMQContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//@Testcontainers
//@SpringBootTest
//@ContextConfiguration(initializers = AbstractRabbitTest.Initializer.class)
//public abstract class AbstractRabbitTest  {
//
//    @Container
//    public static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.7-management")
//            .withExposedPorts(5672)
//            .withVhost("/")
//            .withUser("admin", "admin")
//            .withPermission("/", "admin", ".*", ".*", ".*");
//
//    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
//        @Override
//        public void initialize(ConfigurableApplicationContext applicationContext) {
//            TestPropertyValues values = TestPropertyValues.of(
//                    "spring.rabbitmq.host=" + rabbitMQContainer.getContainerIpAddress(),
//                    "spring.rabbitmq.port=" + rabbitMQContainer.getMappedPort(5672),
//                    "spring.rabbitmq.username=admin",
//                    "spring.rabbitmq.password=admin"
//            );
//            values.applyTo(applicationContext);
//        }
//    }
//}
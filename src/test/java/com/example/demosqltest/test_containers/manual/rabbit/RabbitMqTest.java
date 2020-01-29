package com.example.demosqltest.test_containers.manual.rabbit;



import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("prod")
@SpringBootTest
@SpringJUnitConfig(initializers = TestContextInitialise.class)
public class RabbitMqTest {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    void testRabbitMq() {
        String expected = "expected";
        rabbitTemplate.convertAndSend(TestContextInitialise.MY_EXCHANGE, TestContextInitialise.MY_QUEUE, expected);
        Message receive = rabbitTemplate.receive(TestContextInitialise.MY_QUEUE);
        String actual = new String(receive.getBody());
        assertEquals(expected, actual);

    }




}

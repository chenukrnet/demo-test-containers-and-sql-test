package com.example.demosqltest.test_containers.manual.mock_server;


import com.example.demosqltest.service.rest.SomeRestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("prod")
@SpringBootTest
@SpringJUnitConfig(initializers = TestContextInitialise.class)
public class MockServerTest {

    @Autowired
    private SomeRestService someRestService;
 
    @Test
    public void doRest() {
        String response = someRestService.getInformationAboutStatus("TEST_REF");
        assertEquals("{\"status\":\"ok\"}", response);
    }
}

package com.example.demosqltest.test_containers.manual.mock_server;

import com.google.common.net.MediaType;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.RegexBody;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.testcontainers.containers.MockServerContainer;

import java.util.Map;

import static org.mockserver.model.JsonPathBody.jsonPath;

public class TestContextInitialise implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public static final String RELATIVE_PATH = "/api_v";
    public MockServerContainer mockServer = new MockServerContainer();


    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        setUpExpectation();
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        propertySources.addFirst(getOverrideDatabaseConnection());


    }

    private void setUpExpectation() {
        mockServer.start();
        new MockServerClient(mockServer.getContainerIpAddress(), mockServer.getServerPort())
                .when(
                        HttpRequest.request(RELATIVE_PATH).withMethod("POST")
                        .withBody(jsonPath("$.data.*"))
//                        .withBody(new RegexBody(".*"))
                ).respond(
                HttpResponse.response().withBody("{\"status\":\"ok\"}", MediaType.MANIFEST_JSON_UTF_8)

        );

        ;
    }

    private PropertySource<?> getOverrideDatabaseConnection() {
        String ipAddress = mockServer.getContainerIpAddress();
        Integer port = mockServer.getServerPort();
        Map<String, Object> of = Map.of(
                "some.service.url", "http://" + ipAddress + ":" + port + "/"+RELATIVE_PATH
        );
        return new MapPropertySource("test-container", of);
    }
}

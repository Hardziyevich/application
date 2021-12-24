package com.hardziyevich.app.controller;

import com.hardziyevich.app.dao.TestContainer;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.hardziyevich.app.controller.ConstantHttp.HttpMethod.*;
import static com.hardziyevich.app.controller.ConstantHttp.HttpResponseStatus.*;
import static com.hardziyevich.app.controller.ConstantHttp.UrlPath.RESISTOR_PATH;
import static com.hardziyevich.app.controller.ControllerFactory.flexibleResistorController;
import static com.hardziyevich.app.dao.impl.ConnectionPoolFabric.PropertiesFile.*;
import static com.hardziyevich.app.dao.impl.ConnectionPoolFabric.PropertiesFile.POOL_SIZE;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ControllerResistorTest {

    @Container
    private static final PostgreSQLContainer<TestContainer> postgreSQLContainer = TestContainer.getContainer();
    private static Server server;

    @BeforeAll
    @DisplayName("Init test")
    void start() {
        postgreSQLContainer.start();
        Map<String, String> attributes = new HashMap<>();
        attributes.put(PASSWORD_KEY, postgreSQLContainer.getPassword());
        attributes.put(USERNAME_KEY, postgreSQLContainer.getUsername());
        attributes.put(URL_KEY, postgreSQLContainer.getJdbcUrl());
        attributes.put(POOL_SIZE, "5");
        Map<String, Handler> resistorPath = new HashMap<>();
        resistorPath.put(RESISTOR_PATH, new Handler(flexibleResistorController(attributes)));
        server = new Server(resistorPath);
        server.start();
    }

    @AfterAll
    void stop() {
        server.stop();
        postgreSQLContainer.stop();
    }

    @Test
    @DisplayName("Create controller")
    void create() {
        String content = """
                {\s
                    "value":15,\s
                    "unit":"Ohm",\s
                    "power":"0.1W",\s
                    "case":"0402",\s
                    "temp-low":"-55°C",\s
                    "temp-high":"+125°C"\s
                }\s
                """;
        Client client = new Client.Builder()
                .url("http://localhost:8081/resistor/create")
                .requestMethod(POST)
                .content(content)
                .build();
        client.sendRequest();
        assertEquals(STATUS_CREATED, client.getResponseCode());
    }

    @Test
    @DisplayName("Update controller")
    void update() {
        String content = """
                {\s
                    "id":1,
                    "value":1423,\s
                    "unit":"Ohm",\s
                    "power":"0.25W"
                }\s
                """;
        Client client = new Client.Builder()
                .url("http://localhost:8081/resistor/update")
                .requestMethod(POST)
                .content(content)
                .build();
        client.sendRequest();
        assertEquals(STATUS_CREATED, client.getResponseCode());
    }

    @Test
    @DisplayName("Delete controller")
    void delete() {
        Client client = new Client.Builder()
                .url("http://localhost:8081/resistor/delete?id=2")
                .requestMethod(DELETE)
                .build();
        client.sendRequest();
        assertEquals(STATUS_NO_CONTENT, client.getResponseCode());
    }

    @Test
    @DisplayName("Search controller")
    void search() {
        Client client = new Client.Builder()
                .url("http://localhost:8081/resistor")
                .getRequestUrl("/search?value=1")
                .requestMethod(GET)
                .build();
        client.sendRequest();
        assertAll(()->{
            assertEquals("{\"unit\":\"kOhm\",\"temp-high\":\"+105°C\",\"temp-low\":\"-55°C\",\"power\":\"0.1W\",\"value\":1,\"case\":\"0603\"}",client.getResponse());
            assertEquals(STATUS_OK,client.getResponseCode());
        });
    }

}

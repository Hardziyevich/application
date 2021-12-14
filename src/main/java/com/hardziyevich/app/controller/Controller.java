package com.hardziyevich.app.controller;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.sun.net.httpserver.HttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static com.hardziyevich.app.controller.Controller.HttpMethod.*;


public abstract class Controller {


    static class HttpMethod {
        static final String GET = "GET";
        static final String POST = "POST";
        static final String DELETE = "DELETE";

        static final String REG_POST_CREATE = "/.+/create";
        static final String REG_POST_UPDATE = "/.+/update";
    }

    private static final Logger log = LoggerFactory.getLogger(Handler.class);

    public void execute(final HttpExchange httpExchange) {
        String requestMethod = httpExchange.getRequestMethod();
        String requestType = httpExchange.getRequestURI().getPath();
        JsonObject jsonObject = null;

        switch (requestMethod) {
            case GET -> search();
            case DELETE -> delete();
            case POST -> {
                if (requestType.matches(REG_POST_CREATE)) {
                    create();
                    jsonObject = writeRequestFromJson(httpExchange);
                } else if (requestType.matches(REG_POST_UPDATE)) {
                    update();
                    jsonObject = writeRequestFromJson(httpExchange);
                } else {
                    notSupportRequest(httpExchange,requestType,"Request type {} does noy support");
                }
                System.out.println(jsonObject);
                System.out.println(requestType);
            }
            default -> notSupportRequest(httpExchange, requestMethod, "Method {} does not support");
        }
    }

    JsonObject writeRequestFromJson(final HttpExchange httpExchange) {
        JsonObject deserialize = null;
        try (InputStream requestBody = httpExchange.getRequestBody();
             BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody, StandardCharsets.UTF_8))) {
            if (reader.ready()) {
                deserialize = (JsonObject) Jsoner.deserialize(reader);
            }
        } catch (IOException | JsonException e) {
            log.warn("Exception while reading request body from JSON {}", e.getMessage());
        }

        return deserialize;
    }

    void notSupportRequest(final HttpExchange httpExchange,String value,String message) {
        try {
            httpExchange.sendResponseHeaders(404, 0);
            log.info(message, value);
        } catch (IOException e) {
            log.warn("Exception while setting response headers {}", e.getMessage());
        }
    }

    abstract void delete();

    abstract void search();

    abstract void create();

    abstract void update();
}

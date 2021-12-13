package com.hardziyevich.app.controller;

import com.sun.net.httpserver.HttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class Handler {

    enum RequestType {
        CAPACITOR,
        CASE,
        TEMPERATURE,
        RESISTOR
    }

    private static final Logger log = LoggerFactory.getLogger(Handler.class);
    private static final String REG_URI_TARGET = "/";
    private static final String REG_URI_REPLACEMENT = "";

    public void handle(final HttpExchange httpExchange) {
        try {
            URI requestURI = httpExchange.getRequestURI();
            RequestType requestType = RequestType.valueOf(requestURI.toString().replace(REG_URI_TARGET,REG_URI_REPLACEMENT).toUpperCase());
            switch (requestType){
                case RESISTOR -> System.out.println(requestType);
                case CAPACITOR -> System.out.println(requestType);
                case CASE -> System.out.println(requestType);
                default -> throw new UnsupportedOperationException();
            }
            String response = """
                    {
                        "first":15,
                        "second":16
                    }""";
            httpExchange.sendResponseHeaders(200,response.getBytes(StandardCharsets.UTF_8).length);
            try (OutputStream responseBody = httpExchange.getResponseBody()) {
                responseBody.write(response.getBytes(StandardCharsets.UTF_8));
                responseBody.flush();
            }
        } catch (final Exception e) {
            handle(httpExchange, e);
        } finally {
            httpExchange.close();
        }
    }

    void handle(final HttpExchange httpExchange,final Throwable throwable) {
        log.warn("Something happened {}",throwable.getMessage());
    }
}

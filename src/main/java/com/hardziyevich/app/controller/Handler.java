package com.hardziyevich.app.controller;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.hardziyevich.app.service.Response;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class Handler {

    private static final Logger log = LoggerFactory.getLogger(Handler.class);

    private static final String REG_CAPACITOR = "/capacitor.++";
    private static final String REG_RESISTOR = "/resistor.++";

    private Controller controller;

    public void handle(final HttpExchange httpExchange) {
        try {
            URI requestURI = httpExchange.getRequestURI();
            String pathURI = requestURI.getPath();
            if(pathURI.matches(REG_CAPACITOR)){
                controller = new ControllerCapacitor();
                controller.execute(httpExchange);
            } else if(pathURI.matches(REG_RESISTOR)){
                System.out.println(pathURI);
            }

//            JsonObject deserialize;
//            try (InputStream requestBody = httpExchange.getRequestBody();
//                 BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody,StandardCharsets.UTF_8))) {
//                if(reader.ready()) {
//                    deserialize = (JsonObject) Jsoner.deserialize(reader);
//                    System.out.println(deserialize.toJson());
//                }
//            }
//            Response response = new Response.Builder()
//                    .attribute("Pasha", 13)
//                    .attribute("15", "13")
//                    .build();
//            String s = response.getJson().toJson();
//            Headers responseHeaders = httpExchange.getResponseHeaders();
//            responseHeaders.add("Content-type", "application/json");
//            httpExchange.sendResponseHeaders(200, s.getBytes(StandardCharsets.UTF_8).length);
//            try (OutputStream responseBody = httpExchange.getResponseBody()) {
//                responseBody.write(s.getBytes(StandardCharsets.UTF_8));
//                responseBody.flush();
//            }
        } catch (final Exception e) {
            handle(httpExchange, e);
        } finally {
            httpExchange.close();
        }
    }

    void handle(final HttpExchange httpExchange, final Throwable throwable) {
        log.warn("Something happened {}", (Object) throwable.getStackTrace());
    }
}

package com.hardziyevich.app.controller;

import com.sun.net.httpserver.HttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;

import static com.hardziyevich.app.controller.ConstantHttp.HttpResponseStatus.STATUS_NOT_FOUND;

public class Handler {

    private static final Logger log = LoggerFactory.getLogger(Handler.class);

    private static final String REG_CAPACITOR = "/capacitor.++";
    private static final String REG_RESISTOR = "/resistor.++";

    public void handle(final HttpExchange httpExchange) {
        try {
            URI requestURI = httpExchange.getRequestURI();
            String pathURI = requestURI.getPath();
            Controller controller;
            if (pathURI.matches(REG_CAPACITOR)) {
                controller = new ControllerCapacitor();
                controller.execute(httpExchange);
            } else if (pathURI.matches(REG_RESISTOR)) {
                controller = new ControllerResistor();
                controller.execute(httpExchange);
            }
        } catch (final Exception e) {
            handle(httpExchange, e);
        } finally {
            httpExchange.close();
        }
    }

    void handle(final HttpExchange httpExchange, final Throwable throwable) {
        log.warn("Something happened {}", (Object) throwable.getStackTrace());
        try {
            httpExchange.sendResponseHeaders(STATUS_NOT_FOUND, 0);
        } catch (IOException e) {
            log.warn("Exception while reading request body from JSON {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

package com.hardziyevich.app.controller;

import com.sun.net.httpserver.HttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.hardziyevich.app.controller.ConstantHttp.HttpResponseStatus.STATUS_NOT_FOUND;

/**
 * Provides main handler for all http request and response.
 */
public record Handler(Controller controller) {

    private static final Logger log = LoggerFactory.getLogger(Handler.class);

    /**
     * The main method process httpExchange.
     *
     * @param httpExchange a http exchange.
     */
    public void handle(final HttpExchange httpExchange) {
        try {
            controller.execute(httpExchange);
        } catch (final Exception e) {
            log.warn("Something happened {}", (Object) e.getStackTrace());
            handleException(httpExchange);
        } finally {
            httpExchange.close();
        }
    }

    private void handleException(final HttpExchange httpExchange) {
        try {
            httpExchange.sendResponseHeaders(STATUS_NOT_FOUND, 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

package com.hardziyevich.app.controller;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

import static com.hardziyevich.app.controller.ConstantHttp.HttpResponseStatus.STATUS_NOT_FOUND;

/**
 * Provides main handler for all http request and response.
 */
public record Handler(Controller controller) {

    /**
     * The main method process httpExchange.
     *
     * @param httpExchange a http exchange.
     */
    public void handle(final HttpExchange httpExchange) {
        try {
            controller.execute(httpExchange);
        } catch (final Exception e) {
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

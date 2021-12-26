package com.hardziyevich.app.controller;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.sun.net.httpserver.HttpExchange;

import java.util.List;


/**
 * {@inheritDoc}
 */
public class ControllerStopServer extends Controller{

    private Server server;

    /**
     * {@inheritDoc}
     */
    @Override
    boolean delete(HttpExchange httpExchange) {
        server.stop();
        return true;
    }

    @Override
    List<JsonObject> search(HttpExchange httpExchange) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean create(HttpExchange httpExchange) {
        throw new UnsupportedOperationException();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    boolean update(HttpExchange httpExchange) {
        throw new UnsupportedOperationException();
    }

    /**
     * Inject server for stop him
     * @param server a server
     */
    public void setServer(Server server) {
        this.server = server;
    }
}

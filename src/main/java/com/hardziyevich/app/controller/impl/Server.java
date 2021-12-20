package com.hardziyevich.app.controller.impl;

import com.hardziyevich.app.controller.Handler;
import com.hardziyevich.app.controller.MainServer;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server implements MainServer {

    private final int port;
    private String hostname;
    private int backlog;

    private Server(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    private Server(int port, int backlog) {
        this.backlog = backlog;
        this.port = port;
    }

    @Override
    public void start() {
        if (hostname == null) {
            HttpServer server = null;
            try {
                server = HttpServer.create();
                server.bind(new InetSocketAddress(port), backlog);
            } catch (IOException e) {
                e.printStackTrace();
            }
            server.createContext("/capacitor", new Handler()::handle);
            server.createContext("/resistor", new Handler()::handle);
            server.start();
        }
    }

    @Override
    public void stop() {

    }

    public static MainServer getServer(String hostname, int port) {
        return new Server(hostname, port);
    }

    public static MainServer getServer(int port, int backlog) {
        return new Server(port, backlog);
    }

}

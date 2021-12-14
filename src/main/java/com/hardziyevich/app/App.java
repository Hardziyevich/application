package com.hardziyevich.app;


import com.hardziyevich.app.controller.Handler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(8081), 0);
        server.createContext("/capacitor", new Handler()::handle);
        server.createContext("/resistor", new Handler()::handle);
        server.start();
    }
}

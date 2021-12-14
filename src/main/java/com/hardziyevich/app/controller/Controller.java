package com.hardziyevich.app.controller;

import com.sun.net.httpserver.HttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.hardziyevich.app.controller.Controller.Method.*;


public abstract class Controller {

    enum Method {
        GET,
        POST,
        DELETE
    }

    private static final Logger log = LoggerFactory.getLogger(Handler.class);

    public void execute(final HttpExchange httpExchange) {
        String requestMethod = httpExchange.getRequestMethod();
        String requestType = httpExchange.getRequestURI().getPath();
        if(GET.name().equals(requestMethod)){
            System.out.println(requestMethod);
            search();
        } else if(POST.name().equals(requestMethod)) {

            System.out.println(requestMethod);
        } else if(DELETE.name().equals(requestMethod)) {
            System.out.println(requestMethod);
            delete();
        } else {
            try {
                httpExchange.sendResponseHeaders(404,0);
                log.info("Method {} does not support",requestMethod);
            } catch (IOException e) {
                log.warn("Exception set response headers {}",e.getMessage());
            }
        }
    }
    abstract void delete();

   abstract void search();

   abstract void create();

   abstract void update();
}

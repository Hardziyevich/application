package com.hardziyevich.app;

import com.hardziyevich.app.controller.*;

import java.util.HashMap;
import java.util.Map;

import static com.hardziyevich.app.controller.ConstantHttp.UrlPath.*;

public class App 
{
    public static void main( String[] args ){
        Map<String,Handler> handlers = new HashMap<>();
        ControllerStopServer stopServer = new ControllerStopServer();
        handlers.put(CAPACITOR_PATH,new Handler(ControllerFactory.newCapacitorController()));
        handlers.put(RESISTOR_PATH,new Handler(ControllerFactory.newResistorController()));
        handlers.put(STOP_PATH,new Handler(stopServer));
        Server server = new Server(handlers);
        server.start();
        stopServer.setServer(server);
    }
}

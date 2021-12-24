package com.hardziyevich.app;

import com.hardziyevich.app.controller.ControllerFactory;
import com.hardziyevich.app.controller.Handler;
import com.hardziyevich.app.controller.Server;

import java.util.HashMap;
import java.util.Map;

import static com.hardziyevich.app.controller.ConstantHttp.UrlPath.CAPACITOR_PATH;
import static com.hardziyevich.app.controller.ConstantHttp.UrlPath.RESISTOR_PATH;

public class App 
{
    public static void main( String[] args ){
        Map<String,Handler> handlers = new HashMap<>();
        handlers.put(CAPACITOR_PATH,new Handler(ControllerFactory.newCapacitorController()));
        handlers.put(RESISTOR_PATH,new Handler(ControllerFactory.newResistorController()));
        Server server = new Server(handlers);
        server.start();
    }
}

package com.hardziyevich.app.controller;

import com.hardziyevich.app.dao.impl.CapacitorDao;
import com.hardziyevich.app.dao.impl.ResistorDao;
import com.hardziyevich.app.service.impl.ServiceCapacitor;
import com.hardziyevich.app.service.impl.ServiceResistor;

import java.util.Map;

import static com.hardziyevich.app.dao.impl.ConnectionPoolAbstract.Type.DEFAULT;
import static com.hardziyevich.app.dao.impl.ConnectionPoolAbstract.Type.FLEXIBLE;

public class ControllerFactory {

    public static Controller newResistorController(){
        return new ControllerResistor(new ServiceResistor(new ResistorDao.Builder().type(DEFAULT).build()));
    }

    public static Controller newCapacitorController(){
        return new ControllerCapacitor(new ServiceCapacitor(new CapacitorDao.Builder().type(DEFAULT).build()));
    }

    public static Controller flexibleResistorController(Map<String,String> properties){
        return new ControllerResistor(new ServiceResistor(new ResistorDao.Builder().type(FLEXIBLE).property(properties).build()));
    }

    public static Controller flexibleCapacitorController(Map<String,String> properties){
        return new ControllerCapacitor(new ServiceCapacitor(new CapacitorDao.Builder().type(DEFAULT).property(properties).build()));
    }

}

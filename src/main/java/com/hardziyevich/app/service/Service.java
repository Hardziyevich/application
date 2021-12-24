package com.hardziyevich.app.service;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.hardziyevich.app.controller.Attributes;
import com.hardziyevich.app.service.dto.CreateDto;
import com.hardziyevich.app.service.dto.UpdateDto;

import java.util.List;
import java.util.Map;

/**
 * Provides methode for processing input data from controller and send them to dao
 */
public interface Service {

    /**
     * Methode validation and send data to dao
     *
     * @param id a string
     * @return a boolean result
     */
    boolean deleteById(String id);

    /**
     * Validation dto data and send request to dao
     *
     * @param dto a request from user
     * @return a boolean result
     */
    boolean create(CreateDto dto);

    /**
     * Create request to dao and validation dto request
     *
     * @param capacitorDto a request from user
     * @return a boolean result
     */
    boolean update(UpdateDto capacitorDto);

    /**
     * Validation input data and create request to dao
     *
     * @param attributes a set of request attributes
     * @return a list of json object
     */
    List<JsonObject> search(Map<Attributes, String> attributes);

    class RegularExpression {

        public static final String REG_DIGIT = "\\d+";
        public static final String REG_UNIT_CAPACITOR = "pF|uF";
        public static final String REG_UNIT_RESISTOR = "Ohm|kOhm";
        public static final String REG_TEMP_LOW = "-\\d+\\u00B0C";
        public static final String REG_TEMP_HIGH = "\\+\\d+\\u00B0C";
    }
}

package com.hardziyevich.app.service;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.hardziyevich.app.controller.Attributes;
import com.hardziyevich.app.service.dto.CreateDto;
import com.hardziyevich.app.service.dto.UpdateDto;

import java.util.List;
import java.util.Map;

public interface Service {

    boolean deleteById(String id);

    boolean create(CreateDto dto);

    boolean update(UpdateDto capacitorDto);

    List<JsonObject> search(Map<Attributes, String> attributes);

    class RegularExpression {

        public static final String REG_DIGIT = "\\d+";
        public static final String REG_UNIT_CAPACITOR = "pF|uF";
        public static final String REG_UNIT_RESISTOR = "Ohm|kOhm";
        public static final String REG_TEMP_LOW = "-\\d+\\u00B0C";
        public static final String REG_TEMP_HIGH = "\\+\\d+\\u00B0C";
    }
}

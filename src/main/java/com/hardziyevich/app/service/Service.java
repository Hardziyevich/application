package com.hardziyevich.app.service;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.hardziyevich.app.controller.AttributesCapacitor;
import com.hardziyevich.app.service.dto.CreateCapacitorDto;
import com.hardziyevich.app.service.dto.UpdateCapacitorDto;

import java.util.List;
import java.util.Map;

public interface Service {

    boolean deleteById(String id);

    boolean create(CreateCapacitorDto capacitorDto);

    boolean update(UpdateCapacitorDto capacitorDto);

    List<JsonObject> search(Map<AttributesCapacitor, String> attributes);

    class RegularExpression {

        public static final String REG_DIGIT = "\\d+";
        public static final String REG_UNIT_CAPACITOR = "pF|uF";
        public static final String REG_TEMP_LOW = "-\\d+\\u00B0C";
        public static final String REG_TEMP_HIGH = "\\+\\d+\\u00B0C";
    }
}

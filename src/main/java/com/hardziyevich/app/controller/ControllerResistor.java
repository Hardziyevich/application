package com.hardziyevich.app.controller;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.hardziyevich.app.dao.impl.ResistorDao;
import com.hardziyevich.app.service.Service;
import com.hardziyevich.app.service.ServiceResistor;
import com.hardziyevich.app.service.dto.CreateDto;
import com.hardziyevich.app.service.dto.UpdateDto;
import com.sun.net.httpserver.HttpExchange;

import java.net.URI;
import java.util.*;

import static com.hardziyevich.app.controller.Attributes.*;
import static com.hardziyevich.app.dao.impl.ConnectionPoolAbstract.Type.DEFAULT;

public class ControllerResistor extends Controller {

    private static final Service service = ServiceResistor.getInstance(new ResistorDao.Builder().type(DEFAULT).build());

    @Override
    boolean delete(HttpExchange httpExchange) {
        URI uri = httpExchange.getRequestURI();
        Optional<String> id = readAttributes(uri, ID);
        boolean result = false;
        if (id.isPresent()) {
            result = service.deleteById(id.get());
        }
        return result;
    }

    @Override
    List<JsonObject> search(HttpExchange httpExchange) {
        URI requestURI = httpExchange.getRequestURI();
        Map<Attributes, String> attributes = new HashMap<>();
        searchAttributeUrl(requestURI, attributes);
        return service.search(attributes);
    }

    @Override
    boolean create(HttpExchange httpExchange) {
        JsonObject jsonObject = readRequestFromJson(httpExchange);
        CreateDto dto = CreateDto.builder()
                .value(jsonObject.getString(VALUE))
                .unit(jsonObject.getString(UNIT))
                .power(jsonObject.getString(POWER))
                .caseValue(jsonObject.getString(CASE))
                .tempLow(jsonObject.getString(TEMP_LOW))
                .tempHigh(jsonObject.getString(TEMP_HIGH))
                .build();
        return service.create(dto);
    }

    @Override
    boolean update(HttpExchange httpExchange) {
        JsonObject jsonObject = readRequestFromJson(httpExchange);
        UpdateDto build = UpdateDto.builder()
                .id(jsonObject.getString(ID))
                .value(jsonObject.getString(VALUE))
                .unit(jsonObject.getString(UNIT))
                .power(jsonObject.getString(POWER))
                .build();
        return service.update(build);
    }
}

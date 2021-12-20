package com.hardziyevich.app.controller;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.hardziyevich.app.service.dto.CreateCapacitorDto;
import com.hardziyevich.app.service.Service;
import com.hardziyevich.app.service.ServiceCapacitor;
import com.hardziyevich.app.service.dto.UpdateCapacitorDto;
import com.sun.net.httpserver.HttpExchange;

import java.net.URI;
import java.util.*;

import static com.hardziyevich.app.controller.AttributesCapacitor.*;

class ControllerCapacitor extends Controller {

    private final Service service = ServiceCapacitor.getInstance();

    public ControllerCapacitor() {
    }

    @Override
    boolean delete(final HttpExchange httpExchange) {
        URI uri = httpExchange.getRequestURI();
        Optional<String> id = readAttributes(uri, ID);
        boolean result = false;
        if (id.isPresent()) {
            result = service.deleteById(id.get());
        }
        return result;
    }

    @Override
    boolean create(final HttpExchange httpExchange) {
        JsonObject jsonObject = readRequestFromJson(httpExchange);
        CreateCapacitorDto capacitorDto = CreateCapacitorDto.builder()
                .value(jsonObject.getString(VALUE))
                .unit(jsonObject.getString(UNIT))
                .voltage(jsonObject.getString(VOLTAGE))
                .caseValue(jsonObject.getString(CASE))
                .tempLow(jsonObject.getString(TEMP_LOW))
                .tempHigh(jsonObject.getString(TEMP_HIGH))
                .build();

        return service.create(capacitorDto);
    }

    @Override
    boolean update(final HttpExchange httpExchange) {
        JsonObject jsonObject = readRequestFromJson(httpExchange);
        UpdateCapacitorDto build = UpdateCapacitorDto.builder()
                .id(jsonObject.getString(ID))
                .value(jsonObject.getString(VALUE))
                .unit(jsonObject.getString(UNIT))
                .voltage(jsonObject.getString(VOLTAGE))
                .build();
        return service.update(build);
    }

    @Override
    List<JsonObject> search(final HttpExchange httpExchange) {
        URI requestURI = httpExchange.getRequestURI();
        Map<AttributesCapacitor, String> attributes = new HashMap<>();
        Arrays.stream(AttributesCapacitor.values())
                .forEach(at -> {
                    Optional<String> attribute = readAttributes(requestURI, at);
                    attribute.ifPresent(p -> attributes.put(at, p));
                });

        return service.search(attributes);
    }
}

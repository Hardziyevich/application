package com.hardziyevich.app.controller;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.hardziyevich.app.service.dto.CreateDto;
import com.hardziyevich.app.service.Service;
import com.hardziyevich.app.service.dto.UpdateDto;
import com.sun.net.httpserver.HttpExchange;

import java.net.URI;
import java.util.*;

import static com.hardziyevich.app.controller.Attributes.*;

/**
 * {@inheritDoc}
 */
class ControllerCapacitor extends Controller {

    private final Service service;

    public ControllerCapacitor(final Service service) {
        this.service = service;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    boolean create(final HttpExchange httpExchange) {
        JsonObject jsonObject = readRequestFromJson(httpExchange);
        CreateDto capacitorDto = CreateDto.builder()
                .value(jsonObject.getString(VALUE))
                .unit(jsonObject.getString(UNIT))
                .voltage(jsonObject.getString(VOLTAGE))
                .caseValue(jsonObject.getString(CASE))
                .tempLow(jsonObject.getString(TEMP_LOW))
                .tempHigh(jsonObject.getString(TEMP_HIGH))
                .build();

        return service.create(capacitorDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean update(final HttpExchange httpExchange) {
        JsonObject jsonObject = readRequestFromJson(httpExchange);
        UpdateDto build = UpdateDto.builder()
                .id(jsonObject.getString(ID))
                .value(jsonObject.getString(VALUE))
                .unit(jsonObject.getString(UNIT))
                .voltage(jsonObject.getString(VOLTAGE))
                .build();
        return service.update(build);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    List<JsonObject> search(final HttpExchange httpExchange) {
        URI requestURI = httpExchange.getRequestURI();
        Map<Attributes, String> attributes = new HashMap<>();
        searchAttributeUrl(requestURI, attributes);
        return service.search(attributes);
    }

}

package com.hardziyevich.app.controller;

import com.github.akarazhev.metaconfig.extension.Validator;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.hardziyevich.app.dao.CapacitorDao;
import com.hardziyevich.app.dao.impl.CapacitorDaoImpl;
import com.hardziyevich.app.entity.Capacitors;
import com.sun.net.httpserver.HttpExchange;

import java.net.URI;
import java.util.*;

import static com.hardziyevich.app.controller.AttributesCapacitor.*;

class ControllerCapacitor extends Controller {

    private static final String REG_UNIT = "pF|uF";
    private static final String REG_TEMP_LOW = "-";
    private static final String REG_TEMP_HIGH = "+";
    private static final String REG_DIGIT = "\\d+";
    private final CapacitorDao capacitorDao;

    public ControllerCapacitor() {
        capacitorDao = new CapacitorDaoImpl();
    }

    @Override
    boolean delete(final HttpExchange httpExchange) {
        URI uri = httpExchange.getRequestURI();
        Optional<String> id = readAttributes(uri, ID);
        boolean result = false;
        if (id.isPresent()) {
            Request request = new Request.Builder()
                    .id(Long.parseLong(id.get()))
                    .build();
            result = capacitorDao.delete(request.getId());
        }
        return result;
    }

    @Override
    List<JsonObject> search(final HttpExchange httpExchange) {
        URI requestURI = httpExchange.getRequestURI();
        Request.Builder builder = new Request.Builder();
        Arrays.stream(AttributesCapacitor.values())
                .forEach(at -> {
                    Optional<String> attribute = readAttributes(requestURI, at);
                    attribute.ifPresent(p -> builder.attribute(at, p));
                });
        List<JsonObject> jsonObjects = new ArrayList<>();
        for (Capacitors search : capacitorDao.search(builder.build())) {
            JsonObject json = new Response.Builder().attribute(VALUE.getKey(), search.value())
                    .attribute(UNIT.getKey(), search.unitMeasurement())
                    .attribute(VOLTAGE.getKey(), search.voltageRated())
                    .attribute(CASE.getKey(), search.caseSize().nameInch())
                    .attribute(TEMP_HIGH.getKey(), search.temperature().highTemp())
                    .attribute(TEMP_LOW.getKey(), search.temperature().lowTemp()).build().getJson();
            jsonObjects.add(json);
        }
        return jsonObjects;
    }

    @Override
    boolean create(final HttpExchange httpExchange) {
        JsonObject jsonObject = readRequestFromJson(httpExchange);
        Request request = new Request.Builder().jsonToProperty(Validator.of(jsonObject).get()).build();
        Request validateRequest = validateRequestProperty(request);
        return capacitorDao.create(validateRequest);
    }

    @Override
    boolean update(final HttpExchange httpExchange) {
        JsonObject jsonObject = readRequestFromJson(httpExchange);
        Request request = new Request.Builder().jsonToAttribute(jsonObject).build();
        Request validateRequest = validateRequestAttributes(request);
        return validateRequest.getAttributes().entrySet().stream()
                .anyMatch(a -> ID.equals(a.getKey())) && capacitorDao.update(validateRequest);
    }

    private Request validateRequestProperty(Request request) {
        return new Request.Builder()
                .valueCapacitor(Validator.of(request.getValueCapacitor()).get())
                .unit(Validator.of(request.getUnit()).validate(u -> u.matches(REG_UNIT), "Unit type doesn`t support").get())
                .voltageRate(Validator.of(request.getVoltageRate()).get())
                .caseSize(Validator.of(request.getCaseSize()).get())
                .tempLow(Validator.of(request.getTempLow()).validate(t -> t.contains(REG_TEMP_LOW), "Temp type doesn`t support").get())
                .tempHigh(Validator.of(request.getTempHigh()).validate(t -> t.contains(REG_TEMP_HIGH), "Temp type doesn`t support").get())
                .build();
    }

    private Request validateRequestAttributes(Request request) {
        Iterator<Map.Entry<AttributesCapacitor, String>> iterator = request.getAttributes().entrySet().iterator();
        Request.Builder builder = new Request.Builder();
        while (iterator.hasNext()) {
            Map.Entry<AttributesCapacitor, String> next = iterator.next();
            switch (next.getKey()) {
                case UNIT -> builder.attribute(UNIT, Validator.of(next.getValue()).validate(u -> u.matches(REG_UNIT), "Unit type doesn`t support").get());
                case ID -> builder.attribute(ID, Validator.of(next.getValue()).validate(x -> x.matches(REG_DIGIT), "Doesn`t digit").get());
                default -> builder.attribute(next.getKey(), next.getValue());
            }
        }
        return builder.build();
    }
}

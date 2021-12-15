package com.hardziyevich.app.controller;

import com.github.akarazhev.metaconfig.extension.Validator;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.hardziyevich.app.dao.CapacitorDao;
import com.hardziyevich.app.dao.impl.CapacitorDaoImpl;
import com.sun.net.httpserver.HttpExchange;

import java.net.URI;
import java.util.Optional;

import static com.hardziyevich.app.controller.Attributes.ID;

class ControllerCapacitor extends Controller {

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
    void search(final HttpExchange httpExchange) {
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
        validateRequest.getAttributes().forEach((key, value) -> System.out.println(key + " " + value));

        validateRequest.getAttributes().entrySet().stream()
                .anyMatch(e -> {
                    Attributes.ID.getKey();
                    return false;
                });
        return false;
    }
}

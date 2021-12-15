package com.hardziyevich.app.controller;

import com.github.akarazhev.metaconfig.extension.Validator;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonKey;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.sun.net.httpserver.HttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import static com.hardziyevich.app.controller.Attributes.*;
import static com.hardziyevich.app.controller.Constant.HttpMethod.*;
import static com.hardziyevich.app.controller.Constant.HttpResponseStatus.*;


abstract class Controller {

    private static final Logger log = LoggerFactory.getLogger(Handler.class);

    static final String REG_POST_CREATE = "/.+/create";
    static final String REG_POST_UPDATE = "/.+/update";
    static final String REG_ATTRIBUTES_DELIMITER = "&";
    static final String REG_ATTRIBUTE_DELIMITER = "=";

    public void execute(final HttpExchange httpExchange) {
        String requestMethod = httpExchange.getRequestMethod();
        String requestType = httpExchange.getRequestURI().getPath();
        JsonObject jsonObject;

        switch (requestMethod) {
            case GET -> {
                search(httpExchange);
            }
            case DELETE -> {
                int typeRequest = delete(httpExchange) ? STATUS_NO_CONTENT : STATUS_NOT_FOUND;
                response(httpExchange, typeRequest, -1);
            }
            case POST -> {
                if (requestType.matches(REG_POST_CREATE)) {
                    int typeRequest = create(httpExchange) ? STATUS_CREATED : STATUS_NOT_FOUND;
                    response(httpExchange, typeRequest, -1);
                } else if (requestType.matches(REG_POST_UPDATE)) {
                    update(httpExchange);
                } else {
                    notSupportRequest(httpExchange, requestType, "Request type {} does noy support");
                }
            }
            default -> notSupportRequest(httpExchange, requestMethod, "Method {} does not support");
        }
    }

    protected Optional<String> readAttributes(final URI uri, JsonKey key) {
        final String path = uri.getQuery();
        return path != null ? Arrays.stream(path.split(REG_ATTRIBUTES_DELIMITER))
                .filter(a -> a.contains(key.getKey()))
                .map(p -> p.split(REG_ATTRIBUTE_DELIMITER)[1])
                .findFirst() : Optional.empty();
    }

    protected JsonObject readRequestFromJson(final HttpExchange httpExchange) {
        JsonObject deserialize = null;
        try (InputStream requestBody = httpExchange.getRequestBody();
             BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody, StandardCharsets.UTF_8))) {
            if (reader.ready()) {
                deserialize = (JsonObject) Jsoner.deserialize(reader);
            }
        } catch (IOException | JsonException e) {
            log.warn("Exception while reading request body from JSON {}", e.getMessage());
        }
        return deserialize;
    }

    protected Request validateRequestProperty(Request request) {
        return  new Request.Builder()
                .valueCapacitor(Validator.of(request.getValueCapacitor()).get())
                .unit(Validator.of(request.getUnit()).validate(u -> u.matches("pF|uF"), "Unit type doesn`t support").get())
                .voltageRate(Validator.of(request.getVoltageRate()).get())
                .caseSize(Validator.of(request.getCaseSize()).get())
                .tempLow(Validator.of(request.getTempLow()).validate(t -> t.contains("-"), "Temp type doesn`t support").get())
                .tempHigh(Validator.of(request.getTempHigh()).validate(t -> t.contains("+"), "Temp type doesn`t support").get())
                .build();
    }

    protected Request validateRequestAttributes(Request request) {
        Iterator<Map.Entry<Attributes, String>> iterator = request.getAttributes().entrySet().iterator();
        Request.Builder builder = new Request.Builder();
        while (iterator.hasNext()) {
            Map.Entry<Attributes, String> next = iterator.next();
            switch (next.getKey()) {
                case UNIT -> builder.attribute(UNIT,
                        Validator.of(next.getValue())
                                .validate(u -> u.matches("pF|uF"), "Unit type doesn`t support")
                                .get());
                case TEMP_LOW -> builder.attribute(TEMP_LOW,
                        Validator.of(next.getValue())
                                .validate(t -> t.contains("-"), "Temp type doesn`t support").get());
                case TEMP_HIGH -> builder.attribute(TEMP_HIGH,
                        Validator.of(next.getValue())
                                .validate(t -> t.contains("+"), "Temp type doesn`t support").get());
                default -> builder.attribute(next.getKey(), next.getValue());
            }
        }
        return builder.build();
    }

    protected void notSupportRequest(final HttpExchange httpExchange, final String value, final String message) {
        try {
            httpExchange.sendResponseHeaders(STATUS_NOT_FOUND, 0);
            log.info(message, value);
        } catch (IOException e) {
            log.warn("Exception while setting response headers {}", e.getMessage());
        }
    }

    protected void response(final HttpExchange httpExchange, final int status, final int responseLength) {
        try {
            httpExchange.sendResponseHeaders(status, responseLength);
        } catch (IOException e) {
            log.warn("Exception while setting response headers {}", e.getMessage());
        }
    }

    abstract boolean delete(final HttpExchange httpExchange);

    abstract void search(final HttpExchange httpExchange);

    abstract boolean create(final HttpExchange httpExchange);

    abstract boolean update(final HttpExchange httpExchange);
}

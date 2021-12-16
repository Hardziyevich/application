package com.hardziyevich.app.controller;

import com.github.akarazhev.metaconfig.extension.Validator;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonKey;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.hardziyevich.app.controller.Constant.HttpMethod.*;
import static com.hardziyevich.app.controller.Constant.HttpResponseStatus.*;


abstract class Controller {

    private static final Logger log = LoggerFactory.getLogger(Handler.class);

    static final String REG_POST_CREATE = "/.+/create";
    static final String REG_POST_UPDATE = "/.+/update";
    static final String REG_ATTRIBUTES_DELIMITER = "&";
    static final String REG_ATTRIBUTE_DELIMITER = "=";

    public void execute(final HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        String requestType = httpExchange.getRequestURI().getPath();
        int typeRequest;

        switch (requestMethod) {
            case GET -> {
                List<JsonObject> search = search(httpExchange);
                writeResponse(httpExchange, search);
            }
            case DELETE -> {
                typeRequest = delete(httpExchange) ? STATUS_NO_CONTENT : STATUS_NOT_FOUND;
                response(httpExchange, typeRequest, -1);
            }
            case POST -> {
                if (requestType.matches(REG_POST_CREATE)) {
                    typeRequest = create(httpExchange) ? STATUS_CREATED : STATUS_NOT_FOUND;
                    response(httpExchange, typeRequest, -1);
                } else if (requestType.matches(REG_POST_UPDATE)) {
                    typeRequest = update(httpExchange) ? STATUS_CREATED : STATUS_NOT_FOUND;
                    response(httpExchange, typeRequest, -1);
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

    void writeResponse(final HttpExchange httpExchange, List<JsonObject> jsonObjects) throws IOException {
        Headers responseHeaders = httpExchange.getResponseHeaders();
        StringBuilder db = new StringBuilder();
        for (JsonObject jsonObject : jsonObjects) {
            db.append(jsonObject.toJson());
        }
        System.out.println(db);
        responseHeaders.add("Content-type", "application/json");
        response(httpExchange, STATUS_OK, db.toString().getBytes(StandardCharsets.UTF_8).length);
        try (OutputStream responseBody = httpExchange.getResponseBody()) {
            responseBody.write(db.toString().getBytes(StandardCharsets.UTF_8));
            responseBody.flush();
        }
    }

    abstract boolean delete(final HttpExchange httpExchange);

    abstract List<JsonObject> search(final HttpExchange httpExchange);

    abstract boolean create(final HttpExchange httpExchange);

    abstract boolean update(final HttpExchange httpExchange);
}

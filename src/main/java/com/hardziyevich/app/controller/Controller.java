package com.hardziyevich.app.controller;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonKey;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.hardziyevich.app.controller.ConstantHttp.HttpMethod.*;
import static com.hardziyevich.app.controller.ConstantHttp.HttpResponseStatus.*;

/**
 * Provides methods CRUD and support methods for processing http request and response.
 */
public abstract class Controller {

    static final String REG_POST_CREATE = "/.+/create";
    static final String REG_POST_UPDATE = "/.+/update";
    static final String REG_ATTRIBUTES_DELIMITER = "&";
    static final String REG_ATTRIBUTE_DELIMITER = "=";

    /**
     * Processing a http request for delete element in database.
     *
     * @param httpExchange a http exchange.
     * @return a boolean result.
     */
    abstract boolean delete(final HttpExchange httpExchange);

    /**
     * Processing a http request for search element in database.
     *
     * @param httpExchange a http exchange.
     * @return a list of json objects.
     */
    abstract List<JsonObject> search(final HttpExchange httpExchange);

    /**
     * Processing a http request for create element in database.
     *
     * @param httpExchange a http exchange.
     * @return a boolean result.
     */
    abstract boolean create(final HttpExchange httpExchange);

    /**
     * Processing a http request for update element in database.
     *
     * @param httpExchange a http exchange.
     * @return a boolean result.
     */
    abstract boolean update(final HttpExchange httpExchange);

    /**
     * The main method that processing request method and delivering particular request in right method.
     *
     * @param httpExchange a http exchange.
     * @throws IOException when a controller encounters a problem.
     */
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

    /**
     * The method reads attributes from url and parsing their.
     *
     * @param uri a url.
     * @param key a particular json attribute from attributes.
     * @return optional string attribute.
     */
    protected Optional<String> readAttributes(final URI uri, JsonKey key) {
        final String path = uri.getQuery();
        return path != null ? Arrays.stream(path.split(REG_ATTRIBUTES_DELIMITER))
                .filter(a -> a.contains(key.getKey()))
                .map(p -> p.split(REG_ATTRIBUTE_DELIMITER)[1])
                .findFirst() : Optional.empty();
    }

    /**
     * The method reads request from httpExchange and creates JsonObject.
     *
     * @param httpExchange a http exchange.
     * @return a json object.
     */
    protected JsonObject readRequestFromJson(final HttpExchange httpExchange) {
        JsonObject deserialize = null;
        try (InputStream requestBody = httpExchange.getRequestBody();
             BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody, StandardCharsets.UTF_8))) {
            if (reader.ready()) {
                deserialize = (JsonObject) Jsoner.deserialize(reader);
            }
        } catch (IOException | JsonException e) {
            throw new RuntimeException(e);
        }
        return deserialize;
    }

    /**
     * The method search attributes from url.
     *
     * @param requestURI a url.
     * @param attributes a container for attributes.
     */
    protected void searchAttributeUrl(URI requestURI, Map<Attributes, String> attributes) {
        Arrays.stream(Attributes.values())
                .forEach(at -> {
                    Optional<String> attribute = readAttributes(requestURI, at);
                    attribute.ifPresent(p -> attributes.put(at, p));
                });
    }

    private void response(final HttpExchange httpExchange, final int status, final int responseLength) {
        try {
            httpExchange.sendResponseHeaders(status, responseLength);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeResponse(final HttpExchange httpExchange, List<JsonObject> jsonObjects) throws IOException {
        Headers responseHeaders = httpExchange.getResponseHeaders();
        StringBuilder db = new StringBuilder();
        for (JsonObject jsonObject : jsonObjects) {
            db.append(jsonObject.toJson());
        }
        responseHeaders.add("Content-type", "application/json");
        response(httpExchange, STATUS_OK, db.toString().getBytes(StandardCharsets.UTF_8).length);
        try (OutputStream responseBody = httpExchange.getResponseBody()) {
            responseBody.write(db.toString().getBytes(StandardCharsets.UTF_8));
            responseBody.flush();
        }
    }

    private void notSupportRequest(final HttpExchange httpExchange, final String value, final String message) {
        try {
            httpExchange.sendResponseHeaders(STATUS_NOT_FOUND, 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

package com.hardziyevich.app.service;

import com.github.cliftonlabs.json_simple.JsonObject;

import java.util.HashMap;
import java.util.Map;

public final class Response {
    private final Map<String, Object> attributes;

    private Response(Builder builder) {
        this.attributes = builder.attributes;
    }

    public JsonObject getJson(){
        var json = new JsonObject();
        json.putAllChain(attributes);
        return json;
    }

    public static class Builder {

        private final Map<String, Object> attributes = new HashMap<>();

        public Builder attribute(String name, Object value) {
            attributes.put(name, value);
            return this;
        }

        public Response build() {
            return new Response(this);
        }
    }
}

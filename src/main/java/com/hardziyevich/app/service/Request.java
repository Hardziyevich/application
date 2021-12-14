package com.hardziyevich.app.service;

import java.util.HashMap;
import java.util.Map;

public class Request {

    private final Map<String, Object> attributes;

    public Request(Builder builder) {
        this.attributes = builder.attributes;
    }


    public static class Builder {
        private long id;
        private String caseSize;
        private String tempLow;
        private String tempHigh;
        private double valueCapacitor;
        private String unit;
        private String voltageRate;
        private final Map<String, Object> attributes = new HashMap<>();

        public Builder attribute(String name, Object value) {
            attributes.put(name, value);
            return this;
        }

        public Request build() {
            return new Request(this);
        }
    }
}

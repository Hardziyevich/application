package com.hardziyevich.app.controller;

import com.github.cliftonlabs.json_simple.JsonObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.hardziyevich.app.controller.Attributes.*;

public final class Request {
    private final long id;
    private final String caseSize;
    private final String tempLow;
    private final String tempHigh;
    private final int valueCapacitor;
    private final String unit;
    private final String voltageRate;

    private final Map<Attributes, String> attributes;

    public Request(Builder builder) {
        this.attributes = builder.attributes;
        this.id = builder.id;
        this.caseSize = builder.caseSize;
        this.tempHigh = builder.tempHigh;
        this.tempLow = builder.tempLow;
        this.valueCapacitor = builder.valueCapacitor;
        this.unit = builder.unit;
        this.voltageRate = builder.voltageRate;
    }

    public long getId() {
        return id;
    }

    public String getCaseSize() {
        return caseSize;
    }

    public String getTempLow() {
        return tempLow;
    }

    public String getTempHigh() {
        return tempHigh;
    }

    public int getValueCapacitor() {
        return valueCapacitor;
    }

    public String getUnit() {
        return unit;
    }

    public String getVoltageRate() {
        return voltageRate;
    }

    public Map<Attributes, String> getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", caseSize='" + caseSize + '\'' +
                ", tempLow='" + tempLow + '\'' +
                ", tempHigh='" + tempHigh + '\'' +
                ", valueCapacitor=" + valueCapacitor +
                ", unit='" + unit + '\'' +
                ", voltageRate='" + voltageRate + '\'' +
                '}';
    }

    public static class Builder {
        private long id;
        private String caseSize;
        private String tempLow;
        private String tempHigh;
        private int valueCapacitor;
        private String unit;
        private String voltageRate;
        private final Map<Attributes, String> attributes = new HashMap<>();

        public Builder attribute(Attributes key, String value) {
            attributes.put(key, value);
            return this;
        }

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder caseSize(String caseSize) {
            this.caseSize = caseSize;
            return this;
        }

        public Builder tempLow(String tempLow) {
            this.tempLow = tempLow;
            return this;
        }

        public Builder tempHigh(String tempHigh) {
            this.tempHigh = tempHigh;
            return this;
        }

        public Builder valueCapacitor(int valueCapacitor) {
            this.valueCapacitor = valueCapacitor;
            return this;
        }

        public Builder unit(String unit) {
            this.unit = unit;
            return this;
        }

        public Builder voltageRate(String voltageRate) {
            this.voltageRate = voltageRate;
            return this;
        }

        public Builder jsonToAttribute(JsonObject jsonObject) {
            Arrays.stream(Attributes.values())
                    .filter(a -> jsonObject.getString(a) != null)
                    .forEach(a -> attributes.put(a, jsonObject.getString(a)));
            return this;
        }

        public Builder jsonToProperty(JsonObject jsonObject) {
            valueCapacitor = jsonObject.getInteger(VALUE);
            unit = jsonObject.getString(UNIT);
            voltageRate = jsonObject.getString(VOLTAGE);
            caseSize = jsonObject.getString(CASE);
            tempHigh = jsonObject.getString(TEMP_HIGH);
            tempLow = jsonObject.getString(TEMP_LOW);
            return this;
        }

        public Request build() {
            return new Request(this);
        }
    }
}

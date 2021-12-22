package com.hardziyevich.app.controller;

import com.github.cliftonlabs.json_simple.JsonKey;

public enum Attributes implements JsonKey {

    ID,
    VALUE,
    UNIT,
    POWER,
    VOLTAGE,
    CASE,
    TEMP_LOW,
    TEMP_HIGH;

    private static final String REG_UNDERSCORE = "_";
    private static final String REG_SCORE = "-";

    @Override
    public String getKey() {
        return this.name().toLowerCase().replaceAll(REG_UNDERSCORE, REG_SCORE);
    }

    @Override
    public Object getValue() {
        throw new UnsupportedOperationException();
    }
}

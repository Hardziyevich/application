package com.hardziyevich.app.entity;

public record Resistors(long id, int value, String unitMeasurement, String power, CaseSize caseSize,
                        OperatingTemperature temperature) {
}

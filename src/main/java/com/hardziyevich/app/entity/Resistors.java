package com.hardziyevich.app.entity;

public record Resistors(long id, int value, String initMeasurement, String power, CaseSize caseSize,
                        OperatingTemperature temperature) {
}

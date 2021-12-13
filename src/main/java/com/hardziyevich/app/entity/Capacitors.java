package com.hardziyevich.app.entity;

public record Capacitors(long id, int value, String initMeasurement, String voltageRated, CaseSize caseSize,
                         OperatingTemperature temperature) {
}

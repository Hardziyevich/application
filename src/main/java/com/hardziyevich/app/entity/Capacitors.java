package com.hardziyevich.app.entity;

public record Capacitors(long id, int value, String unitMeasurement, String voltageRated, CaseSize caseSize,
                         OperatingTemperature temperature) {
}

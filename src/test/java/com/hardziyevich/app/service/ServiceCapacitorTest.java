package com.hardziyevich.app.service;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.hardziyevich.app.controller.Attributes;
import com.hardziyevich.app.dao.ElementDao;
import com.hardziyevich.app.dao.JdbcSpecification;
import com.hardziyevich.app.dao.impl.CapacitorDao;
import com.hardziyevich.app.dao.impl.CapacitorsSpecification;
import com.hardziyevich.app.dao.impl.ResistorsSpecification;
import com.hardziyevich.app.entity.Capacitors;
import com.hardziyevich.app.entity.Resistors;
import com.hardziyevich.app.service.dto.CreateDto;
import com.hardziyevich.app.service.dto.UpdateDto;
import com.hardziyevich.app.service.impl.ServiceCapacitor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hardziyevich.app.controller.Attributes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ServiceCapacitorTest {

    private static final ElementDao<Capacitors> elementDao = Mockito.mock(CapacitorDao.class);
    private static final Service service = new ServiceCapacitor(elementDao);
    private static final JdbcSpecification<Capacitors> spec = Mockito.mock(CapacitorsSpecification.class);

    @Test
    @DisplayName("Delete capacitor by correct id")
    void deleteById() {
        Mockito.doReturn(true).when(elementDao).delete(4);
        assertTrue(service.deleteById("4"));
    }

    @Test
    @DisplayName("Delete capacitor by wrong id")
    void delete() {
        assertAll(() ->
        {
            assertFalse(service.deleteById(null));
            assertFalse(service.deleteById("not digit"));
        });
    }

    @Test
    @DisplayName("Create capacitor by wrong Dto")
    void createWrongResult() {
        CreateDto value = CreateDto.builder()
                .value("not digit")
                .unit("uF")
                .tempLow("-55°C")
                .tempHigh("+55°C")
                .voltage("25V")
                .build();
        CreateDto unit = CreateDto.builder()
                .value("10")
                .unit("not unit")
                .tempLow("-55°C")
                .tempHigh("+55°C")
                .voltage("25V")
                .build();
        CreateDto tempLow = CreateDto.builder()
                .value("10")
                .unit("Ohm")
                .tempLow("not temp")
                .tempHigh("+55°C")
                .voltage("25V")
                .build();
        CreateDto tempHigh = CreateDto.builder()
                .value("10")
                .unit("Ohm")
                .tempLow("-55°C")
                .tempHigh("not temp")
                .voltage("25V")
                .build();
        CreateDto voltage = CreateDto.builder()
                .value("123")
                .unit("Ohm")
                .tempLow("-55°C")
                .voltage("not voltage")
                .tempHigh("+55°C")
                .build();

        CreateDto nullPower = CreateDto.builder()
                .value("123")
                .unit("Ohm")
                .tempLow("-55°C")
                .tempHigh("+55°C")
                .build();

        assertAll(() -> {
            assertFalse(service.create(value));
            assertFalse(service.create(unit));
            assertFalse(service.create(tempHigh));
            assertFalse(service.create(tempLow));
            assertFalse(service.create(null));
            assertFalse(service.create(voltage));
            assertFalse(service.create(nullPower));
        });
    }

    @Test
    @DisplayName("Create capacitor by correct Dto")
    void createNormalResult() {
        CreateDto dto = CreateDto.builder()
                .value("10")
                .unit("uF")
                .voltage("6.3V")
                .tempLow("-55°C")
                .tempHigh("+55°C")
                .build();
        Mockito.doReturn(true).when(elementDao).create(dto);
        assertTrue(service.create(dto));
    }

    @Test
    @DisplayName("Update capacitor by wrong result")
    void updateWrong() {
        UpdateDto id = UpdateDto.builder()
                .id("not id")
                .value("uF")
                .unit("uF")
                .voltage("6.3V")
                .build();
        UpdateDto value = UpdateDto.builder()
                .id("1")
                .value("not value")
                .unit("uF")
                .voltage("6.3V")
                .build();
        UpdateDto unit = UpdateDto.builder()
                .id("1")
                .value("10")
                .unit("not unit")
                .voltage("6.3V")
                .build();
        UpdateDto voltage = UpdateDto.builder()
                .id("1")
                .value("10")
                .unit("uF")
                .voltage("not voltage")
                .build();
        assertAll(() -> {
            assertFalse(service.update(id));
            assertFalse(service.update(value));
            assertFalse(service.update(unit));
            assertFalse(service.update(null));
            assertFalse(service.update(voltage));
        });
    }

    @Test
    @DisplayName("Update capacitor by correct result")
    void updateCorrect() {
        UpdateDto test = UpdateDto.builder()
                .id("1")
                .value("10")
                .unit("uF")
                .voltage("25V")
                .build();
        Mockito.doReturn(true).when(elementDao).update(test);
        assertTrue(service.update(test));
    }

    @Test
    @DisplayName("Search capacitor by correct result")
    void searchCorrect() {
        Map<Attributes, String> test = new HashMap<>();
        List<JsonObject> result = new ArrayList<>();
        List<Capacitors> capacitors = new ArrayList<>();
        test.put(ID, "10");
        test.put(VALUE, "10");
        test.put(UNIT, "uF");
        Mockito.doReturn(capacitors).when(elementDao).search(spec);
        assertEquals(result, service.search(test));
    }

    @Test
    @DisplayName("Search capacitor by wrong id ")
    void searchWrongId() {
        Map<Attributes,String> test = new HashMap<>();
        List<JsonObject> result = new ArrayList<>();
        List<Capacitors> capacitors = new ArrayList<>();
        test.put(ID,"test");
        Mockito.doReturn(capacitors).when(elementDao).search(spec);
        assertEquals(result,service.search(test));
    }

    @Test
    @DisplayName("Search capacitor by wrong value")
    void searchWrongValue() {
        Map<Attributes,String> test = new HashMap<>();
        List<JsonObject> result = new ArrayList<>();
        List<Capacitors> capacitors = new ArrayList<>();
        test.put(VALUE,"test");
        Mockito.doReturn(capacitors).when(elementDao).search(spec);
        assertEquals(result,service.search(test));
    }

    @Test
    @DisplayName("Search capacitor by wrong unit")
    void searchWrongUnit() {
        Map<Attributes,String> test = new HashMap<>();
        List<JsonObject> result = new ArrayList<>();
        List<Capacitors> capacitors = new ArrayList<>();
        test.put(UNIT,"test");
        Mockito.doReturn(capacitors).when(elementDao).search(spec);
        assertEquals(result,service.search(test));
    }

    @Test
    @DisplayName("Search capacitor by wrong power")
    void searchWrongPower() {
        Map<Attributes,String> test = new HashMap<>();
        List<JsonObject> result = new ArrayList<>();
        List<Capacitors> capacitors = new ArrayList<>();
        test.put(POWER,"test");
        Mockito.doReturn(capacitors).when(elementDao).search(spec);
        assertEquals(result,service.search(test));
    }
    @Test
    @DisplayName("Search capacitor by null")
    void searchNull() {
        List<JsonObject> result = new ArrayList<>();
        List<Capacitors> capacitors = new ArrayList<>();
        Mockito.doReturn(capacitors).when(elementDao).search(spec);
        assertEquals(result,service.search(null));
    }

    @Test
    @DisplayName("Search capacitor by null power")
    void searchAttributeNullPower() {
        Map<Attributes,String> test = new HashMap<>();
        List<JsonObject> result = new ArrayList<>();
        List<Capacitors> capacitors = new ArrayList<>();
        test.put(POWER,null);
        Mockito.doReturn(capacitors).when(elementDao).search(spec);
        assertEquals(result,service.search(test));
    }

    @Test
    @DisplayName("Search capacitor by null id")
    void searchAttributeNullId() {
        Map<Attributes,String> test = new HashMap<>();
        List<JsonObject> result = new ArrayList<>();
        List<Capacitors> capacitors = new ArrayList<>();
        test.put(ID,null);
        Mockito.doReturn(capacitors).when(elementDao).search(spec);
        assertEquals(result,service.search(test));
    }

    @Test
    @DisplayName("Search capacitor by null value")
    void searchAttributeNullValue() {
        Map<Attributes,String> test = new HashMap<>();
        List<JsonObject> result = new ArrayList<>();
        List<Capacitors> capacitors = new ArrayList<>();
        test.put(VALUE,null);
        Mockito.doReturn(capacitors).when(elementDao).search(spec);
        assertEquals(result,service.search(test));
    }

    @Test
    @DisplayName("Search capacitor by null unit")
    void searchAttributeNullUnit() {
        Map<Attributes,String> test = new HashMap<>();
        List<JsonObject> result = new ArrayList<>();
        List<Capacitors> capacitors = new ArrayList<>();
        test.put(UNIT,null);
        Mockito.doReturn(capacitors).when(elementDao).search(spec);
        assertEquals(result,service.search(test));
    }
}

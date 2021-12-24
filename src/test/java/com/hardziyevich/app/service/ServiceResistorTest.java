package com.hardziyevich.app.service;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.hardziyevich.app.controller.Attributes;
import com.hardziyevich.app.dao.ElementDao;
import com.hardziyevich.app.dao.JdbcSpecification;
import com.hardziyevich.app.dao.impl.ResistorDao;
import com.hardziyevich.app.dao.impl.ResistorsSpecification;
import com.hardziyevich.app.entity.Resistors;
import com.hardziyevich.app.service.dto.CreateDto;
import com.hardziyevich.app.service.dto.UpdateDto;
import com.hardziyevich.app.service.impl.ServiceResistor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hardziyevich.app.controller.Attributes.*;
import static org.junit.jupiter.api.Assertions.*;

public class ServiceResistorTest {

    private static final ElementDao<Resistors> elementDao = Mockito.mock(ResistorDao.class);
    private static final Service service = new ServiceResistor(elementDao);
    private static final JdbcSpecification<Resistors> spec = Mockito.mock(ResistorsSpecification.class);

    @Test
    @DisplayName("Delete resistor by correct id")
    void deleteById() {
        Mockito.doReturn(true).when(elementDao).delete(4);
        assertTrue(service.deleteById("4"));
    }

    @Test
    @DisplayName("Delete resistor by wrong id")
    void delete() {
        assertAll(() ->
        {
            assertFalse(service.deleteById(null));
            assertFalse(service.deleteById("not digit"));
        });
    }

    @Test
    @DisplayName("Create resistor by wrong Dto")
    void createWrongResult() {
        CreateDto value = CreateDto.builder()
                .value("not digit")
                .unit("Ohm")
                .tempLow("-55°C")
                .tempHigh("+55°C")
                .power("0.1W")
                .build();
        CreateDto unit = CreateDto.builder()
                .value("10")
                .unit("not unit")
                .tempLow("-55°C")
                .tempHigh("+55°C")
                .power("0.1W")
                .build();
        CreateDto tempLow = CreateDto.builder()
                .value("10")
                .unit("Ohm")
                .tempLow("not temp")
                .tempHigh("+55°C")
                .power("0.1W")
                .build();
        CreateDto tempHigh = CreateDto.builder()
                .value("10")
                .unit("Ohm")
                .tempLow("-55°C")
                .tempHigh("not temp")
                .power("0.1W")
                .build();
        CreateDto power = CreateDto.builder()
                .value("123")
                .unit("Ohm")
                .tempLow("-55°C")
                .power("wrong")
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
            assertFalse(service.create(power));
            assertFalse(service.create(nullPower));
        });
    }

    @Test
    @DisplayName("Create resistor by correct Dto")
    void createNormalResult() {
        CreateDto dto = CreateDto.builder()
                .value("10")
                .unit("Ohm")
                .power("0.1W")
                .tempLow("-55°C")
                .tempHigh("+55°C")
                .build();
        Mockito.doReturn(true).when(elementDao).create(dto);
        assertTrue(service.create(dto));
    }

    @Test
    @DisplayName("Update resistor by wrong result")
    void updateWrong() {
        UpdateDto id = UpdateDto.builder()
                .id("wrong")
                .value("10")
                .unit("Ohm")
                .power("0.1W")
                .build();
        UpdateDto value = UpdateDto.builder()
                .id("1")
                .value("wrong")
                .unit("Ohm")
                .power("0.1W")
                .build();
        UpdateDto unit = UpdateDto.builder()
                .id("1")
                .value("10")
                .unit("wrong")
                .power("0.1W")
                .build();
        UpdateDto power = UpdateDto.builder()
                .id("1")
                .value("10")
                .unit("wrong")
                .power("0.1W")
                .build();
        assertAll(() -> {
            assertFalse(service.update(id));
            assertFalse(service.update(value));
            assertFalse(service.update(unit));
            assertFalse(service.update(null));
            assertFalse(service.update(power));
        });
    }

    @Test
    @DisplayName("Update resistor by correct result")
    void updateCorrect() {
        UpdateDto test = UpdateDto.builder()
                .id("10")
                .value("10")
                .unit("Ohm")
                .power("0.1W")
                .build();
        Mockito.doReturn(true).when(elementDao).update(test);
        assertTrue(service.update(test));
    }

    @Test
    @DisplayName("Search resistor by correct result")
    void searchCorrect() {
        Map<Attributes,String> test = new HashMap<>();
        List<JsonObject> result = new ArrayList<>();
        List<Resistors> resistors = new ArrayList<>();
        test.put(ID,"10");
        test.put(VALUE,"10");
        test.put(UNIT,"kOhm");
        Mockito.doReturn(resistors).when(elementDao).search(spec);
        assertEquals(result,service.search(test));
    }

    @Test
    @DisplayName("Search resistor by wrong id ")
    void searchWrongId() {
        Map<Attributes,String> test = new HashMap<>();
        List<JsonObject> result = new ArrayList<>();
        List<Resistors> resistors = new ArrayList<>();
        test.put(ID,"test");
        Mockito.doReturn(resistors).when(elementDao).search(spec);
        assertEquals(result,service.search(test));
    }

    @Test
    @DisplayName("Search resistor by wrong value")
    void searchWrongValue() {
        Map<Attributes,String> test = new HashMap<>();
        List<JsonObject> result = new ArrayList<>();
        List<Resistors> resistors = new ArrayList<>();
        test.put(VALUE,"test");
        Mockito.doReturn(resistors).when(elementDao).search(spec);
        assertEquals(result,service.search(test));
    }

    @Test
    @DisplayName("Search resistor by wrong unit")
    void searchWrongUnit() {
        Map<Attributes,String> test = new HashMap<>();
        List<JsonObject> result = new ArrayList<>();
        List<Resistors> resistors = new ArrayList<>();
        test.put(UNIT,"test");
        Mockito.doReturn(resistors).when(elementDao).search(spec);
        assertEquals(result,service.search(test));
    }

    @Test
    @DisplayName("Search resistor by wrong power")
    void searchWrongPower() {
        Map<Attributes,String> test = new HashMap<>();
        List<JsonObject> result = new ArrayList<>();
        List<Resistors> resistors = new ArrayList<>();
        test.put(POWER,"test");
        Mockito.doReturn(resistors).when(elementDao).search(spec);
        assertEquals(result,service.search(test));
    }
    @Test
    @DisplayName("Search resistor by null")
    void searchNull() {
        List<JsonObject> result = new ArrayList<>();
        List<Resistors> resistors = new ArrayList<>();
        Mockito.doReturn(resistors).when(elementDao).search(spec);
        assertEquals(result,service.search(null));
    }

    @Test
    @DisplayName("Search resistor by null power")
    void searchAttributeNullPower() {
        Map<Attributes,String> test = new HashMap<>();
        List<JsonObject> result = new ArrayList<>();
        List<Resistors> resistors = new ArrayList<>();
        test.put(POWER,null);
        Mockito.doReturn(resistors).when(elementDao).search(spec);
        assertEquals(result,service.search(test));
    }

    @Test
    @DisplayName("Search resistor by null id")
    void searchAttributeNullId() {
        Map<Attributes,String> test = new HashMap<>();
        List<JsonObject> result = new ArrayList<>();
        List<Resistors> resistors = new ArrayList<>();
        test.put(ID,null);
        Mockito.doReturn(resistors).when(elementDao).search(spec);
        assertEquals(result,service.search(test));
    }

    @Test
    @DisplayName("Search resistor by null value")
    void searchAttributeNullValue() {
        Map<Attributes,String> test = new HashMap<>();
        List<JsonObject> result = new ArrayList<>();
        List<Resistors> resistors = new ArrayList<>();
        test.put(VALUE,null);
        Mockito.doReturn(resistors).when(elementDao).search(spec);
        assertEquals(result,service.search(test));
    }

    @Test
    @DisplayName("Search resistor by null unit")
    void searchAttributeNullUnit() {
        Map<Attributes,String> test = new HashMap<>();
        List<JsonObject> result = new ArrayList<>();
        List<Resistors> resistors = new ArrayList<>();
        test.put(UNIT,null);
        Mockito.doReturn(resistors).when(elementDao).search(spec);
        assertEquals(result,service.search(test));
    }
}

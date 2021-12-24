package com.hardziyevich.app.service;

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

import static org.junit.jupiter.api.Assertions.*;

public class ServiceResistorTest {

    private static final ElementDao<Resistors> elementDao = Mockito.mock(ResistorDao.class);
    private static final Service service = ServiceResistor.getInstance(elementDao);
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
                .build();
        CreateDto unit = CreateDto.builder()
                .value("10")
                .unit("not unit")
                .tempLow("-55°C")
                .tempHigh("+55°C")
                .build();
        CreateDto tempLow = CreateDto.builder()
                .value("10")
                .unit("Ohm")
                .tempLow("not temp")
                .tempHigh("+55°C")
                .build();
        CreateDto tempHigh = CreateDto.builder()
                .value("not digit")
                .unit("Ohm")
                .tempLow("-55°C")
                .tempHigh("not temp")
                .build();

        assertAll(() -> {
            assertFalse(service.create(value));
            assertFalse(service.create(unit));
            assertFalse(service.create(tempHigh));
            assertFalse(service.create(tempLow));
            assertFalse(service.create(null));
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
        assertAll(() -> {
            assertFalse(service.update(id));
            assertFalse(service.update(value));
            assertFalse(service.update(unit));
            assertFalse(service.update(null));
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
/*
    @Test
    @DisplayName("Search resistor by correct result")
    void searchCorrect() {
        Map<Attributes,String> test = new HashMap<>();
        List<JsonObject> resultMock = new ArrayList<>();
        JsonObject jsonObject = new JsonObject();
        jsonObject.put(ID,"test");
        test.put(ID,"10");
        test.put(VALUE,"10");
        test.put(UNIT,"kOhm");
        resultMock.add(jsonObject);
        Mockito.doReturn(resultMock).when(elementDao).search(spec);
        System.out.println(service.search(test));
    }
*/

//    @Test
//    @DisplayName("Resistor search ")
}

package com.hardziyevich.app.dao;

import com.hardziyevich.app.controller.Attributes;
import com.hardziyevich.app.dao.impl.ResistorDao;
import com.hardziyevich.app.dao.impl.ResistorsSpecification;
import com.hardziyevich.app.entity.CaseSize;
import com.hardziyevich.app.entity.OperatingTemperature;
import com.hardziyevich.app.entity.Resistors;
import com.hardziyevich.app.service.dto.CreateDto;
import com.hardziyevich.app.service.dto.UpdateDto;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.Map;

import static com.hardziyevich.app.dao.impl.ConnectionPoolAbstract.Type.FLEXIBLE;
import static com.hardziyevich.app.dao.impl.ConnectionPoolFabric.PropertiesFile.*;
import static com.hardziyevich.app.dao.impl.ConnectionPoolFabric.PropertiesFile.POOL_SIZE;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ResistorDaoTest {

    @Container
    private static final PostgreSQLContainer<TestContainer> postgreSQLContainer = TestContainer.getContainer();
    private static ElementDao<Resistors> resistors;

    @BeforeAll
    @DisplayName("Create db")
    void load() {
        postgreSQLContainer.start();
        Map<String, String> attributes = new HashMap<>();
        attributes.put(PASSWORD_KEY, postgreSQLContainer.getPassword());
        attributes.put(USERNAME_KEY, postgreSQLContainer.getUsername());
        attributes.put(URL_KEY, postgreSQLContainer.getJdbcUrl());
        attributes.put(POOL_SIZE, "5");
        resistors = new ResistorDao.Builder().type(FLEXIBLE).property(attributes).build();
    }

    @AfterAll
    void stop() {
        postgreSQLContainer.stop();
    }


    @Test
    @DisplayName("Search resistors by id")
    void searchId() {
        Resistors resistor = new Resistors(1, 50, "Ohm", "0.1W",
                new CaseSize(1, "0402", 0.4, 0.2),
                new OperatingTemperature(2, "-55°C", "+105°C"));
        Map<Attributes, String> attributes = new HashMap<>();
        attributes.put(Attributes.ID, "1");
        Resistors testResistor = resistors.search(new ResistorsSpecification(attributes)).stream().findFirst().get();
        assertEquals(resistor, testResistor);
    }


    @Test
    @DisplayName("Search resistors by value")
    void searchValue() {
        Resistors resistor = new Resistors(4, 1, "kOhm", "0.1W",
                new CaseSize(2, "0603", 0.6, 0.3),
                new OperatingTemperature(2, "-55°C", "+105°C"));
        Map<Attributes, String> attributes = new HashMap<>();
        attributes.put(Attributes.VALUE, "1");
        Resistors testResistor = resistors.search(new ResistorsSpecification(attributes)).stream().findFirst().get();
        assertEquals(resistor, testResistor);
    }


    @Test
    @DisplayName("Search resistors by case")
    void searchCase() {
        Resistors resistor = new Resistors(1, 50, "Ohm", "0.1W",
                new CaseSize(1, "0402", 0.4, 0.2),
                new OperatingTemperature(2, "-55°C", "+105°C"));
        Map<Attributes, String> attributes = new HashMap<>();
        attributes.put(Attributes.CASE, "0402");
        Resistors testResistor = resistors.search(new ResistorsSpecification(attributes)).stream().findFirst().get();
        assertEquals(resistor, testResistor);
    }

    @Test
    @DisplayName("Search resistors by unit,power and case")
    void searchMulti() {
        Resistors resistor = new Resistors(4, 1, "kOhm", "0.1W",
                new CaseSize(2, "0603", 0.6, 0.3),
                new OperatingTemperature(2, "-55°C", "+105°C"));
        Map<Attributes, String> attributes = new HashMap<>();
        attributes.put(Attributes.VALUE, "1");
        attributes.put(Attributes.UNIT, "kOhm");
        attributes.put(Attributes.POWER, "0.1W");
        attributes.put(Attributes.CASE, "0603");
        Resistors testResistor = resistors.search(new ResistorsSpecification(attributes)).stream().findFirst().get();
        assertEquals(resistor, testResistor);
    }


    @Test
    @DisplayName("Search resistor by null")
    void searchNull() {
        assertThrows(NullPointerException.class, () -> resistors.search(null));
    }


    @Test
    @DisplayName("Create resistors")
    void create() {
        CreateDto createDto = CreateDto.builder()
                .caseValue("0402")
                .tempHigh("+175°C")
                .tempLow("-65°C")
                .value("12")
                .unit("Ohm")
                .power("0.1W")
                .build();
        assertTrue(resistors.create(createDto));
    }

    @Test
    @DisplayName("Delete resistors by id")
    void deleteById() {
        assertTrue(resistors.delete(1));
    }

    @Test
    @DisplayName("Update resistors")
    void update() {
        UpdateDto updateDto = UpdateDto.builder()
                .id("5")
                .unit("Ohm")
                .power("0.1W")
                .value("123")
                .build();
        assertTrue(resistors.update(updateDto));
    }
}

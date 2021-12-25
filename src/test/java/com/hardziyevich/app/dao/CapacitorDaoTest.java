package com.hardziyevich.app.dao;

import com.hardziyevich.app.controller.Attributes;
import com.hardziyevich.app.dao.impl.CapacitorDao;
import com.hardziyevich.app.dao.impl.CapacitorsSpecification;
import com.hardziyevich.app.entity.Capacitors;
import com.hardziyevich.app.entity.CaseSize;
import com.hardziyevich.app.entity.OperatingTemperature;
import com.hardziyevich.app.service.dto.CreateDto;
import com.hardziyevich.app.service.dto.UpdateDto;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hardziyevich.app.dao.impl.ConnectionPoolAbstract.Type.FLEXIBLE;
import static com.hardziyevich.app.dao.impl.ConnectionPoolFabric.PropertiesFile.*;
import static com.hardziyevich.app.dao.impl.ConnectionPoolFabric.PropertiesFile.POOL_SIZE;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CapacitorDaoTest {

    @Container
    private static final PostgreSQLContainer<TestContainer> postgreSQLContainer = TestContainer.getContainer();
    private static ElementDao<Capacitors> capacitors;

    @BeforeAll
    @DisplayName("Create db")
    void start() {
        postgreSQLContainer.start();
        Map<String, String> attributes = new HashMap<>();
        attributes.put(PASSWORD_KEY, postgreSQLContainer.getPassword());
        attributes.put(USERNAME_KEY, postgreSQLContainer.getUsername());
        attributes.put(URL_KEY, postgreSQLContainer.getJdbcUrl());
        attributes.put(POOL_SIZE, "5");
        capacitors = new CapacitorDao.Builder().type(FLEXIBLE).property(attributes).build();
    }

    @AfterAll
    void stop() {
        postgreSQLContainer.stop();
    }

    @Test
    @DisplayName("Search capacitors by id")
    void searchId() {
        Capacitors capacitor = new Capacitors(1, 10, "pF", "25V",
                new CaseSize(1, "0402", 0.4, 0.2),
                new OperatingTemperature(1, "-65°C", "+175°C"));
        Map<Attributes, String> attributes = new HashMap<>();
        attributes.put(Attributes.ID, "1");
        Capacitors capacitors = CapacitorDaoTest.capacitors.search(new CapacitorsSpecification(attributes)).stream().findFirst().get();
        assertEquals(capacitor, capacitors);
    }

    @Test
    @DisplayName("Search capacitors by value")
    void searchValue() {
        Capacitors capacitor = new Capacitors(3, 1, "uF", "25V",
                new CaseSize(1, "0402", 0.4, 0.2),
                new OperatingTemperature(1, "-65°C", "+175°C"));
        Map<Attributes, String> attributes = new HashMap<>();
        attributes.put(Attributes.VALUE, "1");
        Capacitors capacitors = CapacitorDaoTest.capacitors.search(new CapacitorsSpecification(attributes)).stream().findFirst().get();
        assertEquals(capacitor, capacitors);
    }

    @Test
    @DisplayName("Search capacitors by unit")
    void searchUnit() {
        Capacitors capacitor1 = new Capacitors(3, 1, "uF", "25V",
                new CaseSize(1, "0402", 0.4, 0.2),
                new OperatingTemperature(1, "-65°C", "+175°C"));

        Capacitors capacitor2 = new Capacitors(6, 10, "uF", "6.3V",
                new CaseSize(1, "0402", 0.4, 0.2),
                new OperatingTemperature(1, "-65°C", "+175°C"));
        List<Capacitors> list = List.of(capacitor1, capacitor2);
        Map<Attributes, String> attributes = new HashMap<>();
        attributes.put(Attributes.UNIT, "uF");assertEquals(list, capacitors.search(new CapacitorsSpecification(attributes)));
    }

    @Test
    @DisplayName("Search capacitors by voltage")
    void searchByVoltage() {
        Capacitors capacitor = new Capacitors(6, 10, "uF", "6.3V",
                new CaseSize(1, "0402", 0.4, 0.2),
                new OperatingTemperature(1, "-65°C", "+175°C"));
        Map<Attributes, String> attributes = new HashMap<>();
        attributes.put(Attributes.VOLTAGE, "6.3V");
        Capacitors capacitors = CapacitorDaoTest.capacitors.search(new CapacitorsSpecification(attributes)).stream().findFirst().get();
        assertEquals(capacitor, capacitors);
    }

    @Test
    @DisplayName("Search capacitors by case")
    void searchCase() {
        Capacitors capacitor = new Capacitors(1, 10, "pF", "25V",
                new CaseSize(1, "0402", 0.4, 0.2),
                new OperatingTemperature(1, "-65°C", "+175°C"));
        Map<Attributes, String> attributes = new HashMap<>();
        attributes.put(Attributes.CASE, "0402");
        Capacitors capacitors = CapacitorDaoTest.capacitors.search(new CapacitorsSpecification(attributes)).stream().findFirst().get();
        assertEquals(capacitor, capacitors);
    }


    @Test
    @DisplayName("Search capacitors by null")
    void searchByTemp() {
        assertThrows(NullPointerException.class, () -> CapacitorDaoTest.capacitors.search(null));
    }

    @Test
    @DisplayName("Create capacitors")
    void create() {
        CreateDto createDto = CreateDto.builder()
                .caseValue("0402")
                .tempHigh("+175°C")
                .tempLow("-65°C")
                .value("12")
                .unit("pF")
                .voltage("25V")
                .build();
        assertTrue(capacitors.create(createDto));
    }

    @Test
    @DisplayName("Delete capacitors by id")
    void deleteById() {
        assertTrue(capacitors.delete(5));
    }

    @Test
    @DisplayName("Update capacitors")
    void update() {
        UpdateDto updateDto = UpdateDto.builder()
                .id("4")
                .unit("pF")
                .voltage("50V")
                .value("123")
                .build();
        assertTrue(capacitors.update(updateDto));
    }
}

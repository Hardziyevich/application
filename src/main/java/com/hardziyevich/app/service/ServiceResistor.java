package com.hardziyevich.app.service;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.hardziyevich.app.controller.Attributes;
import com.hardziyevich.app.dao.ElementDao;
import com.hardziyevich.app.dao.impl.ResistorDaoImpl;
import com.hardziyevich.app.dao.impl.ResistorsSpecificationImpl;
import com.hardziyevich.app.entity.Resistors;
import com.hardziyevich.app.service.dto.CreateDto;
import com.hardziyevich.app.service.dto.UpdateDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.hardziyevich.app.controller.Attributes.*;
import static com.hardziyevich.app.dao.impl.ConnectionPoolAbstract.Type.DEFAULT;
import static com.hardziyevich.app.service.Service.RegularExpression.*;
import static com.hardziyevich.app.service.Service.RegularExpression.REG_TEMP_HIGH;

public class ServiceResistor implements Service {

    private static final ServiceResistor instance = new ServiceResistor();

    private final ElementDao<Resistors> resistorDao = new ResistorDaoImpl.Builder().type(DEFAULT).build();

    private ServiceResistor() {
    }

    @Override
    public boolean deleteById(String id) {
        boolean result = false;
        if (id != null) {
            Validator<String> validatorId = Validator.of(id).validator(i -> i.matches(REG_DIGIT), "Resistor id is not digit.");
            result = validatorId.isEmpty() && resistorDao.delete(Long.parseLong(validatorId.get()));
        }
        return result;
    }

    @Override
    public boolean create(CreateDto dto) {
        boolean result = false;
        if (dto != null) {
            Validator<CreateDto> validator = Validator.of(dto)
                    .validator(c -> c.getValue().matches(REG_DIGIT), "Resistor value is not digit.")
                    .validator(c -> c.getUnit().matches(REG_UNIT_RESISTOR), "Resistor contains is not correct unit.")
                    .validator(c -> c.getTempLow().matches(REG_TEMP_LOW), "Resistor temp low contains is not correct value.")
                    .validator(c -> c.getTempHigh().matches(REG_TEMP_HIGH), "Resistor temp high contains is not correct value.");
            result = validator.isEmpty() && resistorDao.create(dto);
        }
        return result;
    }

    @Override
    public boolean update(UpdateDto capacitorDto) {
        boolean result = false;
        if (capacitorDto != null) {
            Validator<UpdateDto> validator = Validator.of(capacitorDto)
                    .validator(c -> c.getId().matches(REG_DIGIT), "Resistor id is not digit.")
                    .validator(c -> c.getValue().matches(REG_DIGIT), "Resistor value is not digit.")
                    .validator(c -> c.getUnit().matches(REG_UNIT_RESISTOR), "Resistor contains is not correct unit.");
            result = validator.isEmpty() && resistorDao.update(capacitorDto);
        }
        return result;
    }

    @Override
    public List<JsonObject> search(Map<Attributes, String> attributes) {
        List<Resistors> result = new ArrayList<>();
        List<JsonObject> jsonObjects = new ArrayList<>();
        if (attributes != null) {
            Validator<Map<Attributes, String>> validator = Validator.of(attributes);
            attributes.forEach((k, v) -> {
                switch (k) {
                    case ID -> {
                        validator.validator(var -> var.get(ID).matches(REG_DIGIT), "Resistor id is not digit.");
                    }
                    case VALUE -> {
                        validator.validator(var -> var.get(VALUE).matches(REG_DIGIT), "Resistor value is not digit.");
                    }
                    case UNIT -> {
                        validator.validator(var -> var.get(UNIT).matches(REG_UNIT_CAPACITOR), "Resistor contains is not correct unit.");
                    }
                }
            });
            result = validator.isEmpty() ? resistorDao.search(new ResistorsSpecificationImpl(attributes)) : result;
        }
        for (Resistors search : result) {
            JsonObject json = new JsonObject();
            json.put(VALUE, search.value());
            json.put(UNIT, search.unitMeasurement());
            json.put(POWER, search.power());
            json.put(CASE, search.caseSize().nameInch());
            json.put(TEMP_HIGH, search.temperature().highTemp());
            json.put(TEMP_LOW, search.temperature().lowTemp());
            jsonObjects.add(json);
        }

        return jsonObjects;
    }

    public static Service getInstance() {
        return instance;
    }

}

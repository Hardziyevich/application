package com.hardziyevich.app.service.impl;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.hardziyevich.app.controller.Attributes;
import com.hardziyevich.app.dao.ElementDao;
import com.hardziyevich.app.dao.impl.ResistorsSpecification;
import com.hardziyevich.app.entity.Resistors;
import com.hardziyevich.app.service.Service;
import com.hardziyevich.app.service.Validator;
import com.hardziyevich.app.service.dto.CreateDto;
import com.hardziyevich.app.service.dto.UpdateDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.hardziyevich.app.controller.Attributes.*;
import static com.hardziyevich.app.service.Service.RegularExpression.*;
import static com.hardziyevich.app.service.Service.RegularExpression.REG_TEMP_HIGH;

/**
 * {@inheritDoc}
 */
public class ServiceResistor implements Service {

    private final ElementDao<Resistors> resistorDao;

    public ServiceResistor(final ElementDao<Resistors> resistorDao) {
        this.resistorDao = resistorDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteById(String id) {
        boolean result = false;
        if (id != null) {
            try {
                Validator<String> validatorId = Validator.of(id).validator(i -> i.matches(REG_DIGIT), "Resistor id is not digit.");
                result = validatorId.isEmpty() && resistorDao.delete(Long.parseLong(validatorId.get()));
            } catch (NullPointerException e) {
                return false;
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean create(CreateDto dto) {
        boolean result = false;
        if (dto != null) {
            try {
                Validator<CreateDto> validator = Validator.of(dto)
                        .validator(c -> c.getValue().matches(REG_DIGIT), "Resistor value is not digit.")
                        .validator(c -> c.getUnit().matches(REG_UNIT_RESISTOR), "Resistor contains is not correct unit.")
                        .validator(c -> c.getTempLow().matches(REG_TEMP_LOW), "Resistor temp low contains is not correct value.")
                        .validator(c -> c.getPower().matches(REG_POWER), "Resistor power contains is not correct power value.")
                        .validator(c -> c.getTempHigh().matches(REG_TEMP_HIGH), "Resistor temp high contains is not correct value.");
                result = validator.isEmpty() && resistorDao.create(validator.get());
            } catch (NullPointerException e) {
                return false;
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean update(UpdateDto capacitorDto) {
        boolean result = false;
        if (capacitorDto != null) {
            try {
                Validator<UpdateDto> validator = Validator.of(capacitorDto)
                        .validator(c -> c.getId().matches(REG_DIGIT), "Resistor id is not digit.")
                        .validator(c -> c.getValue().matches(REG_DIGIT), "Resistor value is not digit.")
                        .validator(c -> c.getPower().matches(REG_POWER), "Resistor power contains is not correct power value.")
                        .validator(c -> c.getUnit().matches(REG_UNIT_RESISTOR), "Resistor contains is not correct unit.");
                result = validator.isEmpty() && resistorDao.update(validator.get());
            } catch (NullPointerException e) {
                return false;
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<JsonObject> search(Map<Attributes, String> attributes) {
        boolean validation;
        List<Resistors> result = new ArrayList<>();
        List<JsonObject> jsonObjects = new ArrayList<>();
        if (attributes != null) {
            Validator<Map<Attributes, String>> validator = Validator.of(attributes);
            try {
                attributes.forEach((k, v) -> {
                    switch (k) {
                        case ID -> validator.validator(var -> var.get(ID).matches(REG_DIGIT), "Resistor id is not digit.");
                        case VALUE -> validator.validator(var -> var.get(VALUE).matches(REG_DIGIT), "Resistor value is not digit.");
                        case UNIT -> validator.validator(var -> var.get(UNIT).matches(REG_UNIT_RESISTOR), "Resistor contains is not correct unit.");
                        case POWER -> validator.validator(var -> var.get(POWER).matches(REG_POWER), "Resistor power contains is not correct power value.");
                    }
                });
                validation = validator.isEmpty();
            } catch (NullPointerException e) {
                validation = false;
            }
            result = validation ? resistorDao.search(new ResistorsSpecification(validator.get())) : result;
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

}

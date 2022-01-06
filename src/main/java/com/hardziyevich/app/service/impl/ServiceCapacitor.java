package com.hardziyevich.app.service.impl;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.hardziyevich.app.controller.Attributes;
import com.hardziyevich.app.dao.ElementDao;
import com.hardziyevich.app.dao.impl.CapacitorsSpecification;
import com.hardziyevich.app.entity.Capacitors;
import com.hardziyevich.app.service.Service;
import com.hardziyevich.app.service.Validator;
import com.hardziyevich.app.service.dto.CreateDto;
import com.hardziyevich.app.service.dto.UpdateDto;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.hardziyevich.app.service.Service.RegularExpression.*;
import static com.hardziyevich.app.controller.Attributes.*;

/**
 * {@inheritDoc}
 */
public class ServiceCapacitor implements Service {

    private final ElementDao<Capacitors> capacitorDao;

    public ServiceCapacitor(final ElementDao<Capacitors> capacitorDao) {
        this.capacitorDao = capacitorDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteById(String id) {
        boolean result = false;
        if (id != null) {
            try {
                Validator<String> validatorId = Validator.of(id).validator(i -> i.matches(REG_DIGIT), "Capacitor id is not digit.");
                result = validatorId.isEmpty() && capacitorDao.delete(Long.parseLong(validatorId.get()));
            }catch (NullPointerException e){
                return false;
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long create(CreateDto dto) {
        long result = INVALID_RESULT;
        if (dto != null) {
            try {
                Validator<CreateDto> validator = Validator.of(dto)
                        .validator(c -> c.getValue().matches(REG_DIGIT), "Capacitor value is not digit.")
                        .validator(c -> c.getUnit().matches(REG_UNIT_CAPACITOR), "Capacitor contains is not correct unit.")
                        .validator(c -> c.getVoltage().matches(REG_VOLTAGE), "Capacitor contains is not correct voltage value.")
                        .validator(c -> c.getTempLow().matches(REG_TEMP_LOW), "Capacitor temp low contains is not correct value.")
                        .validator(c -> c.getTempHigh().matches(REG_TEMP_HIGH), "Capacitor temp high contains is not correct value.");
                result = validator.isEmpty() ? capacitorDao.create(dto) : result;
            }catch (NullPointerException e) {
                return result;
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long update(UpdateDto dto) {
        long result = INVALID_RESULT;
        if (dto != null) {
            Validator<UpdateDto> validator = Validator.of(dto)
                    .validator(c -> c.getId().matches(REG_DIGIT), "Capacitor id is not digit.")
                    .validator(c -> c.getValue().matches(REG_DIGIT), "Capacitor value is not digit.")
                    .validator(c -> c.getVoltage().matches(REG_VOLTAGE), "Capacitor contains is not correct voltage value.")
                    .validator(c -> c.getUnit().matches(REG_UNIT_CAPACITOR), "Capacitor contains is not correct unit.");
            result = validator.isEmpty() ? capacitorDao.update(dto) : result;
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<JsonObject> search(Map<Attributes, String> attributes) {
        boolean validation;
        List<Capacitors> result = new ArrayList<>();
        List<JsonObject> jsonObjects = new ArrayList<>();
        if (attributes != null) {
            Validator<Map<Attributes, String>> validator = Validator.of(attributes);
            try {
                attributes.forEach((k, v) -> {
                    switch (k) {
                        case ID -> validator.validator(var -> var.get(ID).matches(REG_DIGIT), "Capacitor id is not digit.");
                        case VALUE -> validator.validator(var -> var.get(VALUE).matches(REG_DIGIT), "Capacitor value is not digit.");
                        case UNIT -> validator.validator(var -> var.get(UNIT).matches(REG_UNIT_CAPACITOR), "Capacitor contains is not correct unit.");
                        case VOLTAGE -> validator.validator(var -> var.get(VOLTAGE).matches(REG_VOLTAGE), "Capacitor contains is not correct voltage value.");
                    }
                });
                validation = validator.isEmpty();
            }catch (NullPointerException e) {
                validation = false;
            }
            result = validation ? capacitorDao.search(new CapacitorsSpecification(attributes)) : result;
        }
        for (Capacitors search : result) {
            JsonObject json = new JsonObject();
            json.put(VALUE, search.value());
            json.put(UNIT, search.unitMeasurement());
            json.put(VOLTAGE, search.voltageRated());
            json.put(CASE, search.caseSize().nameInch());
            json.put(TEMP_HIGH, search.temperature().highTemp());
            json.put(TEMP_LOW, search.temperature().lowTemp());
            jsonObjects.add(json);
        }
        return jsonObjects;
    }

}

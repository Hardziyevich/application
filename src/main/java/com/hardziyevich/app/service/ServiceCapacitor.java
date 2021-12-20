package com.hardziyevich.app.service;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.hardziyevich.app.controller.AttributesCapacitor;
import com.hardziyevich.app.dao.CapacitorDao;
import com.hardziyevich.app.dao.impl.CapacitorDaoImpl;
import com.hardziyevich.app.dao.impl.CapacitorsSpecificationImpl;
import com.hardziyevich.app.entity.Capacitors;
import com.hardziyevich.app.service.dto.CreateCapacitorDto;
import com.hardziyevich.app.service.dto.UpdateCapacitorDto;
import com.hardziyevich.app.util.Validator;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.hardziyevich.app.service.Service.RegularExpression.*;
import static com.hardziyevich.app.controller.AttributesCapacitor.*;

public class ServiceCapacitor implements Service {

    private static final ServiceCapacitor instance = new ServiceCapacitor();

    private final CapacitorDao capacitorDao = CapacitorDaoImpl.getInstance();


    private ServiceCapacitor() {
    }

    @Override
    public boolean deleteById(String id) {
        boolean result = false;
        if (id != null) {
            Validator<String> validatorId = Validator.of(id).validator(i -> i.matches(REG_DIGIT), "Capacitor id is not digit.");
            result = validatorId.isEmpty() && capacitorDao.delete(Long.parseLong(validatorId.get()));
        }
        return result;
    }

    @Override
    public boolean create(CreateCapacitorDto capacitorDto) {
        boolean result = false;
        if (capacitorDto != null) {
            Validator<CreateCapacitorDto> validator = Validator.of(capacitorDto)
                    .validator(c -> c.getValue().matches(REG_DIGIT), "Capacitor value is not digit.")
                    .validator(c -> c.getUnit().matches(REG_UNIT_CAPACITOR), "Capacitor contains is not correct unit.")
                    .validator(c -> c.getTempLow().matches(REG_TEMP_LOW), "Capacitor temp low contains is not correct value.")
                    .validator(c -> c.getTempHigh().matches(REG_TEMP_HIGH), "Capacitor temp high contains is not correct value.");
            result = validator.isEmpty() && capacitorDao.create(capacitorDto);
        }
        return result;
    }

    @Override
    public boolean update(UpdateCapacitorDto capacitorDto) {
        boolean result = false;
        if (capacitorDto != null) {
            Validator<UpdateCapacitorDto> validator = Validator.of(capacitorDto)
                    .validator(c -> c.getId().matches(REG_DIGIT), "Capacitor id is not digit.")
                    .validator(c -> c.getValue().matches(REG_DIGIT), "Capacitor value is not digit.")
                    .validator(c -> c.getUnit().matches(REG_UNIT_CAPACITOR), "Capacitor contains is not correct unit.");
            result = validator.isEmpty() && capacitorDao.update(capacitorDto);
        }
        return result;
    }

    @Override
    public List<JsonObject> search(Map<AttributesCapacitor, String> attributes) {
        List<Capacitors> result = new ArrayList<>();
        List<JsonObject> jsonObjects = new ArrayList<>();
        if (attributes != null) {
            Validator<Map<AttributesCapacitor, String>> validator = Validator.of(attributes);
            attributes.forEach((k, v) -> {
                switch (k) {
                    case ID -> {
                        validator.validator(var -> var.get(ID).matches(REG_DIGIT), "Capacitor id is not digit.");
                    }
                    case VALUE -> {
                        validator.validator(var -> var.get(VALUE).matches(REG_DIGIT), "Capacitor value is not digit.");
                    }
                    case UNIT -> {
                        validator.validator(var -> var.get(UNIT).matches(REG_UNIT_CAPACITOR), "Capacitor contains is not correct unit.");
                    }
                }
            });
            result = validator.isEmpty() ? capacitorDao.search(new CapacitorsSpecificationImpl(attributes)) : result;
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

    public static ServiceCapacitor getInstance() {
        return instance;
    }
}

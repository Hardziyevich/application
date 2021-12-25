package com.hardziyevich.app.dao.impl;

import com.hardziyevich.app.entity.Capacitors;
import com.hardziyevich.app.entity.CaseSize;
import com.hardziyevich.app.entity.OperatingTemperature;
import com.hardziyevich.app.entity.Resistors;

import java.sql.ResultSet;
import java.sql.SQLException;

class Mapper {

    static Resistors mapResistor(ResultSet resultSet) throws SQLException {
        return new Resistors(
                resultSet.getLong("id"),
                resultSet.getInt("value"),
                resultSet.getString("unit_measurement"),
                resultSet.getString("power"),
                new CaseSize(
                        resultSet.getLong(5),
                        resultSet.getString("name"),
                        resultSet.getDouble("length_mm"),
                        resultSet.getDouble("width_mm")
                ),
                new OperatingTemperature(
                        resultSet.getLong(9),
                        resultSet.getString("low_temp"),
                        resultSet.getString("high_temp")
                )
        );
    }

    static Capacitors mapCapacitor(ResultSet resultSet) throws SQLException {
        return new Capacitors(
                resultSet.getLong("id"),
                resultSet.getInt("value"),
                resultSet.getString("unit_measurement"),
                resultSet.getString("voltage_rated"),
                new CaseSize(
                        resultSet.getLong(5),
                        resultSet.getString("name"),
                        resultSet.getDouble("length_mm"),
                        resultSet.getDouble("width_mm")
                ),
                new OperatingTemperature(
                        resultSet.getLong(9),
                        resultSet.getString("low_temp"),
                        resultSet.getString("high_temp")
                )
        );
    }
}

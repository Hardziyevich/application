package com.hardziyevich.app.dao.impl;

import com.github.cliftonlabs.json_simple.JsonKey;
import com.hardziyevich.app.controller.AttributesCapacitor;
import com.hardziyevich.app.dao.JdbcSpecification;
import com.hardziyevich.app.entity.Capacitors;
import com.hardziyevich.app.entity.CaseSize;
import com.hardziyevich.app.entity.OperatingTemperature;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static com.hardziyevich.app.dao.impl.SqlCommand.Select.*;
import static com.hardziyevich.app.dao.impl.SqlCommand.Tables.TABLE_CAPACITORS;


public class CapacitorsSpecificationImpl implements JdbcSpecification {

    private Map<AttributesCapacitor, String> attributes;

    public CapacitorsSpecificationImpl(Map<AttributesCapacitor, String> attributes) {
        this.attributes = attributes;
    }

    @Override
    public List<Capacitors> searchFilter(Connection connection) throws SQLException {
        List<Capacitors> capacitors = new ArrayList<>();
        String sql = SELECT.formatted(SELECT_SETTING_CAPACITORS, TABLE_CAPACITORS);
        StringBuilder sqlBuilder = new StringBuilder(sql);
        Queue<Object> queue = new ArrayDeque<>();
        for (Map.Entry<AttributesCapacitor, String> next : attributes.entrySet()) {
            switch (next.getKey()) {
                case ID -> {
                    sqlBuilder.append(SELECT_ID).append(SELECT_AND);
                    queue.add(Long.parseLong(next.getValue()));
                }
                case CASE -> {
                    sqlBuilder.append(SELECT_CASE_NAME).append(SELECT_AND);
                    queue.add(next.getValue());
                }
                case VALUE -> {
                    sqlBuilder.append(SELECT_VALUE).append(SELECT_AND);
                    queue.add(Integer.parseInt(next.getValue()));
                }
                case UNIT -> {
                    sqlBuilder.append(SELECT_UNIT).append(SELECT_AND);
                    queue.add(next.getValue());
                }
                case VOLTAGE -> {
                    sqlBuilder.append(SELECT_VOLTAGE_RATED).append(SELECT_AND);
                    queue.add(next.getValue());
                }
            }
        }
        sqlBuilder.delete(sqlBuilder.length() - SELECT_AND.length(), sqlBuilder.length());
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlBuilder.toString())) {
            final int size = queue.size();
            for (int i = 1; i < size + 1; i++) {
                preparedStatement.setObject(i, queue.poll());
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                capacitors.add(mapper(resultSet));
            }
        }
        return capacitors;
    }

    private Capacitors mapper(ResultSet resultSet) throws SQLException {
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

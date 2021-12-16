package com.hardziyevich.app.dao.impl;

import com.hardziyevich.app.controller.AttributesCapacitor;
import com.hardziyevich.app.dao.CapacitorDao;
import com.hardziyevich.app.controller.Request;
import com.hardziyevich.app.entity.Capacitors;
import com.hardziyevich.app.entity.CaseSize;
import com.hardziyevich.app.entity.OperatingTemperature;

import java.sql.*;
import java.util.*;

import static com.hardziyevich.app.controller.AttributesCapacitor.*;
import static com.hardziyevich.app.dao.impl.SqlCommand.Delete.*;
import static com.hardziyevich.app.dao.impl.SqlCommand.Insert.INSERT;
import static com.hardziyevich.app.dao.impl.SqlCommand.Insert.INSERT_SETTING_CAPACITORS;
import static com.hardziyevich.app.dao.impl.SqlCommand.Select.*;
import static com.hardziyevich.app.dao.impl.SqlCommand.Tables.*;
import static com.hardziyevich.app.dao.impl.SqlCommand.Update.*;
import static com.hardziyevich.app.db.ConnectionPool.INSTANCE;

public class CapacitorDaoImpl implements CapacitorDao {


    @Override
    public boolean create(Request request) {
        int result = 0;
        String command = INSERT.formatted(TABLE_CAPACITORS, INSERT_SETTING_CAPACITORS, TABLE_CASE, TABLE_TEMPERATURE);
        try (Connection connection = INSTANCE.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(command)) {
            preparedStatement.setObject(1, request.getValueCapacitor());
            preparedStatement.setObject(2, request.getUnit());
            preparedStatement.setObject(3, request.getVoltageRate());
            preparedStatement.setObject(4, request.getCaseSize());
            preparedStatement.setObject(5, request.getTempHigh());
            preparedStatement.setObject(6, request.getTempLow());
            preparedStatement.execute();
            result = preparedStatement.getUpdateCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result == 1;
    }

    @Override
    public boolean delete(long id) {
        int result = 0;
        String delete = DELETE_BY_ID.formatted(TABLE_CAPACITORS);
        try (Connection connection = INSTANCE.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(delete)) {
            preparedStatement.setLong(1, id);
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result == 1;
    }

    @Override
    public boolean update(Request build) {
        boolean result = false;
        String sql = UPDATE.formatted(TABLE_CAPACITORS);
        try (Connection connection = INSTANCE.openConnection()) {
            result = concatRequestForUpdate(build, sql, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Capacitors> search(Request request) {
        List<Capacitors> result = new ArrayList<>();
        String sql = SELECT.formatted(SELECT_SETTING_CAPACITORS, TABLE_CAPACITORS);
        try (Connection connection = INSTANCE.openConnection()) {
            result.addAll(concatRequestForSearch(request, sql, connection));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    List<Capacitors> concatRequestForSearch(Request build, String request, Connection connection) throws SQLException {
        List<Capacitors> connections = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder(request);
        Queue<Object> attributes = new ArrayDeque<>();
        for (Map.Entry<AttributesCapacitor, String> next : build.getAttributes().entrySet()) {
            switch (next.getKey()) {
                case ID -> {
                    sqlBuilder.append(SELECT_ID).append(SELECT_AND);
                    attributes.add(Long.parseLong(next.getValue()));
                }
                case CASE -> {
                    sqlBuilder.append(SELECT_CASE_NAME).append(SELECT_AND);
                    attributes.add(next.getValue());
                }
                case TEMP_LOW -> {
                    sqlBuilder.append(SELECT_TEMP_LOW).append(SELECT_AND);
                    attributes.add(next.getValue());
                }
                case TEMP_HIGH -> {
                    sqlBuilder.append(SELECT_TEMP_HIGH).append(SELECT_AND);
                    attributes.add(next.getValue());
                }
                case VALUE -> {
                    sqlBuilder.append(SELECT_VALUE).append(SELECT_AND);
                    attributes.add(Integer.parseInt(next.getValue()));
                }
                case UNIT -> {
                    sqlBuilder.append(SELECT_UNIT).append(SELECT_AND);
                    attributes.add(next.getValue());
                }
                case VOLTAGE -> {
                    sqlBuilder.append(SELECT_VOLTAGE_RATED).append(SELECT_AND);
                    attributes.add(next.getValue());
                }
            }
        }
        sqlBuilder.delete(sqlBuilder.length() - SELECT_AND.length(), sqlBuilder.length());
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlBuilder.toString())) {
            final int size = attributes.size();
            for (int i = 1; i < size + 1; i++) {
                preparedStatement.setObject(i, attributes.poll());
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                connections.add(mapper(resultSet));
            }
        }
        return connections;
    }

    boolean concatRequestForUpdate(Request build, String request, Connection connection) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder(request);
        Queue<Object> attributes = new ArrayDeque<>();
        boolean result;
        for (Map.Entry<AttributesCapacitor, String> next : build.getAttributes().entrySet()) {
            switch (next.getKey()) {
                case CASE -> {
                    sqlBuilder.append(UPDATE_CASE_NAME).append(UPDATE_COMMA);
                    attributes.add(next.getValue());
                }
                case VALUE -> {
                    sqlBuilder.append(UPDATE_VALUE).append(UPDATE_COMMA);
                    attributes.add(Integer.parseInt(next.getValue()));
                }
                case UNIT -> {
                    sqlBuilder.append(UPDATE_UNIT).append(UPDATE_COMMA);
                    attributes.add(next.getValue());
                }
                case VOLTAGE -> {
                    sqlBuilder.append(UPDATE_VOLTAGE).append(UPDATE_COMMA);
                    attributes.add(next.getValue());
                }
            }
        }
        build.getAttributes().forEach((key, value) -> {
            if (key.equals(ID)) {
                attributes.add(Long.parseLong(value));
            }
        });
        sqlBuilder.delete(sqlBuilder.length() - UPDATE_COMMA.length(), sqlBuilder.length()).append(UPDATE_WHERE_ID);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlBuilder.toString())) {
            final int size = attributes.size();
            for (int i = 1; i < size + 1; i++) {
                preparedStatement.setObject(i, attributes.poll());
            }
            result = preparedStatement.executeUpdate() == 1;
        }
        return result;
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

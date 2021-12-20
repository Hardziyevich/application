package com.hardziyevich.app.dao.impl;

import com.hardziyevich.app.dao.CapacitorDao;
import com.hardziyevich.app.dao.JdbcSpecification;
import com.hardziyevich.app.entity.Capacitors;
import com.hardziyevich.app.service.dto.CreateCapacitorDto;
import com.hardziyevich.app.service.dto.UpdateCapacitorDto;

import java.sql.*;
import java.util.*;

import static com.hardziyevich.app.dao.impl.SqlCommand.Insert.*;
import static com.hardziyevich.app.dao.impl.SqlCommand.Delete.*;
import static com.hardziyevich.app.dao.impl.SqlCommand.Tables.*;
import static com.hardziyevich.app.dao.impl.SqlCommand.Update.*;
import static com.hardziyevich.app.dao.impl.ConnectionPool.INSTANCE;

public class CapacitorDaoImpl implements CapacitorDao {

    private static final CapacitorDaoImpl instance = new CapacitorDaoImpl();

    private CapacitorDaoImpl() {
    }


    @Override
    public boolean create(CreateCapacitorDto request) {
        int result = 0;
        String command = INSERT.formatted(TABLE_CAPACITORS, INSERT_SETTING_CAPACITORS, TABLE_CASE, TABLE_TEMPERATURE);
        try (Connection connection = INSTANCE.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(command)) {
            preparedStatement.setObject(1, Integer.parseInt(request.getValue()));
            preparedStatement.setObject(2, request.getUnit());
            preparedStatement.setObject(3, request.getVoltage());
            preparedStatement.setObject(4, request.getCaseValue());
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
    public boolean update(UpdateCapacitorDto capacitorDto) {
        boolean result = false;
        String sql = UPDATE.formatted(TABLE_CAPACITORS);
        try (Connection connection = INSTANCE.openConnection()) {
            result = concatUpdate(capacitorDto, sql, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Capacitors> search(JdbcSpecification jdbcSpecification) {
        List<Capacitors> capacitors = new ArrayList<>();
        try (Connection connection = INSTANCE.openConnection()) {
            capacitors.addAll(jdbcSpecification.searchFilter(connection));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return capacitors;
    }

    private boolean concatUpdate(UpdateCapacitorDto capacitorDto, String sql, Connection connection) throws SQLException {
        boolean result = false;
        StringBuilder sqlBuilder = new StringBuilder(sql);
        sqlBuilder.append(UPDATE_VALUE).append(UPDATE_COMMA)
                .append(UPDATE_UNIT).append(UPDATE_COMMA)
                .append(UPDATE_VOLTAGE)
                .append(UPDATE_WHERE_ID);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlBuilder.toString())) {
            preparedStatement.setObject(1, Integer.parseInt(capacitorDto.getValue()));
            preparedStatement.setObject(2, capacitorDto.getUnit());
            preparedStatement.setObject(3, capacitorDto.getVoltage());
            preparedStatement.setObject(4, Long.parseLong(capacitorDto.getId()));
            result = preparedStatement.executeUpdate() == 1;
        }
        return result;
    }

    public static CapacitorDaoImpl getInstance() {
        return instance;
    }

}

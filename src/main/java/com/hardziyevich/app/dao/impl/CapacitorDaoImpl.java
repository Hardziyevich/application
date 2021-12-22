package com.hardziyevich.app.dao.impl;

import com.hardziyevich.app.dao.ElementDao;
import com.hardziyevich.app.dao.JdbcSpecification;
import com.hardziyevich.app.entity.Capacitors;
import com.hardziyevich.app.service.dto.CreateDto;
import com.hardziyevich.app.service.dto.UpdateDto;

import java.sql.*;
import java.util.*;

import static com.hardziyevich.app.dao.impl.SqlCommand.Insert.*;
import static com.hardziyevich.app.dao.impl.SqlCommand.Delete.*;
import static com.hardziyevich.app.dao.impl.SqlCommand.Select.SELECT;
import static com.hardziyevich.app.dao.impl.SqlCommand.Select.SELECT_SETTING_CAPACITORS;
import static com.hardziyevich.app.dao.impl.SqlCommand.Tables.*;
import static com.hardziyevich.app.dao.impl.SqlCommand.Update.*;

public class CapacitorDaoImpl implements ElementDao<Capacitors> {

    private static ConnectionPool connectionPool;

    private CapacitorDaoImpl(CapacitorDaoImpl.Builder builder) {
        connectionPool = ConnectionPoolAbstract.connectionPool(builder.type, builder.properties);
    }

    @Override
    public boolean create(CreateDto request) {
        int result = 0;
        String command = INSERT.formatted(TABLE_CAPACITORS, INSERT_SETTING_CAPACITORS, TABLE_CASE, TABLE_TEMPERATURE);
        try (Connection connection = connectionPool.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(command)) {
            Object[] seq = {
                    Integer.parseInt(request.getValue()),
                    request.getUnit(),
                    request.getVoltage(),
                    request.getCaseValue(),
                    request.getTempHigh(),
                    request.getTempLow()
            };
            JdbcUtil.setStatement(preparedStatement, seq);
            preparedStatement.execute();
            result = preparedStatement.getUpdateCount();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result == 1;
    }

    @Override
    public boolean delete(long id) {
        int result;
        String delete = DELETE_BY_ID.formatted(TABLE_CAPACITORS);
        try (Connection connection = connectionPool.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(delete)) {
            preparedStatement.setLong(1, id);
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result == 1;
    }

    @Override
    public boolean update(UpdateDto capacitorDto) {
        boolean result;
        String sql = UPDATE.formatted(TABLE_CAPACITORS);
        StringBuilder sqlBuilder = new StringBuilder(sql);
        sqlBuilder.append(UPDATE_VALUE).append(UPDATE_COMMA)
                .append(UPDATE_UNIT).append(UPDATE_COMMA)
                .append(UPDATE_VOLTAGE)
                .append(UPDATE_WHERE_ID);
        Object[] req = {
                Integer.parseInt(capacitorDto.getValue()),
                capacitorDto.getUnit(),
                capacitorDto.getVoltage(),
                Long.parseLong(capacitorDto.getId())
        };
        try (Connection connection = connectionPool.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlBuilder.toString())) {
            JdbcUtil.setStatement(preparedStatement, req);
            result = preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public List<Capacitors> search(JdbcSpecification<Capacitors> jdbcSpecification) {
        String sql = SELECT.formatted(SELECT_SETTING_CAPACITORS, TABLE_CAPACITORS);
        jdbcSpecification.setSql(sql);
        List<Capacitors> capacitors = new ArrayList<>();
        try (Connection connection = connectionPool.openConnection()) {
            capacitors.addAll(jdbcSpecification.searchFilter(connection));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return capacitors;
    }



    public static class Builder {

        private Map<String,String> properties = new HashMap<>();
        private ConnectionPoolAbstract.Type type;

        public Builder property(String key, String value) {
            properties.put(key, value);
            return this;
        }

        public Builder type(ConnectionPoolAbstract.Type type) {
            this.type = type;
            return this;
        }

        public CapacitorDaoImpl build() {
            return new CapacitorDaoImpl(this);
        }
    }
}

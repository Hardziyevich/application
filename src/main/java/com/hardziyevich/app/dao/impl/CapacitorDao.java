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

/**
 * {@inheritDoc}
 */
public class CapacitorDao implements ElementDao<Capacitors> {

    private static ConnectionPool connectionPool;

    private CapacitorDao(final Builder builder) {
        if (connectionPool == null) {
            connectionPool = ConnectionPoolAbstract.connectionPool(builder.type, builder.properties);
        }
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Capacitors> search(JdbcSpecification<Capacitors> jdbcSpecification) {
        String sql = SELECT.formatted(SELECT_SETTING_CAPACITORS, TABLE_CAPACITORS);
        List<Capacitors> capacitors = new ArrayList<>();
        try (Connection connection = connectionPool.openConnection()) {
            capacitors.addAll(jdbcSpecification.searchFilter(connection,sql));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return capacitors;
    }

    /**
     * Builder sets setting for ResistorDao
     */
    public static class Builder {

        private final Map<String, String> properties = new HashMap<>();
        private ConnectionPoolAbstract.Type type;

        /**
         * Provides method that set properties for ResistorDao
         *
         * @param key   that key need to use from PropertiesFile
         * @param value a string value
         * @return itself
         */
        public Builder property(String key, String value) {
            properties.put(key, value);
            return this;
        }

        /**
         * Provides method that set properties for ResistorDao
         *
         * @param properties a set of the properties
         * @return itself
         */
        public Builder property(Map<String, String> properties) {
            this.properties.putAll(properties);
            return this;
        }

        /**
         * Provides method that set type for loading ResistorDao
         *
         * @param type a type of ConnectionPoolAbstract.Type
         * @return itself
         */
        public Builder type(ConnectionPoolAbstract.Type type) {
            this.type = type;
            return this;
        }

        /**
         * Create ElementDao<Resistors>
         *
         * @return a elementdao interface
         */
        public CapacitorDao build() {
            return new CapacitorDao(this);
        }
    }
}

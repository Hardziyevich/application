package com.hardziyevich.app.dao.impl;

import com.hardziyevich.app.dao.ElementDao;
import com.hardziyevich.app.dao.JdbcSpecification;
import com.hardziyevich.app.entity.Resistors;
import com.hardziyevich.app.service.dto.CreateDto;
import com.hardziyevich.app.service.dto.UpdateDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hardziyevich.app.dao.impl.SqlCommand.Constant.INVALID_RESULT;
import static com.hardziyevich.app.dao.impl.SqlCommand.Delete.DELETE_BY_ID;
import static com.hardziyevich.app.dao.impl.SqlCommand.Insert.*;
import static com.hardziyevich.app.dao.impl.SqlCommand.Select.SELECT;
import static com.hardziyevich.app.dao.impl.SqlCommand.Select.SELECT_SETTING_RESISTORS;
import static com.hardziyevich.app.dao.impl.SqlCommand.Tables.*;
import static com.hardziyevich.app.dao.impl.SqlCommand.Update.*;

/**
 * {@inheritDoc}
 */
public class ResistorDao implements ElementDao<Resistors> {

    private static ConnectionPool connectionPool;

    private ResistorDao(final Builder builder) {
        if (connectionPool == null) {
            connectionPool = ConnectionPoolAbstract.connectionPool(builder.type, builder.properties);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long create(CreateDto request) {
        long result = INVALID_RESULT;
        String command = INSERT.formatted(TABLE_RESISTORS, INSERT_SETTING_RESISTORS, TABLE_CASE, TABLE_TEMPERATURE);
        try (Connection connection = connectionPool.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(command, Statement.RETURN_GENERATED_KEYS)) {
            JdbcUtil.setStatement(preparedStatement, new Object[]{
                    Integer.parseInt(request.getValue()),
                    request.getUnit(),
                    request.getPower(),
                    request.getCaseValue(),
                    request.getTempHigh(),
                    request.getTempLow()
            });
            preparedStatement.execute();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    result = generatedKeys.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(long id) {
        int result;
        String delete = DELETE_BY_ID.formatted(TABLE_RESISTORS);
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
    public long update(UpdateDto request) {
        long result = INVALID_RESULT;
        String sql = UPDATE.formatted(TABLE_RESISTORS);
        StringBuilder sqlBuilder = new StringBuilder(sql);
        sqlBuilder.append(UPDATE_VALUE).append(UPDATE_COMMA)
                .append(UPDATE_UNIT).append(UPDATE_COMMA)
                .append(UPDATE_POWER)
                .append(UPDATE_WHERE_ID);
        try (Connection connection = connectionPool.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlBuilder.toString())) {
            JdbcUtil.setStatement(preparedStatement, new Object[]{
                    Integer.parseInt(request.getValue()),
                    request.getUnit(),
                    request.getPower(),
                    Long.parseLong(request.getId())});
            result = preparedStatement.executeUpdate() == 1 ? Long.parseLong(request.getId()) : result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Resistors> search(JdbcSpecification<Resistors> jdbcSpecification) {
        String sql = SELECT.formatted(SELECT_SETTING_RESISTORS, TABLE_RESISTORS);
        List<Resistors> resistors = new ArrayList<>();
        try (Connection connection = connectionPool.openConnection()) {
            resistors.addAll(jdbcSpecification.searchFilter(connection, sql));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resistors;
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
        public ElementDao<Resistors> build() {
            return new ResistorDao(this);
        }

    }

}

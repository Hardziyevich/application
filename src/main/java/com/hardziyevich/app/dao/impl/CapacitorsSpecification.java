package com.hardziyevich.app.dao.impl;

import com.hardziyevich.app.controller.Attributes;
import com.hardziyevich.app.dao.JdbcSpecification;
import com.hardziyevich.app.entity.Capacitors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * {@inheritDoc}
 */
public class CapacitorsSpecification implements JdbcSpecification<Capacitors> {

    private final Map<Attributes, String> attributes;
    private String sql;

    public CapacitorsSpecification(Map<Attributes, String> attributes) {
        this.attributes = attributes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Capacitors> searchFilter(Connection connection) throws SQLException {
        List<Capacitors> capacitors = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder(sql);
        Queue<Object> queue = new ArrayDeque<>();
        JdbcUtil.preparedRequest(sqlBuilder, queue, attributes);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlBuilder.toString())) {
            final int size = queue.size();
            for (int i = 1; i < size + 1; i++) {
                preparedStatement.setObject(i, queue.poll());
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                capacitors.add(Mapper.mapCapacitor(resultSet));
            }
        }
        return capacitors;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSql(String sql) {
        this.sql = sql;
    }
}

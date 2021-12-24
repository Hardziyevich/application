package com.hardziyevich.app.dao.impl;

import com.hardziyevich.app.controller.Attributes;
import com.hardziyevich.app.dao.JdbcSpecification;
import com.hardziyevich.app.entity.Resistors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * {@inheritDoc}
 */
public class ResistorsSpecification implements JdbcSpecification<Resistors> {

    private final Map<Attributes, String> attributes;
    private String sql;


    public ResistorsSpecification(Map<Attributes, String> attributes) {
        this.attributes = attributes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Resistors> searchFilter(Connection connection) throws SQLException {
        List<Resistors> resistors = new ArrayList<>();
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
                resistors.add(Mapper.mapResistor(resultSet));
            }
        }
        return resistors;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void setSql(String sql) {
        this.sql = sql;
    }
}

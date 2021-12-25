package com.hardziyevich.app.dao.impl;

import com.hardziyevich.app.controller.Attributes;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Queue;

import static com.hardziyevich.app.dao.impl.SqlCommand.Select.*;
import static com.hardziyevich.app.dao.impl.SqlCommand.Select.SELECT_AND;

/**
 * Util class provides methods for create request to database
 */
class JdbcUtil {

    static void setStatement(PreparedStatement preparedStatement, Object[] seq) throws SQLException {
        for (int i = 1; i < seq.length + 1; i++) {
            preparedStatement.setObject(i, seq[i - 1]);
        }
    }

    static void preparedRequest(StringBuilder sqlBuilder, Queue<Object> queue, Map<Attributes, String> attributes) {
        for (Map.Entry<Attributes, String> next : attributes.entrySet()) {
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
                case POWER -> {
                    sqlBuilder.append(SELECT_POWER).append(SELECT_AND);
                    queue.add(next.getValue());
                }
            }
        }
        sqlBuilder.delete(sqlBuilder.length() - SELECT_AND.length(), sqlBuilder.length());
    }
}

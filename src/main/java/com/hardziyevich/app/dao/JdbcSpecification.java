package com.hardziyevich.app.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Provides methode for concat sql request and search filter.
 * @param <T>
 */
public interface JdbcSpecification<T> {

    /**
     * Create sql request in database for search elements.
     * @param connection
     * @return
     * @throws SQLException
     */
    List<T> searchFilter(Connection connection) throws SQLException;

    /**
     * Concat sql request
     * @param sql
     */
    void setSql(String sql);
}

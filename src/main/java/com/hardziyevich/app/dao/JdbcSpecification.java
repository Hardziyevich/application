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
     * @param connection connection to data base
     * @return a list of element
     * @throws SQLException a sql exception
     */
    List<T> searchFilter(Connection connection, String sql) throws SQLException;
}

package com.hardziyevich.app.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface JdbcSpecification<T> {

    List<T> searchFilter(Connection connection) throws SQLException;

    void setSql(String sql);
}

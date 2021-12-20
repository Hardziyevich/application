package com.hardziyevich.app.dao;

import com.hardziyevich.app.entity.Capacitors;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface JdbcSpecification {

    List<Capacitors> searchFilter(Connection connection) throws SQLException;

}

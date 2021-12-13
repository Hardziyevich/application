package com.hardziyevich.app.db;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ConnectionPool {

    INSTANCE;

    private static final Logger log = LoggerFactory.getLogger(ConnectionPool.class);

    private static final String PASSWORD_KEY = "db.password";
    private static final String USERNAME_KEY = "db.username";
    private static final String URL_KEY = "db.url";
    private static final String POOL_SIZE = "db.pool";
    private static final Integer DEFAULT_POOL_SIZE = 5;
    private static final String SQL_DRIVER = "org.postgresql.Driver";

    private BlockingQueue<Connection> pool;
    private List<Connection> connections;

    ConnectionPool() {
        loadDriver();
        initConnectionPool();
    }

    private void initConnectionPool() {
        String poolSize = PropertiesUtil.get(POOL_SIZE);
        int size = poolSize == null ? DEFAULT_POOL_SIZE : Integer.parseInt(poolSize);

        pool = new ArrayBlockingQueue<>(size);
        connections = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            final Connection connection;
            try {
                connection = postgreSqlDataSource().getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            Object proxyConnection = Proxy.newProxyInstance(ConnectionPool.class.getClassLoader(), new Class[]{Connection.class},
                    (proxy, method, args) -> method.getName().equals("close") ? pool.add((Connection) proxy) : method.invoke(connection, args));

            pool.add((Connection) proxyConnection);
            connections.add(connection);
        }
    }

    public Connection openConnection() {
        try {
            log.info("Connection was given, stay around {}", pool.size() - 1);
            return pool.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void destroyPool() {
        for (Connection connection : connections) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private DataSource postgreSqlDataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL(PropertiesUtil.get(URL_KEY));
        dataSource.setUser(PropertiesUtil.get(USERNAME_KEY));
        dataSource.setPassword(PropertiesUtil.get(PASSWORD_KEY));
        return dataSource;
    }

    private void loadDriver() {
        try {
            Class.forName(SQL_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

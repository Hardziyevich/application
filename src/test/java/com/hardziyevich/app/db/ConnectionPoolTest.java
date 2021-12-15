package com.hardziyevich.app.db;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.hardziyevich.app.db.ConnectionPool.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConnectionPoolTest {

    @AfterAll
    void close() {
        INSTANCE.destroyPool();
    }

    @Test
    @DisplayName("Test connection")
    void testConnection() {
        String schema = null;
        try (Connection connection = INSTANCE.openConnection()) {
            schema = connection.getSchema();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertEquals("public", schema);
    }

    @Test()
    @DisplayName("All connection will lose")
    void testConnectionPoll() {
        INSTANCE.destroyPool();
        INSTANCE.openConnection();
        assertNotNull(INSTANCE.openConnection());
    }


}

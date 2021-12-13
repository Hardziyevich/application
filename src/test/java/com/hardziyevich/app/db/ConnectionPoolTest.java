package com.hardziyevich.app.db;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.Connection;
import java.sql.SQLException;

import static com.hardziyevich.app.db.ConnectionPool.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        assertEquals("public",schema);
    }


}

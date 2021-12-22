package com.hardziyevich.app.dao;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.hardziyevich.app.dao.impl.ConnectionPool;
import com.hardziyevich.app.dao.impl.ConnectionPoolAbstract;
import com.hardziyevich.app.service.Service;
import com.hardziyevich.app.service.ServiceCapacitor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import static com.hardziyevich.app.dao.TestContainer.InitDB.createDb;
import static com.hardziyevich.app.dao.TestContainer.insertDb;
import static com.hardziyevich.app.dao.impl.ConnectionPoolAbstract.Type.FLEXIBLE;
import static com.hardziyevich.app.dao.impl.ConnectionPoolFabric.PropertiesFile.*;
import static com.hardziyevich.app.dao.impl.ConnectionPoolFabric.PropertiesFile.POOL_SIZE;

@Testcontainers
public class CapacitorDaoImplTest {

    @Container
    public static PostgreSQLContainer<TestContainer> postgreSQLContainer = TestContainer.getContainer();
    public ConnectionPool connectionPool;
    public Map<String, String> attributes = new HashMap<>();
    public static Service service =ServiceCapacitor.getInstance();

    {
        attributes.put(PASSWORD_KEY, postgreSQLContainer.getPassword());
        attributes.put(USERNAME_KEY, postgreSQLContainer.getUsername());
        attributes.put(URL_KEY, postgreSQLContainer.getJdbcUrl());
        attributes.put(POOL_SIZE, "5");
        connectionPool = ConnectionPoolAbstract.connectionPool(FLEXIBLE, attributes);
        try (Connection connection = connectionPool.openConnection();
             Statement statement = connection.createStatement()) {
            System.out.println(statement.execute(createDb));
            int i = statement.executeUpdate(insertDb);

            System.out.println(i);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void createDb() throws SQLException {
        for (JsonObject search : service.search()) {

        }

    }
}

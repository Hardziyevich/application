package com.hardziyevich.app;


import java.sql.Connection;
import java.sql.SQLException;

import static com.hardziyevich.app.db.ConnectionPool.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        try (Connection connection = INSTANCE.openConnection();){
            INSTANCE.name();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            INSTANCE.destroyPool();
        }
    }
}

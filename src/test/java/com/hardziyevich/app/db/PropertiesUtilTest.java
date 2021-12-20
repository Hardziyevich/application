package com.hardziyevich.app.db;

import com.hardziyevich.app.util.PropertiesUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PropertiesUtilTest {

    private static final String PASSWORD_KEY = "db.password";
    private static final String USERNAME_KEY = "db.username";
    private static final String URL_KEY = "db.url";

    @Test
    @DisplayName("Reading property from file.")
    void getResource() {
        assertEquals("das2632337", PropertiesUtil.get(PASSWORD_KEY));
        assertEquals("postgres", PropertiesUtil.get(USERNAME_KEY));
        assertEquals("jdbc:postgresql://localhost:5432/postgres", PropertiesUtil.get(URL_KEY));
    }
}

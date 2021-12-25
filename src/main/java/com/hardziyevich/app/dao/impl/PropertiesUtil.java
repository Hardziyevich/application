package com.hardziyevich.app.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Util class for searching properties in application.properties file.
 */
public class PropertiesUtil {

    private static final Properties PROPERTIES = new Properties();
    private static final String PATH_FILL = "application.properties";

    private static final Logger log = LoggerFactory.getLogger(PropertiesUtil.class);

    static {
        loadProperties();
    }

    private PropertiesUtil() {
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    static void loadProperties() {
        try (InputStream resourceAsStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(PATH_FILL)) {
            PROPERTIES.load(resourceAsStream);
        } catch (IOException e) {
            log.warn("Something wrong {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

}

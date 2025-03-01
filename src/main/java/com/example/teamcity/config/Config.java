package com.example.teamcity.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final String CONFIG_PROPERTIES = "config.properties";
    private static Config config;
    private final Properties properties;

    private Config() {
        properties = new Properties();
        loadProperties(CONFIG_PROPERTIES);
    }

    public static Config getConfig() {
        if (config == null) {
            config = new Config();
        }
        return config;
    }

    private void loadProperties(String fileName) {
        try (InputStream stream = Config.class.getClassLoader().getResourceAsStream(fileName)) {
            if (stream == null) {
                System.err.println("File not found: " + fileName);
                return;
            }
            properties.load(stream);
        } catch (IOException e) {
            System.err.println("Error while reading file: " + fileName);
            throw new RuntimeException(e);
        }
    }

    public static String getProperty(String key) {
        return getConfig().properties.getProperty(key);
    }
}

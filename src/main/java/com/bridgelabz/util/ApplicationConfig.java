package com.bridgelabz.util;



import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationConfig {

    private static ApplicationConfig instance;
    private final Properties properties = new Properties();

    private ApplicationConfig() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (is == null) throw new RuntimeException("application.properties not found");
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties", e);
        }
    }

    public static synchronized ApplicationConfig getInstance() {
        if (instance == null) instance = new ApplicationConfig();
        return instance;
    }

    public String getDbUrl() {
        return properties.getProperty("db.url");
    }

    public String getDbUsername() {
        return properties.getProperty("db.username");
    }

    public String getDbPassword() {
        return properties.getProperty("db.password");
    }

    public String getDbDriver() {
        return properties.getProperty("db.driver");
    }

    public String getRepositoryType() {
        return properties.getProperty("repository.type");
    }
}
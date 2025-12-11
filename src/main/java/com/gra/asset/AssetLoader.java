package com.gra.asset;

import java.io.InputStream;
import java.util.Properties;

public class AssetLoader {
    private Properties properties;

    public AssetLoader() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getDatabaseUrl() {
        return getProperty("db.url");
    }

    public String getDatabaseUser() {
        return getProperty("db.user");
    }

    public String getDatabasePassword() {
        return getProperty("db.password");
    }
}
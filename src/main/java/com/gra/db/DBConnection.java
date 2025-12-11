package com.gra.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnection {
    private static DBConnection instance;
    private Connection connection;

    private DBConnection() throws Exception {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new Exception("Nuk gjendet application.properties");
            }
            props.load(input);
        }

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");
        String driver = props.getProperty("db.driver");

        // Load MySQL driver
        Class.forName(driver);

        // Create connection
        connection = DriverManager.getConnection(url, user, password);
        System.out.println("✅ Database connection vendoset ne: " + url);
    }

    public static DBConnection getInstance() throws Exception {
        if (instance == null) {
            synchronized (DBConnection.class) {
                if (instance == null) {
                    instance = new DBConnection();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            instance = null;
            System.out.println("🔌 Database connection mbyllet.");
        }
    }

    public boolean testConnection() {
        try {
            return connection != null && !connection.isClosed();
        } catch (Exception e) {
            return false;
        }
    }
}
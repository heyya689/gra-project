package com.gra.test;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestXAMPPConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/gra_db";
        String user = "root";
        String password = "";  // Bosh për XAMPP

        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Krijoni lidhjen
            Connection conn = DriverManager.getConnection(url, user, password);

            if (conn != null) {
                System.out.println("✅ U lidhëm me sukses me XAMPP MySQL!");
                System.out.println("Database: " + conn.getMetaData().getDatabaseProductName());
                System.out.println("Version: " + conn.getMetaData().getDatabaseProductVersion());

                // Mbyll lidhjen
                conn.close();
            }
        } catch (Exception e) {
            System.err.println("❌ Gabim në lidhje me XAMPP MySQL:");
            e.printStackTrace();
        }
    }
}
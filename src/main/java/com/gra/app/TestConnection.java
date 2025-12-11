package com.gra.app;

import com.gra.db.DBConnection;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Statement;
import java.sql.ResultSet;

public class TestConnection {
    public static void main(String[] args) {
        try {
            System.out.println("🔧 Testing GRA Database Connection...");
            System.out.println("=====================================");

            // Test connection
            DBConnection db = DBConnection.getInstance();
            Connection conn = db.getConnection();

            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Database connection SUCCESSFUL!");

                // Get database info
                DatabaseMetaData metaData = conn.getMetaData();
                System.out.println("\n📊 DATABASE INFORMATION:");
                System.out.println("Database: " + metaData.getDatabaseProductName());
                System.out.println("Version: " + metaData.getDatabaseProductVersion());
                System.out.println("Driver: " + metaData.getDriverName());
                System.out.println("URL: " + metaData.getURL());
                System.out.println("User: " + metaData.getUserName());

                // Test if we can execute a simple query
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT DATABASE() as db_name");
                if (rs.next()) {
                    System.out.println("Current database: " + rs.getString("db_name"));
                }

                // Count tables
                rs = stmt.executeQuery("SELECT COUNT(*) as table_count FROM information_schema.tables " +
                        "WHERE table_schema = DATABASE()");
                if (rs.next()) {
                    System.out.println("Total tables: " + rs.getInt("table_count"));
                }

                // List tables
                System.out.println("\n📋 LIST OF TABLES:");
                rs = stmt.executeQuery("SHOW TABLES");
                int tableNum = 1;
                while (rs.next()) {
                    System.out.println("  " + tableNum + ". " + rs.getString(1));
                    tableNum++;
                }

                rs.close();
                stmt.close();
                db.closeConnection();

                System.out.println("\n🔌 Connection closed.");
                System.out.println("\n🎉 Database connection test PASSED!");

            } else {
                System.out.println("❌ Database connection FAILED!");
            }

        } catch (Exception e) {
            System.err.println("❌ Database connection FAILED:");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
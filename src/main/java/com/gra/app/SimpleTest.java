package com.gra.app;

import com.gra.dao.UserDAO;
import com.gra.model.User;

public class SimpleTest {
    public static void main(String[] args) {
        try {
            System.out.println("🚀 Starting GRA System Simple Test...");

            UserDAO userDAO = new UserDAO();

            // VETËM TESTO SAVE (mos përdor findByEmail)
            User testUser = new User();
            testUser.setName("Test User");
            testUser.setEmail(System.currentTimeMillis() + "@gra.com");
            testUser.setPassword("test123");

            userDAO.save(testUser);
            System.out.println("✅ User saved with ID: " + testUser.getUserId());

            // Testo vetëm countUsers
            int count = userDAO.countUsers();
            System.out.println("📊 Total users: " + count);

            System.out.println("🎉 Test completed successfully!");

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
}
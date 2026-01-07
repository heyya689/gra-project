package com.gra.tester;

import com.gra.db.DBConnection;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.*;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;

public abstract class BaseDAOTest {

    private static boolean databaseInitialized = false;

    @BeforeEach
    void initDatabase() throws Exception {
        System.setProperty("env", "test");

        // initialize database once per test class
        if (!databaseInitialized) {
            Connection conn = DBConnection.getInstance().getConnection();
            Statement stmt = conn.createStatement();

            try {
                // disable foreign key constraints temporarily
                stmt.execute("SET REFERENTIAL_INTEGRITY FALSE");

                // drop all objects
                stmt.execute("DROP ALL OBJECTS");

                // recreate schema
                RunScript.execute(
                        conn,
                        new InputStreamReader(
                                BaseDAOTest.class
                                        .getClassLoader()
                                        .getResourceAsStream("schema.sql")
                        )
                );

                // re-enable foreign key constraints
                stmt.execute("SET REFERENTIAL_INTEGRITY TRUE");

                databaseInitialized = true;
                System.out.println("Database initialized successfully");

            } catch (Exception e) {
                System.err.println("Database initialization failed: " + e.getMessage());
                throw e;
            } finally {
                if (stmt != null) stmt.close();
            }
        }
    }

    @AfterAll
    static void tearDown() {
        System.clearProperty("env");
        databaseInitialized = false; // reset for next test class
    }
}

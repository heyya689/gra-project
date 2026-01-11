package com.gra.db;

import com.gra.db.DBConnection;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testi i Integrimit: Lidhja me Databazën GRA")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestConnectionJUnit {

    private static Connection conn;
    private static DBConnection dbInstance;

    @BeforeAll
    static void setup() throws Exception {
        // Inicializojmë lidhjen një herë për të gjitha testet
        dbInstance = DBConnection.getInstance();
        conn = dbInstance.getConnection();
    }

    @AfterAll
    static void tearDown() throws Exception {
        // Mbyllim lidhjen në fund
        dbInstance.closeConnection();
    }

    @Test
    @Order(1)
    @DisplayName("Verifikimi i lidhjes aktive")
    void testConnectionIsActive() throws Exception {
        assertNotNull(conn, "Objekti Connection nuk duhet të jetë null");
        assertFalse(conn.isClosed(), "Lidhja duhet të jetë e hapur");
    }

    @Test
    @Order(2)
    @DisplayName("Verifikimi i Metadata-ve të Databazës")
    void testDatabaseMetadata() throws Exception {
        DatabaseMetaData metaData = conn.getMetaData();

        assertAll("Kontrolli i Metadata",
                () -> assertNotNull(metaData.getDatabaseProductName()),
                () -> assertNotNull(metaData.getUserName()),
                () -> assertTrue(metaData.getDatabaseProductVersion().length() > 0)
        );
    }

    @Test
    @Order(3)
    @DisplayName("Ekzekutimi i një Query bazë (SELECT DATABASE)")
    void testSimpleQuery() throws Exception {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DATABASE()")) {

            assertTrue(rs.next(), "Query duhet të kthejë të paktën një rresht");
            assertNotNull(rs.getString(1), "Emri i databazës nuk duhet të jetë null");
        }
    }

}
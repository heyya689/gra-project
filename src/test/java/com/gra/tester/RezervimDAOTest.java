package com.gra.tester;

import com.gra.dao.*;
import com.gra.db.DBConnection;
import com.gra.model.*;

import org.h2.tools.RunScript;
import org.junit.jupiter.api.*;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RezervimDAOTest extends BaseDAOTest {

    private static RezervimDAO rezervimDAO;
    private static UserDAO userDAO;
    private static BiznesDAO biznesDAO;
    private static InventariDAO inventariDAO;

    @BeforeEach
    void setup() throws Exception {
        System.setProperty("env", "test");

        rezervimDAO = new RezervimDAO();
        userDAO = new UserDAO();
        biznesDAO = new BiznesDAO();
        inventariDAO = new InventariDAO();
        clearTables();
    }

    @AfterAll
    static void tearDown() {
        System.clearProperty("env");
    }


    private void clearTables() throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        stmt.execute("DELETE FROM rezervim");
        stmt.execute("DELETE FROM inventari");
        stmt.execute("DELETE FROM biznes");
        stmt.execute("DELETE FROM users");
    }




    //helper methods

    private User insertTestUser() throws Exception {
        User user = new User();
        user.setName("Test");
        user.setEmail("test@user.com");
        user.setPassword("1234");
        userDAO.save(user);
        return user;
    }



    private Inventari insertTestInventari(Biznes biznes) throws Exception {
        Inventari inventari = new Inventari();
        inventari.setBiznes(biznes);
        inventari.setEmerProdukt("Test Produkt");
        inventari.setSasi(5);
        inventariDAO.save(inventari);
        return inventari;
    }



}

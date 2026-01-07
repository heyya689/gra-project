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


    @Test
    void testSaveAndFindById() throws Exception {
        Rezervim rezervim = insertTestRezervim();

        Rezervim fromDb = rezervimDAO.findById(rezervim.getRezervimId());
        assertNotNull(fromDb);
        assertEquals("PENDING", fromDb.getStatus());
        assertEquals(2, fromDb.getNumriPersonave());
    }

    @Test
    void testFindAll() throws Exception {
        insertTestRezervim();
        insertTestRezervim();

        List<Rezervim> list = rezervimDAO.findAll();
        assertEquals(2, list.size());
    }

    @Test
    void testFindByUserId() throws Exception {
        Rezervim rezervim = insertTestRezervim();

        List<Rezervim> list = rezervimDAO.findByUserId(
                rezervim.getUser().getUserId()
        );
        assertEquals(1, list.size());
    }

    @Test
    void testFindByBusinessId() throws Exception {
        Rezervim rezervim = insertTestRezervim();

        List<Rezervim> list = rezervimDAO.findByBusinessId(
                rezervim.getBiznes().getBiznesId()
        );
        assertEquals(1, list.size());
    }

    @Test
    void testUpdateStatus() throws Exception {
        Rezervim rezervim = insertTestRezervim();

        rezervimDAO.updateStatus(rezervim.getRezervimId(), "CONFIRMED");

        Rezervim updated = rezervimDAO.findById(rezervim.getRezervimId());
        assertEquals("CONFIRMED", updated.getStatus());
    }

    @Test
    void testCountReservations() throws Exception {
        insertTestRezervim();
        insertTestRezervim();

        assertEquals(2, rezervimDAO.countReservations());
    }

    @Test
    void testCountByStatus() throws Exception {
        insertTestRezervim();

        assertEquals(1,
                rezervimDAO.countReservationsByStatus("PENDING"));
    }

    @Test
    void testFindByDateRange() throws Exception {
        insertTestRezervim();

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        List<Rezervim> list =
                rezervimDAO.findReservationsByDateRange(start, end);

        assertEquals(1, list.size());
    }

    @Test
    void testDeleteReservation() throws Exception {
        Rezervim rezervim = insertTestRezervim();

        rezervimDAO.delete(rezervim.getRezervimId());

        assertNull(rezervimDAO.findById(rezervim.getRezervimId()));
        assertEquals(0, rezervimDAO.countReservations());
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

    private Biznes insertTestBiznes() throws Exception {
        Biznes biznes = new Biznes();
        biznes.setEmri("Test Biznes");
        biznes.setNipt("TEST-NIPT");
        biznesDAO.save(biznes);
        return biznes;
    }

    private Inventari insertTestInventari(Biznes biznes) throws Exception {
        Inventari inventari = new Inventari();
        inventari.setBiznes(biznes);
        inventari.setEmerProdukt("Test Produkt");
        inventari.setSasi(5);
        inventariDAO.save(inventari);
        return inventari;
    }

    private Rezervim insertTestRezervim() throws Exception {
        User user = insertTestUser();
        Biznes biznes = insertTestBiznes();

        Rezervim rezervim = new Rezervim();
        rezervim.setUser(user);
        rezervim.setBiznes(biznes);
        rezervim.setData(LocalDateTime.now().plusDays(1));
        rezervim.setNumriPersonave(2);
        rezervim.setShenime("Test Rezervim");
        rezervim.setStatus("PENDING");

        rezervimDAO.save(rezervim);
        return rezervim;
    }

}

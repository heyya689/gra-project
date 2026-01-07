package com.gra.tester;

import com.gra.dao.BiznesDAO;
import com.gra.dao.PagesatDAO;
import com.gra.dao.RezervimDAO;
import com.gra.dao.UserDAO;
import com.gra.model.*;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PagesatDAOTest extends BaseDAOTest {

    private PagesatDAO pagesatDAO;
    private RezervimDAO rezervimDAO;
    private UserDAO userDAO;
    private BiznesDAO biznesDAO;

    private static User testUser;
    private static Biznes testBiznes;
    private static Rezervim testRezervim;
    private static boolean dataSetupComplete = false;

    @BeforeEach
    void setupTest() throws Exception {
        // initialize DAOs
        pagesatDAO = new PagesatDAO();
        rezervimDAO = new RezervimDAO();
        userDAO = new UserDAO();
        biznesDAO = new BiznesDAO();

        // create test data once
        if (!dataSetupComplete) {
            testUser = insertTestUser();
            testBiznes = insertTestBiznes();
            testRezervim = insertTestRezervim();
            dataSetupComplete = true;
        }
    }

    @AfterAll
    static void cleanup() {
        dataSetupComplete = false;
    }

    @Test
    void testSaveAndFindById() throws Exception {
        Pagesat p = insertTestPayment("PENDING", 50.0);

        assertNotNull(p.getPagesaId(), "Payment ID should be generated");
        assertTrue(p.getPagesaId() > 0, "Payment ID should be positive");

        Pagesat found = pagesatDAO.findById(p.getPagesaId());

        assertNotNull(found, "Payment should be found");
        assertEquals(50.0, found.getShuma());
        assertEquals("PENDING", found.getStatus());
        assertEquals("CARD", found.getMetoda());
        assertNotNull(found.getRezervim(), "Reservation should be loaded");
    }

    @Test
    void testFindAll() throws Exception {
        insertTestPayment("PENDING", 10.0);
        insertTestPayment("COMPLETED", 20.0);

        List<Pagesat> list = pagesatDAO.findAll();

        assertNotNull(list, "Payment list should not be null");
        assertTrue(list.size() >= 2, "Should have at least 2 payments");
    }

    @Test
    void testFindByReservationId() throws Exception {
        Pagesat p1 = insertTestPayment("COMPLETED", 100.0);
        Pagesat p2 = insertTestPayment("PENDING", 50.0);

        List<Pagesat> payments = pagesatDAO.findByReservationId(testRezervim.getRezervimId());

        assertNotNull(payments);
        assertTrue(payments.size() >= 2);
        assertTrue(payments.stream().anyMatch(p -> p.getPagesaId() == p1.getPagesaId()));
    }

    @Test
    void testFindByStatus() throws Exception {
        insertTestPayment("COMPLETED", 75.0);
        insertTestPayment("COMPLETED", 25.0);

        List<Pagesat> completed = pagesatDAO.findByStatus("COMPLETED");

        assertNotNull(completed);
        assertTrue(completed.size() >= 2);
        assertTrue(completed.stream().allMatch(p -> "COMPLETED".equals(p.getStatus())));
    }

    @Test
    void testUpdate() throws Exception {
        Pagesat p = insertTestPayment("PENDING", 100.0);

        p.setStatus("COMPLETED");
        p.setShuma(150.0);
        p.setMetoda("PAYPAL");

        pagesatDAO.update(p);

        Pagesat updated = pagesatDAO.findById(p.getPagesaId());

        assertEquals("COMPLETED", updated.getStatus());
        assertEquals(150.0, updated.getShuma());
        assertEquals("PAYPAL", updated.getMetoda());
    }

    @Test
    void testCountPayments() throws Exception {
        insertTestPayment("PENDING", 10.0);
        insertTestPayment("COMPLETED", 20.0);

        int count = pagesatDAO.countPayments();

        assertEquals(2, count);
    }

    @Test
    void testCountPaymentsByStatus() throws Exception {
        insertTestPayment("FAILED", 50.0);
        insertTestPayment("FAILED", 30.0);

        int failedCount = pagesatDAO.countPaymentsByStatus("FAILED");
        assertTrue(failedCount >= 2, "Should have at least 2 failed payments");
    }

    @Test
    void testGetTotalRevenue() throws Exception {
        double revenue = pagesatDAO.getTotalRevenue();
        assertTrue(revenue > 0, "Total revenue should be positive");
    }

    @Test
    void testDelete() throws Exception {
        Pagesat p = insertTestPayment("PENDING", 40.0);
        int id = p.getPagesaId();

        pagesatDAO.delete(id);

        Pagesat deleted = pagesatDAO.findById(id);
        assertNull(deleted, "Deleted payment should not be found");
    }

    // helper methods

    private User insertTestUser() throws Exception {
        User u = new User();
        u.setName("Test Payment User");
        u.setEmail("payment.user." + System.nanoTime() + "@test.com");
        u.setPassword("pass123");

        Preferenca pref = new Preferenca();
        pref.setNjoftimeAktive(true);
        pref.setGjuha("EN");
        pref.setTema("LIGHT");
        pref.setEmailNotifications(true);
        pref.setSmsNotifications(false);
        u.setPreferenca(pref);

        userDAO.save(u);
        return u;
    }

    private Biznes insertTestBiznes() throws Exception {
        Biznes b = new Biznes();
        b.setEmri("Payment Test Biznes");
        b.setEmail("payment.biznes." + System.nanoTime() + "@test.com");
        b.setTelefon("123456789");
        b.setNipt("NIPT-" + System.nanoTime());
        b.setPershkrim("Test business for payments");
        biznesDAO.save(b);
        return b;
    }

    private Rezervim insertTestRezervim() throws Exception {
        Rezervim r = new Rezervim();
        r.setBiznes(testBiznes);
        r.setUser(testUser);
        r.setData(LocalDateTime.now().plusDays(1));
        r.setStatus("CONFIRMED");
        r.setNumriPersonave(2);
        rezervimDAO.save(r);
        return r;
    }

    private Pagesat insertTestPayment(String status, double amount) throws Exception {
        Pagesat p = new Pagesat();
        p.setRezervim(testRezervim);
        p.setShuma(amount);
        p.setMetoda("CARD");
        p.setStatus(status);
        p.setTransactionId("TX-" + System.nanoTime());
        p.setPaymentDate(LocalDateTime.now());
        pagesatDAO.save(p);
        return p;
    }
}

package com.gra.tester;

import com.gra.dao.KontaktDAO;
import com.gra.dao.UserDAO;
import com.gra.db.DBConnection;
import com.gra.model.Kontakt;
import com.gra.model.User;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class KontaktDAOTest extends BaseDAOTest{

    private KontaktDAO kontaktDAO;
    private UserDAO userDAO;

    @AfterAll
    static void tearDown() {
        System.clearProperty("env");
    }

    @BeforeEach
    void setup() {
        System.setProperty("env", "test");
        kontaktDAO = new KontaktDAO();
        userDAO = new UserDAO();
    }

    @AfterEach
    void cleanupAll() throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("DELETE FROM kontakt");
        stmt.executeUpdate("DELETE FROM users");
    }

    //findById()
    @Test
    void testFindById() throws Exception {
        User user = createUser("user@test.com");
        Kontakt kontakt = createKontakt( "user@test.com", "Hello", "PENDING");

        Kontakt fromDb = kontaktDAO.findById(kontakt.getKontaktId());

        assertNotNull(fromDb);
        assertEquals("Hello", fromDb.getSubjekti());
        assertEquals("PENDING", fromDb.getStatus());
        assertNotNull(fromDb.getUser());
    }

    //findAll()
    @Test
    void testFindAll() throws Exception {
        createKontakt(  "a@test.com", "A", "PENDING");
        createKontakt(  "b@test.com", "B", "READ");

        List<Kontakt> kontaktet = kontaktDAO.findAll();
        assertEquals(2, kontaktet.size());
    }

    //findByUserId(int)
    @Test
    void testFindByUserId() throws Exception {
        User user = createUser("u@test.com");
        createKontakt( user,"u@test.com", "Msg 1", "PENDING");
        createKontakt( user,"u@test.com", "Msg 2", "READ");

        List<Kontakt> kontaktet = kontaktDAO.findByUserId(user.getUserId());
        assertEquals(2, kontaktet.size());
    }

    //findByEmail(string)
    @Test
    void testFindByEmail() throws Exception {
        createKontakt(  "x@test.com", "Email test", "PENDING");

        List<Kontakt> kontaktet = kontaktDAO.findByEmail("x@test.com");
        assertEquals(1, kontaktet.size());
    }

    //findByStatus(string)
    @Test
    void testFindByStatus() throws Exception {
        createKontakt(  "a@test.com", "A", "PENDING");
        createKontakt(  "b@test.com", "B", "CLOSED");

        List<Kontakt> pending = kontaktDAO.findByStatus("PENDING");
        assertEquals(1, pending.size());
    }

    //findOpenMessages()
    @Test
    void testFindOpenMessages() throws Exception {
        createKontakt(  "a@test.com", "A", "PENDING");
        createKontakt(  "b@test.com", "B", "READ");
        createKontakt(  "c@test.com", "C", "CLOSED");

        List<Kontakt> open = kontaktDAO.findOpenMessages();
        assertEquals(2, open.size());
    }

    //searchBySubject()
    @Test
    void testSearchBySubject() throws Exception {
        createKontakt(  "a@test.com", "Order issue", "PENDING");
        createKontakt(  "b@test.com", "Login problem", "READ");

        List<Kontakt> result = kontaktDAO.searchBySubject("Order");
        assertEquals(1, result.size());
    }

    //update()
    @Test
    void testUpdateKontakt() throws Exception {
        Kontakt kontakt = createKontakt(  "a@test.com", "Old", "PENDING");

        kontakt.setSubjekti("Updated");
        kontakt.setStatus("READ");
        kontaktDAO.update(kontakt);

        Kontakt updated = kontaktDAO.findById(kontakt.getKontaktId());
        assertEquals("Updated", updated.getSubjekti());
        assertEquals("READ", updated.getStatus());
    }

    //markAsRead(int)
    @Test
    void testMarkAsRead() throws Exception {
        Kontakt kontakt = createKontakt(  "a@test.com", "Test", "PENDING");

        kontaktDAO.markAsRead(kontakt.getKontaktId());

        Kontakt updated = kontaktDAO.findById(kontakt.getKontaktId());
        assertEquals("READ", updated.getStatus());
    }

    //markAsReplied(int)
    @Test
    void testMarkAsReplied() throws Exception {
        Kontakt kontakt = createKontakt(  "a@test.com", "Test", "PENDING");

        kontaktDAO.markAsReplied(kontakt.getKontaktId());

        Kontakt updated = kontaktDAO.findById(kontakt.getKontaktId());
        assertEquals("REPLIED", updated.getStatus());
    }

    //markAsClosed(int)
    @Test
    void testMarkAsClosed() throws Exception {
        Kontakt kontakt = createKontakt(  "a@test.com", "Test", "READ");

        kontaktDAO.markAsClosed(kontakt.getKontaktId());

        Kontakt updated = kontaktDAO.findById(kontakt.getKontaktId());
        assertEquals("CLOSED", updated.getStatus());
    }

   //count tests

    @Test
    void testCountMessages() throws Exception {
        createKontakt(  "a@test.com", "A", "PENDING");
        createKontakt(  "b@test.com", "B", "READ");

        assertEquals(2, kontaktDAO.countMessages());
    }

    @Test
    void testCountMessagesByStatus() throws Exception {
        createKontakt(  "a@test.com", "A", "PENDING");
        createKontakt(  "b@test.com", "B", "PENDING");

        assertEquals(2, kontaktDAO.countMessagesByStatus("PENDING"));
    }

    @Test
    void testCountUnreadMessages() throws Exception {
        createKontakt(  "a@test.com", "A", "PENDING");
        createKontakt(  "b@test.com", "B", "READ");

        assertEquals(1, kontaktDAO.countUnreadMessages());
    }

   //delete tests

    @Test
    void testDeleteKontakt() throws Exception {
        Kontakt kontakt = createKontakt(  "a@test.com", "Delete", "PENDING");

        kontaktDAO.delete(kontakt.getKontaktId());

        assertNull(kontaktDAO.findById(kontakt.getKontaktId()));
    }

    @Test
    void testFindLatestMessages() throws Exception {
        createKontakt(  "a@test.com", "1", "PENDING");
        createKontakt(  "b@test.com", "2", "PENDING");
        createKontakt(  "c@test.com", "3", "PENDING");

        List<Kontakt> latest = kontaktDAO.findLatestMessages(2);
        assertEquals(2, latest.size());
    }

    //helper methods

    private User createUser(String email) throws Exception {
        User user = new User();
        user.setName("Test User");
        user.setEmail(email);
        user.setPassword("password");
        userDAO.save(user);
        return user;
    }


    private Kontakt createKontakt(String email, String subject, String status) throws Exception {
        User user = createUser(email);

        Kontakt kontakt = new Kontakt();
        kontakt.setUser(user);
        kontakt.setEmail(email);
        kontakt.setSubjekti(subject);
        kontakt.setMesazh("Test message");
        kontakt.setStatus(status);

        kontaktDAO.save(kontakt);
        return kontakt;
    }

    private Kontakt createKontakt(User user, String email, String subject, String status) throws Exception {

        Kontakt kontakt = new Kontakt();
        kontakt.setUser(user);
        kontakt.setEmail(email);
        kontakt.setSubjekti(subject);
        kontakt.setMesazh("Test message");
        kontakt.setStatus(status);

        kontaktDAO.save(kontakt);
        return kontakt;
    }


}




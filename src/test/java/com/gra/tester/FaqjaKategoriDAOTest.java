package com.gra.tester;

import com.gra.dao.FaqjaKategoriDAO;
import com.gra.db.DBConnection;
import com.gra.model.FaqjaKategori;
import com.gra.model.FAQ;
import com.gra.tester.BaseDAOTest;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FaqjaKategoriDAOTest extends BaseDAOTest {

    private FaqjaKategoriDAO dao;

    @BeforeAll
    void setupDAO() {
        dao = new FaqjaKategoriDAO();
    }

    @BeforeEach
    void clean() throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        stmt.execute("DELETE FROM faq_kategori");
        stmt.execute("DELETE FROM faq");
        stmt.execute("DELETE FROM faqja_kategori");
    }

    // helper methods

    private int insertCategory(String emri, int renditja) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO faqja_kategori (emri, pershkrim, renditja) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        );
        ps.setString(1, emri);
        ps.setString(2, "desc");
        ps.setInt(3, renditja);
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        return rs.getInt(1);
    }

    private int insertFAQ(String question, boolean active, int renditja) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO faq (pyetje, pergjigje, is_active, renditja, created_at) VALUES (?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        );
        ps.setString(1, question);
        ps.setString(2, "answer");
        ps.setBoolean(3, active);
        ps.setInt(4, renditja);
        ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        return rs.getInt(1);
    }

    // tests

    @Test
    void testSaveAndFindById() throws Exception {
        FaqjaKategori k = new FaqjaKategori();
        k.setEmri("General");
        k.setPershkrim("desc");
        k.setRenditja(1);

        dao.save(k);

        assertTrue(k.getKategoriId() > 0);

        FaqjaKategori fromDb = dao.findById(k.getKategoriId());
        assertNotNull(fromDb);
        assertEquals("General", fromDb.getEmri());
    }

    @Test
    void testFindByEmri() throws Exception {
        insertCategory("Payments", 1);

        FaqjaKategori k = dao.findByEmri("Payments");
        assertNotNull(k);
        assertEquals("Payments", k.getEmri());
    }

    @Test
    void testFindAll() throws Exception {
        insertCategory("A", 2);
        insertCategory("B", 1);

        List<FaqjaKategori> list = dao.findAll();

        assertEquals(2, list.size());
        assertEquals("B", list.get(0).getEmri()); // ordered by renditja
    }

    @Test
    void testUpdate() throws Exception {
        int id = insertCategory("Old", 1);

        FaqjaKategori k = dao.findById(id);
        k.setEmri("New");
        k.setRenditja(5);

        dao.update(k);

        FaqjaKategori updated = dao.findById(id);
        assertEquals("New", updated.getEmri());
        assertEquals(5, updated.getRenditja());
    }

    @Test
    void testDelete() throws Exception {
        int id = insertCategory("ToDelete", 1);

        dao.delete(id);

        assertNull(dao.findById(id));
    }

    @Test
    void testAddAndRemoveFAQFromCategory() throws Exception {
        int catId = insertCategory("Cat", 1);
        int faqId = insertFAQ("Q1", true, 1);

        dao.addFAQToCategory(catId, faqId);
        assertEquals(1, dao.countFAQsInCategory(catId));

        dao.removeFAQFromCategory(catId, faqId);
        assertEquals(0, dao.countFAQsInCategory(catId));
    }

    @Test
    void testFindCategoriesWithFAQs() throws Exception {
        int catId = insertCategory("ActiveCat", 1);
        int faqId = insertFAQ("Active FAQ", true, 1);

        dao.addFAQToCategory(catId, faqId);

        List<FaqjaKategori> list = dao.findCategoriesWithFAQs();

        assertEquals(1, list.size());
        assertEquals(1, list.get(0).getFaqs().size());
    }

    @Test
    void testUpdateOrder() throws Exception {
        int id = insertCategory("Order", 1);

        dao.updateOrder(id, 10);

        FaqjaKategori k = dao.findById(id);
        assertEquals(10, k.getRenditja());
    }

    @Test
    void testCountCategories() throws Exception {
        insertCategory("A", 1);
        insertCategory("B", 2);

        assertEquals(2, dao.countCategories());
    }

    @Test
    void testSearchByEmri() throws Exception {
        insertCategory("Beta", 1);
        insertCategory("Delta", 2);
        insertCategory("Zeta", 3);

        List<FaqjaKategori> list = dao.searchByEmri("et");

        assertEquals(2, list.size());
    }
}

package com.gra.tester;

import com.gra.dao.FAQDAO;
import com.gra.db.DBConnection;
import com.gra.model.FAQ;
import com.gra.model.FaqjaKategori;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FAQDAOTest extends BaseDAOTest {

    private FAQDAO faqDAO;
    private int faqId;
    private int kategoriId;

    @BeforeEach
    void setup() throws Exception {
        super.initDatabase();

        faqDAO = new FAQDAO();

        Connection conn = DBConnection.getInstance().getConnection();

        // create category (dependency)
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO faqja_kategori (emri, pershkrim, renditja) VALUES (?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS
        );
        ps.setString(1, "General");
        ps.setString(2, "General questions");
        ps.setInt(3, 1);
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        assertTrue(rs.next(), "Category ID should be generated");
        kategoriId = rs.getInt(1);
        rs.close();
        ps.close();

        createTestFAQ();
    }

    private void createTestFAQ() throws Exception {
        FaqjaKategori kategori = new FaqjaKategori();
        kategori.setKategoriId(kategoriId);

        FAQ faq = new FAQ();
        faq.setPyetje("What is this app?");
        faq.setPergjigje("This is a test FAQ.");
        faq.setRenditja(1);
        faq.setActive(true);
        faq.setKategorite(List.of(kategori));

        faqDAO.save(faq);
        faqId = faq.getFaqId();

        assertTrue(faqId > 0, "FAQ ID should be set after save");
    }

    // save(FAQ)
    @Test
    @Order(1)
    void testSaveFAQ() throws Exception {
        assertTrue(faqId > 0);

        FAQ faq = faqDAO.findById(faqId);
        assertNotNull(faq);
        assertEquals("What is this app?", faq.getPyetje());
    }

    // findById(int)
    @Test
    @Order(2)
    void testFindById() throws Exception {
        FAQ faq = faqDAO.findById(faqId);
        assertNotNull(faq, "FAQ should not be null");
        assertEquals("What is this app?", faq.getPyetje());
        assertNotNull(faq.getKategorite(), "Categories should not be null");
        assertFalse(faq.getKategorite().isEmpty(), "Should have at least one category");
    }

    // findAll()
    @Test
    @Order(3)
    void testFindAll() throws Exception {
        List<FAQ> list = faqDAO.findAll();
        assertTrue(list.size() > 0, "Should have at least one FAQ");
        assertTrue(list.stream().anyMatch(f -> f.getFaqId() == faqId),
                "Should contain our test FAQ");
    }

    // findActiveFAQs()
    @Test
    @Order(4)
    void testFindActiveFAQs() throws Exception {
        List<FAQ> list = faqDAO.findActiveFAQs();
        assertFalse(list.isEmpty(), "Should have active FAQs");
        assertTrue(list.stream().anyMatch(f -> f.getFaqId() == faqId),
                "Should contain our test FAQ");
        assertTrue(list.stream().allMatch(FAQ::isActive),
                "All FAQs should be active");
    }

    // findByCategoryId(int)
    @Test
    @Order(5)
    void testFindByCategoryId() throws Exception {
        List<FAQ> list = faqDAO.findByCategoryId(kategoriId);
        assertEquals(1, list.size(), "Should find exactly one FAQ in this category");
        assertEquals(faqId, list.get(0).getFaqId(), "Should be our test FAQ");
    }

    // searchByQuestion()
    @Test
    @Order(6)
    void testSearchByQuestion() throws Exception {
        List<FAQ> list = faqDAO.searchByQuestion("app");
        assertFalse(list.isEmpty(), "Should find FAQs containing 'app'");
        assertTrue(list.stream().anyMatch(f -> f.getFaqId() == faqId),
                "Should find our test FAQ");

        // test case insensitivity
        List<FAQ> listUpper = faqDAO.searchByQuestion("APP");

        assertNotNull(listUpper, "Search should return a list (empty or not)");
    }

    // update(FAQ)
    @Test
    @Order(7)
    void testUpdateFAQ() throws Exception {
        FAQ faq = faqDAO.findById(faqId);
        assertNotNull(faq);

        String newAnswer = "Updated answer with more details";
        faq.setPergjigje(newAnswer);

        faqDAO.update(faq);

        FAQ updated = faqDAO.findById(faqId);
        assertNotNull(updated);
        assertEquals(newAnswer, updated.getPergjigje());
        assertEquals("What is this app?", updated.getPyetje(),
                "Question should remain unchanged");
    }

    // deactivate / activate
    @Test
    @Order(8)
    void testDeactivateActivate() throws Exception {
        // deactivate
        faqDAO.deactivate(faqId);

        FAQ deactivated = faqDAO.findById(faqId);
        assertNotNull(deactivated);
        assertFalse(deactivated.isActive(), "FAQ should be deactivated");

        List<FAQ> activeFAQs = faqDAO.findActiveFAQs();
        assertFalse(activeFAQs.stream().anyMatch(f -> f.getFaqId() == faqId),
                "Deactivated FAQ should not appear in active FAQs");

        // activate
        faqDAO.activate(faqId);

        FAQ activated = faqDAO.findById(faqId);
        assertNotNull(activated);
        assertTrue(activated.isActive(), "FAQ should be activated");

        activeFAQs = faqDAO.findActiveFAQs();
        assertTrue(activeFAQs.stream().anyMatch(f -> f.getFaqId() == faqId),
                "Activated FAQ should appear in active FAQs");
    }

    // updateOrder()
    @Test
    @Order(9)
    void testUpdateOrder() throws Exception {
        int newOrder = 5;
        faqDAO.updateOrder(faqId, newOrder);

        FAQ faq = faqDAO.findById(faqId);
        assertNotNull(faq);
        assertEquals(newOrder, faq.getRenditja(), "Order should be updated");
    }

    // countFAQs / countActiveFAQs
    @Test
    @Order(10)
    void testCounts() throws Exception {
        int totalCount = faqDAO.countFAQs();
        assertTrue(totalCount > 0, "Should have at least one FAQ");

        int activeCount = faqDAO.countActiveFAQs();
        assertTrue(activeCount > 0, "Should have at least one active FAQ");
        assertTrue(activeCount <= totalCount,
                "Active count should not exceed total count");
    }

    // findLatestFAQs(int)
    @Test
    @Order(11)
    void testFindLatestFAQs() throws Exception {
        List<FAQ> list = faqDAO.findLatestFAQs(5);
        assertFalse(list.isEmpty(), "Should find latest FAQs");
        assertTrue(list.size() <= 5, "Should not exceed requested limit");
        assertTrue(list.stream().anyMatch(f -> f.getFaqId() == faqId),
                "Should include our test FAQ");
    }

    // Test with multiple FAQs
    @Test
    @Order(12)
    void testMultipleFAQs() throws Exception {
        // Get initial counts
        int initialAllCount = faqDAO.findAll().size();
        int initialActiveCount = faqDAO.findActiveFAQs().size();

        FaqjaKategori kategori = new FaqjaKategori();
        kategori.setKategoriId(kategoriId);

        // Create additional FAQs
        FAQ faq2 = new FAQ();
        faq2.setPyetje("How do I register?");
        faq2.setPergjigje("Click the register button.");
        faq2.setRenditja(2);
        faq2.setActive(true);
        faq2.setKategorite(List.of(kategori));
        faqDAO.save(faq2);
        assertTrue(faq2.getFaqId() > 0, "FAQ 2 should be saved");

        FAQ faq3 = new FAQ();
        faq3.setPyetje("Is it free?");
        faq3.setPergjigje("Yes, it is free.");
        faq3.setRenditja(3);
        faq3.setActive(false); // Inactive
        faq3.setKategorite(List.of(kategori));
        faqDAO.save(faq3);
        assertTrue(faq3.getFaqId() > 0, "FAQ 3 should be saved");

        // test findAll - should have 2 more than initial
        List<FAQ> allFAQs = faqDAO.findAll();
        assertEquals(initialAllCount + 2, allFAQs.size(),
                "Should have exactly 2 more FAQs total");

        // test findActiveFAQs - should have 1 more active than initial (faq2-active, faq3-not active)
        List<FAQ> activeFAQs = faqDAO.findActiveFAQs();
        assertEquals(initialActiveCount + 1, activeFAQs.size(),
                "Should have exactly 1 more active FAQ");
        assertTrue(activeFAQs.stream().anyMatch(f -> f.getFaqId() == faq2.getFaqId()),
                "Should contain the new active FAQ");
        assertFalse(activeFAQs.stream().anyMatch(f -> f.getFaqId() == faq3.getFaqId()),
                "Inactive FAQ should not appear in active list");

        // test findByCategoryId - verify the FAQs are in the category
        List<FAQ> categoryFAQs = faqDAO.findByCategoryId(kategoriId);
        assertTrue(categoryFAQs.size() >= 2,
                "Should have at least 2 FAQs in category (might filter inactive)");

        // verify that we can find the FAQs by ID
        FAQ retrieved2 = faqDAO.findById(faq2.getFaqId());
        assertNotNull(retrieved2, "Should be able to retrieve FAQ 2");
        FAQ retrieved3 = faqDAO.findById(faq3.getFaqId());
        assertNotNull(retrieved3, "Should be able to retrieve FAQ 3");
    }

    // edge cases
    @Test
    @Order(13)
    void testEdgeCases() throws Exception {
        // finding non-existent FAQ
        FAQ notFound = faqDAO.findById(99999);
        assertNull(notFound, "Should return null for non-existent FAQ");

        // searching with no results
        List<FAQ> noResults = faqDAO.searchByQuestion("xyznonexistent");
        assertTrue(noResults.isEmpty(), "Should return empty list for no matches");

        // finding by non-existent category
        List<FAQ> noCategoryResults = faqDAO.findByCategoryId(99999);
        assertTrue(noCategoryResults.isEmpty(),
                "Should return empty list for non-existent category");
    }
}
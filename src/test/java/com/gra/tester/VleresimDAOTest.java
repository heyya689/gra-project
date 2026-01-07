package com.gra.tester;

import com.gra.dao.BiznesDAO;
import com.gra.dao.UserDAO;
import com.gra.dao.VleresimDAO;
import com.gra.db.DBConnection;
import com.gra.model.Biznes;
import com.gra.model.User;
import com.gra.model.Vleresim;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VleresimDAOTest extends BaseDAOTest{

    private static VleresimDAO vleresimDAO;
    private static UserDAO userDAO;
    private static BiznesDAO biznesDAO;

    private static User testUser;
    private static Biznes testBiznes;


    @BeforeEach
    void setup () throws Exception {
        System.setProperty("env", "test");

        vleresimDAO = new VleresimDAO();
        userDAO = new UserDAO();
        biznesDAO = new BiznesDAO();

        clearTables();

        testUser = insertTestUser();
        testBiznes = insertTestBiznes();
    }

    @AfterAll
    static void tearDown() {
        System.clearProperty("env");
    }

    @AfterEach
    void cleanupAfterTest() throws Exception {
        clearReviews();
    }


    @Test
    void testSaveAndFindById() throws Exception {
        Vleresim v = createTestVleresim(false, 5);
        vleresimDAO.save(v);

        assertTrue(v.getVleresimId() > 0);

        Vleresim fromDb = vleresimDAO.findById(v.getVleresimId());
        assertNotNull(fromDb);
        assertEquals(5, fromDb.getRating());
        assertEquals(testUser.getUserId(), fromDb.getUser().getUserId());
        assertEquals(testBiznes.getBiznesId(), fromDb.getBiznes().getBiznesId());
    }

    @Test
    void testFindByUserId() throws Exception {
        vleresimDAO.save(createTestVleresim(true, 4));
        vleresimDAO.save(createTestVleresim(false, 5));

        List<Vleresim> list = vleresimDAO.findByUserId(testUser.getUserId());
        assertEquals(2, list.size());
    }

    @Test
    void testFindByBusinessId() throws Exception {
        vleresimDAO.save(createTestVleresim(true, 3));
        vleresimDAO.save(createTestVleresim(true, 4));

        List<Vleresim> list = vleresimDAO.findByBusinessId(testBiznes.getBiznesId());
        assertEquals(2, list.size());
    }

    @Test
    void testApproveAndRejectReview() throws Exception {
        Vleresim v = createTestVleresim(false, 5);
        vleresimDAO.save(v);

        vleresimDAO.approveReview(v.getVleresimId());
        Vleresim approved = vleresimDAO.findById(v.getVleresimId());
        assertTrue(approved.isApproved());

        vleresimDAO.rejectReview(v.getVleresimId());
        Vleresim rejected = vleresimDAO.findById(v.getVleresimId());
        assertFalse(rejected.isApproved());
    }

    @Test
    void testCountReviews() throws Exception {
        vleresimDAO.save(createTestVleresim(true, 5));
        vleresimDAO.save(createTestVleresim(false, 4));

        assertEquals(2, vleresimDAO.countTotalReviews());
        assertEquals(1, vleresimDAO.countPendingReviews());
        assertEquals(2, vleresimDAO.countReviewsByUserId(testUser.getUserId()));
    }

    @Test
    void testAverageRatingByBusiness() throws Exception {
        vleresimDAO.save(createTestVleresim(true, 4));
        vleresimDAO.save(createTestVleresim(true, 2));
        vleresimDAO.save(createTestVleresim(false, 5)); // ignored

        double avg = vleresimDAO.getAverageRatingByBusinessId(testBiznes.getBiznesId());
        assertEquals(3.0, avg);
    }

    @Test
    void testDeleteReview() throws Exception {
        Vleresim v = createTestVleresim(true, 5);
        vleresimDAO.save(v);

        vleresimDAO.delete(v.getVleresimId());

        assertNull(vleresimDAO.findById(v.getVleresimId()));
        assertEquals(0, vleresimDAO.countTotalReviews());
    }

    @Test
    void testSearchByComment() throws Exception {
        vleresimDAO.save(createTestVleresim(true, 5));
        Vleresim v = createTestVleresim(true, 4);
        v.setKoment("Amazing food");
        vleresimDAO.save(v);

        List<Vleresim> results = vleresimDAO.searchByComment("food");
        assertEquals(1, results.size());
    }

    //helper methods

    private static void clearTables() throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();

        stmt.execute("delete from vleresim");
        stmt.execute("delete from users");
        stmt.execute("delete from biznes");
    }

    private static void clearReviews() throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        stmt.execute("delete from vleresim");
    }

    private static User insertTestUser() throws Exception {
        User u = new User();
        u.setName("Test User");
        u.setEmail("test@test.com");
        u.setPassword("123456");
        userDAO.save(u);
        return u;
    }

    private static Biznes insertTestBiznes() throws Exception {
        Biznes b = new Biznes();
        b.setEmri("Test Biznes");
        b.setPershkrim("Test Description");
        b.setNipt("TEST-NIPT");
        biznesDAO.save(b);
        return b;
    }

    private Vleresim createTestVleresim(boolean approved, int rating) {
        Vleresim v = new Vleresim();
        v.setUser(testUser);
        v.setBiznes(testBiznes);
        v.setRating(rating);
        v.setKoment("Very good service");
        v.setApproved(approved);
        return v;
    }

}

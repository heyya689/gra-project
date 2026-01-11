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



    @AfterAll
    static void tearDown() {
        System.clearProperty("env");
    }

    @AfterEach
    void cleanupAfterTest() throws Exception {
        clearReviews();
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

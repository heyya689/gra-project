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


    @AfterAll
    static void cleanup() {
        dataSetupComplete = false;
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

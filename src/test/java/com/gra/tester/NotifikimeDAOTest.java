package com.gra.tester;

import com.gra.dao.NotifikimeDAO;
import com.gra.dao.UserDAO;
import com.gra.model.Notifikime;
import com.gra.model.Preferenca;
import com.gra.model.User;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NotifikimeDAOTest extends BaseDAOTest {

    private NotifikimeDAO notifikimeDAO;
    private UserDAO userDAO;

    private static User testUser;
    private static User testUser2;
    private static boolean dataSetupComplete = false;

    @BeforeEach
    void setupTest() throws Exception {
        notifikimeDAO = new NotifikimeDAO();
        userDAO = new UserDAO();

        if (!dataSetupComplete) {
            testUser = insertTestUser("notif.user1");
            testUser2 = insertTestUser("notif.user2");
            dataSetupComplete = true;
        }
    }

    @AfterAll
    static void cleanup() {
        dataSetupComplete = false;
    }

    @Test
    void testSaveAndFindById() throws Exception {
        Notifikime notif = createNotification(testUser, "Test Title", "Test Message", "INFO");
        notifikimeDAO.save(notif);

        assertNotNull(notif.getNjoftimId(), "Notification ID should be generated");
        assertTrue(notif.getNjoftimId() > 0, "Notification ID should be positive");

        Notifikime found = notifikimeDAO.findById(notif.getNjoftimId());

        assertNotNull(found, "Notification should be found");
        assertEquals("Test Title", found.getTitulli());
        assertEquals("Test Message", found.getMesazh());
        assertEquals("INFO", found.getTipi());
        assertFalse(found.isLexuar(), "Notification should be unread by default");
        assertNotNull(found.getUser(), "User should be loaded");
        assertEquals(testUser.getUserId(), found.getUser().getUserId());
    }

    @Test
    void testFindAll() throws Exception {
        Notifikime n1 = createNotification(testUser, "Title 1", "Message 1", "INFO");
        Notifikime n2 = createNotification(testUser2, "Title 2", "Message 2", "WARNING");

        notifikimeDAO.save(n1);
        notifikimeDAO.save(n2);

        List<Notifikime> all = notifikimeDAO.findAll();

        assertNotNull(all, "Notification list should not be null");
        assertTrue(all.size() >= 2, "Should have at least 2 notifications");
    }

    @Test
    void testFindByUserId() throws Exception {
        Notifikime n1 = createNotification(testUser, "User Notif 1", "Msg 1", "INFO");
        Notifikime n2 = createNotification(testUser, "User Notif 2", "Msg 2", "SUCCESS");

        notifikimeDAO.save(n1);
        notifikimeDAO.save(n2);

        List<Notifikime> userNotifications = notifikimeDAO.findByUserId(testUser.getUserId());

        assertNotNull(userNotifications);
        assertTrue(userNotifications.size() >= 2, "User should have at least 2 notifications");
        assertTrue(userNotifications.stream()
                .allMatch(n -> n.getUser().getUserId() == testUser.getUserId()));
    }

    @Test
    void testFindUnreadByUserId() throws Exception {
        Notifikime n1 = createNotification(testUser, "Unread 1", "Message", "INFO");
        n1.setLexuar(false);
        notifikimeDAO.save(n1);

        Notifikime n2 = createNotification(testUser, "Read", "Message", "INFO");
        n2.setLexuar(true);
        notifikimeDAO.save(n2);

        List<Notifikime> unread = notifikimeDAO.findUnreadByUserId(testUser.getUserId());

        assertNotNull(unread);
        assertTrue(unread.stream().noneMatch(Notifikime::isLexuar),
                "All returned notifications should be unread");
    }

    @Test
    void testFindByType() throws Exception {
        Notifikime n1 = createNotification(testUser, "Alert 1", "Message", "ALERT");
        Notifikime n2 = createNotification(testUser2, "Alert 2", "Message", "ALERT");

        notifikimeDAO.save(n1);
        notifikimeDAO.save(n2);

        List<Notifikime> alerts = notifikimeDAO.findByType("ALERT");

        assertNotNull(alerts);
        assertTrue(alerts.size() >= 2);
        assertTrue(alerts.stream().allMatch(n -> "ALERT".equals(n.getTipi())));
    }

    @Test
    void testUpdate() throws Exception {
        Notifikime notif = createNotification(testUser, "Original Title", "Original Message", "INFO");
        notifikimeDAO.save(notif);

        notif.setTitulli("Updated Title");
        notif.setMesazh("Updated Message");
        notif.setTipi("WARNING");
        notif.setLexuar(true);

        notifikimeDAO.update(notif);

        Notifikime updated = notifikimeDAO.findById(notif.getNjoftimId());

        assertEquals("Updated Title", updated.getTitulli());
        assertEquals("Updated Message", updated.getMesazh());
        assertEquals("WARNING", updated.getTipi());
        assertTrue(updated.isLexuar());
    }

    @Test
    void testMarkAsRead() throws Exception {
        Notifikime notif = createNotification(testUser, "To Mark Read", "Message", "INFO");
        notif.setLexuar(false);
        notifikimeDAO.save(notif);

        notifikimeDAO.markAsRead(notif.getNjoftimId());

        Notifikime marked = notifikimeDAO.findById(notif.getNjoftimId());
        assertTrue(marked.isLexuar(), "Notification should be marked as read");
    }

    @Test
    void testMarkAsUnread() throws Exception {
        Notifikime notif = createNotification(testUser, "To Mark Unread", "Message", "INFO");
        notif.setLexuar(true);
        notifikimeDAO.save(notif);

        notifikimeDAO.markAsUnread(notif.getNjoftimId());

        Notifikime marked = notifikimeDAO.findById(notif.getNjoftimId());
        assertFalse(marked.isLexuar(), "Notification should be marked as unread");
    }

    @Test
    void testMarkAllAsRead() throws Exception {
        Notifikime n1 = createNotification(testUser, "Unread 1", "Msg", "INFO");
        Notifikime n2 = createNotification(testUser, "Unread 2", "Msg", "INFO");
        n1.setLexuar(false);
        n2.setLexuar(false);

        notifikimeDAO.save(n1);
        notifikimeDAO.save(n2);

        notifikimeDAO.markAllAsRead(testUser.getUserId());

        List<Notifikime> unread = notifikimeDAO.findUnreadByUserId(testUser.getUserId());
        assertEquals(0, unread.size(), "User should have no unread notifications");
    }

    @Test
    void testCountNotifications() throws Exception {
        int count = notifikimeDAO.countNotifications();
        assertTrue(count > 0, "Should have notifications");
    }

    @Test
    void testCountUnreadNotifications() throws Exception {
        Notifikime n = createNotification(testUser, "Unread Count Test", "Msg", "INFO");
        n.setLexuar(false);
        notifikimeDAO.save(n);

        int unreadCount = notifikimeDAO.countUnreadNotifications(testUser.getUserId());
        assertTrue(unreadCount > 0, "Should have unread notifications");
    }

    @Test
    void testCountNotificationsByType() throws Exception {
        Notifikime n1 = createNotification(testUser, "Error 1", "Msg", "ERROR");
        Notifikime n2 = createNotification(testUser2, "Error 2", "Msg", "ERROR");

        notifikimeDAO.save(n1);
        notifikimeDAO.save(n2);

        int errorCount = notifikimeDAO.countNotificationsByType("ERROR");
        assertTrue(errorCount >= 2, "Should have at least 2 ERROR notifications");
    }

    @Test
    void testSendNotificationToUser() throws Exception {
        int beforeCount = notifikimeDAO.findByUserId(testUser.getUserId()).size();

        notifikimeDAO.sendNotificationToUser(
                testUser.getUserId(),
                "Direct Notification",
                "This is a direct message",
                "DIRECT"
        );

        int afterCount = notifikimeDAO.findByUserId(testUser.getUserId()).size();
        assertEquals(beforeCount + 1, afterCount, "User should have one more notification");

        List<Notifikime> userNotifs = notifikimeDAO.findByUserId(testUser.getUserId());
        assertTrue(userNotifs.stream()
                .anyMatch(n -> "Direct Notification".equals(n.getTitulli())));
    }

    @Test
    void testSendNotificationToAllUsers() throws Exception {
        int user1Before = notifikimeDAO.findByUserId(testUser.getUserId()).size();
        int user2Before = notifikimeDAO.findByUserId(testUser2.getUserId()).size();

        notifikimeDAO.sendNotificationToAllUsers(
                "Broadcast Message",
                "This message goes to everyone",
                "BROADCAST"
        );

        int user1After = notifikimeDAO.findByUserId(testUser.getUserId()).size();
        int user2After = notifikimeDAO.findByUserId(testUser2.getUserId()).size();

        assertEquals(user1Before + 1, user1After, "User 1 should receive notification");
        assertEquals(user2Before + 1, user2After, "User 2 should receive notification");
    }

    @Test
    void testDelete() throws Exception {
        Notifikime notif = createNotification(testUser, "To Delete", "Message", "INFO");
        notifikimeDAO.save(notif);
        int id = notif.getNjoftimId();

        notifikimeDAO.delete(id);

        Notifikime deleted = notifikimeDAO.findById(id);
        assertNull(deleted, "Deleted notification should not be found");
    }

    // helper methods

    private User insertTestUser(String prefix) throws Exception {
        User u = new User();
        u.setName(prefix + " User");
        u.setEmail(prefix + "." + System.nanoTime() + "@test.com");
        u.setPassword("password123");

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

    private Notifikime createNotification(User user, String title, String message, String type) {
        Notifikime notif = new Notifikime();
        notif.setUser(user);
        notif.setTitulli(title);
        notif.setMesazh(message);
        notif.setTipi(type);
        notif.setLexuar(false);
        notif.setData(LocalDateTime.now());
        return notif;
    }
}

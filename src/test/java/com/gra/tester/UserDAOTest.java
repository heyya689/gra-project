package com.gra.tester;

import com.gra.dao.UserDAO;
import com.gra.db.DBConnection;
import com.gra.model.User;
import com.gra.model.Preferenca;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDAOTest extends BaseDAOTest {

    private UserDAO userDAO;
    private static int testUserId;

    @BeforeEach
    void setup() throws Exception {
        super.initDatabase();
        System.setProperty("env", "test");
        userDAO = new UserDAO();
    }

    @AfterAll
    static void cleanupAll() throws Exception {
        if (testUserId > 0) {
            try {
                UserDAO dao = new UserDAO();
                dao.delete(testUserId);
            } catch (Exception e) {
                System.err.println("Cleanup warning: " + e.getMessage());
            }
        }
    }

    // save(User)
    @Test
    @Order(1)
    void testSaveUserWithPreferences() throws Exception {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test.user@email.com");
        user.setPassword("password123");

        Preferenca preferenca = new Preferenca();
        preferenca.setNjoftimeAktive(true);
        preferenca.setGjuha("EN");
        preferenca.setTema("DARK");
        preferenca.setEmailNotifications(true);
        preferenca.setSmsNotifications(false);

        user.setPreferenca(preferenca);

        userDAO.save(user);

        assertTrue(user.getUserId() > 0, "User ID should be generated");
        assertNotNull(user.getPreferenca(), "Preferences should be saved");
        assertTrue(user.getPreferenca().getPreferencaId() > 0, "Preference ID should be generated");

        testUserId = user.getUserId();
    }

    // findById(int)
    @Test
    @Order(2)
    void testFindById() throws Exception {
        User user = userDAO.findById(testUserId);

        assertNotNull(user, "User should be found");
        assertEquals(testUserId, user.getUserId());
        assertEquals("Test User", user.getName());
        assertNotNull(user.getPreferenca(), "Preferences should be loaded");
    }

    // findByEmail(String)
    @Test
    @Order(3)
    void testFindByEmail() throws Exception {
        User user = userDAO.findByEmail("test.user@email.com");

        assertNotNull(user, "User should be found by email");
        assertEquals(testUserId, user.getUserId());
        assertEquals("test.user@email.com", user.getEmail());
    }

    // findAll()
    @Test
    @Order(4)
    void testFindAll() throws Exception {
        List<User> users = userDAO.findAll();

        assertNotNull(users, "User list should not be null");
        assertTrue(users.size() > 0, "Should have at least one user");
    }

    // findByRole(String)
    @Test
    @Order(5)
    void testFindByRole() throws Exception {
        assignRoleToUser(testUserId, "USER");

        List<User> users = userDAO.findByRole("USER");

        assertNotNull(users, "User list should not be null");
        assertTrue(
                users.stream().anyMatch(u -> u.getUserId() == testUserId),
                "Test user should have USER role"
        );
    }

    // update(User)
    @Test
    @Order(6)
    void testUpdateUserAndPreferences() throws Exception {
        User user = userDAO.findById(testUserId);
        assertNotNull(user, "User should exist before update");

        user.setName("Updated Name");
        user.setPassword("newPassword");

        Preferenca p = user.getPreferenca();
        p.setTema("DARK");
        p.setEmailNotifications(false);

        userDAO.update(user);

        User updated = userDAO.findById(testUserId);

        assertEquals("Updated Name", updated.getName());
        assertEquals("DARK", updated.getPreferenca().getTema());
        assertFalse(updated.getPreferenca().isEmailNotifications());
    }

    // emailExists(String)
    @Test
    @Order(7)
    void testEmailExists() throws Exception {
        assertTrue(userDAO.emailExists("test.user@email.com"), "Email should exist");
        assertFalse(userDAO.emailExists("doesnotexist@email.com"), "Non-existent email should return false");
    }

    // countUsers()
    @Test
    @Order(8)
    void testCountUsers() throws Exception {
        int count = userDAO.countUsers();
        assertTrue(count > 0, "Should have at least one user");
    }

    // delete(int)
    @Test
    @Order(9)
    void testDeleteUser() throws Exception {
        int tempUserId = createTemporaryUser();

        userDAO.delete(tempUserId);

        User deleted = userDAO.findById(tempUserId);
        assertNull(deleted, "Deleted user should not be found");
    }

    // helper methods

    private int createTemporaryUser() throws Exception {
        User user = new User();
        user.setName("Temp User");
        user.setEmail("temp@email.com");
        user.setPassword("temp");

        Preferenca preferenca = new Preferenca();
        preferenca.setNjoftimeAktive(false);
        preferenca.setGjuha("EN");
        preferenca.setTema("LIGHT");
        preferenca.setEmailNotifications(false);
        preferenca.setSmsNotifications(false);

        user.setPreferenca(preferenca);

        userDAO.save(user);
        return user.getUserId();
    }

    private void assignRoleToUser(int userId, String roleName) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();

        // ensure role exists
        int roleId = ensureRoleExists(roleName, conn);

        // link user to role (check if already exists to avoid duplicates)
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO user_role (user_id, role_id) VALUES (?, ?)")) {
            ps.setInt(1, userId);
            ps.setInt(2, roleId);
            ps.executeUpdate();
        }
    }

    private int ensureRoleExists(String roleName, Connection conn) throws Exception {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT role_id FROM role WHERE emri = ?")) {
            ps.setString(1, roleName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("role_id");
            }
        }

        // ff role does not exist then create it
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO role (emri) VALUES (?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, roleName);
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
        }

        throw new IllegalStateException("Failed to create role: " + roleName);
    }
}

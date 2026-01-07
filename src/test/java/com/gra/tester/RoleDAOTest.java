package com.gra.tester;

import com.gra.dao.RoleDAO;
import com.gra.dao.UserDAO;
import com.gra.db.DBConnection;
import com.gra.model.Role;
import com.gra.model.User;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RoleDAOTest extends BaseDAOTest{

    private RoleDAO roleDAO;
    private UserDAO userDAO;

    @BeforeEach
    void setup() throws Exception {
        System.setProperty("env", "test");

        roleDAO = new RoleDAO();
        userDAO = new UserDAO();
        clearTables();
    }

    @AfterAll
    static void tearDown() {
        System.clearProperty("env");
    }


    private void clearTables() throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        stmt.execute("delete from user_role");
        stmt.execute("delete from role");
        stmt.execute("delete from users");
    }

    //helper methods

    private Role insertTestRole(String name) throws Exception {
        Role role = new Role();
        role.setEmri(name);
        role.setDescription(name + " description");
        role.setPermissions("READ,WRITE");
        roleDAO.save(role);
        return role;
    }

    private User insertTestUser() throws Exception {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@user.com");
        user.setPassword("1234");
        userDAO.save(user);
        return user;
    }

    @Test
    void testSaveAndFindById() throws Exception {
        Role role = insertTestRole("ADMIN");

        Role fromDb = roleDAO.findById(role.getRoleId());
        assertNotNull(fromDb);
        assertEquals("ADMIN", fromDb.getEmri());
    }

    @Test
    void testFindByEmri() throws Exception {
        insertTestRole("USER");

        Role role = roleDAO.findByEmri("USER");
        assertNotNull(role);
        assertEquals("USER", role.getEmri());
    }

    @Test
    void testFindAll() throws Exception {
        insertTestRole("ADMIN");
        insertTestRole("USER");

        List<Role> roles = roleDAO.findAll();
        assertEquals(2, roles.size());
    }

    @Test
    void testUpdateRole() throws Exception {
        Role role = insertTestRole("EDITOR");

        role.setDescription("Updated description");
        roleDAO.update(role);

        Role updated = roleDAO.findById(role.getRoleId());
        assertEquals("Updated description", updated.getDescription());
    }

    @Test
    void testAssignRoleToUser() throws Exception {
        Role role = insertTestRole("ADMIN");
        User user = insertTestUser();

        roleDAO.assignRoleToUser(user.getUserId(), role.getRoleId());

        List<Role> roles = roleDAO.findRolesByUserId(user.getUserId());
        assertEquals(1, roles.size());
        assertEquals("ADMIN", roles.get(0).getEmri());
    }

    @Test
    void testRemoveRoleFromUser() throws Exception {
        Role role = insertTestRole("USER");
        User user = insertTestUser();

        roleDAO.assignRoleToUser(user.getUserId(), role.getRoleId());
        roleDAO.removeRoleFromUser(user.getUserId(), role.getRoleId());

        List<Role> roles = roleDAO.findRolesByUserId(user.getUserId());
        assertEquals(0, roles.size());
    }

    @Test
    void testFindUsersByRoleId() throws Exception {
        Role role = insertTestRole("ADMIN");
        User user = insertTestUser();

        roleDAO.assignRoleToUser(user.getUserId(), role.getRoleId());

        List<User> users = roleDAO.findUsersByRoleId(role.getRoleId());
        assertEquals(1, users.size());
        assertEquals(user.getEmail(), users.get(0).getEmail());
    }

    @Test
    void testDeleteRole() throws Exception {
        Role role = insertTestRole("TEMP");

        roleDAO.delete(role.getRoleId());

        assertNull(roleDAO.findById(role.getRoleId()));
    }
}

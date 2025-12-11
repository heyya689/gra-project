package com.gra.tester;

import com.gra.dao.UserDAO;
import com.gra.model.User;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDAOTest {
    private UserDAO userDAO;

    @BeforeAll
    public void setup() throws Exception {
        userDAO = new UserDAO();
    }

    @Test
    public void testFindById() throws Exception {
        User user = userDAO.findById(1);
        assertNotNull(user);
        assertEquals(1, user.getUserId());
    }

    @Test
    public void testFindByEmail() throws Exception {
        User user = userDAO.findByEmail("test@example.com");
        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    public void testSaveAndDelete() throws Exception {
        User newUser = new User(0, "Test User", "new@example.com", "password", "CLIENT");
        userDAO.save(newUser);
        User found = userDAO.findByEmail("new@example.com");
        assertNotNull(found);
        userDAO.delete(found.getUserId());
        User deleted = userDAO.findByEmail("new@example.com");
        assertNull(deleted);
    }
}
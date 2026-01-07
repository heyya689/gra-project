package com.gra.tester;

import com.gra.dao.LokacionDAO;
import com.gra.dao.UserDAO;
import com.gra.db.DBConnection;
import com.gra.model.Lokacion;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.*;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LokacionDAOTest extends BaseDAOTest{

    private static LokacionDAO lokacionDAO;

    @BeforeEach
    void setup () {
        System.setProperty("env", "test");
        lokacionDAO = new LokacionDAO();
    }

    @BeforeEach
    void clearTable() throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        conn.createStatement().execute("DELETE FROM lokacion");
    }

    @AfterAll
    static void tearDown() {
        System.clearProperty("env");
    }


    @Test
    void testSaveAndFindById() throws Exception {
        Lokacion saved = insertTestLokacion(
                "Tirane", "Blloku", "1001", 41.3275, 19.8187
        );

        assertNotNull(saved.getLokacionId());

        Lokacion found = lokacionDAO.findById(saved.getLokacionId());
        assertNotNull(found);
        assertEquals("Tirane", found.getQyteti());
    }

    @Test
    void testFindAll() throws Exception {
        insertTestLokacion("Tirane", "Adresa 1", "1001", null, null);
        insertTestLokacion("Durres", "Adresa 2", "2001", null, null);

        List<Lokacion> all = lokacionDAO.findAll();
        assertEquals(2, all.size());
    }

    @Test
    void testFindByCity() throws Exception {
        insertTestLokacion("Tirane", "Blloku", "1001", null, null);
        insertTestLokacion("Durres", "Plazh", "2001", null, null);

        List<Lokacion> tirane = lokacionDAO.findByCity("Tirane");
        assertEquals(1, tirane.size());
        assertEquals("Tirane", tirane.get(0).getQyteti());
    }

    @Test
    void testFindByZipCode() throws Exception {
        insertTestLokacion("Tirane", "Adresa", "1001", null, null);
        insertTestLokacion("Tirane", "Adresa 2", "1001", null, null);

        List<Lokacion> result = lokacionDAO.findByZipCode("1001");
        assertEquals(2, result.size());
    }

    @Test
    void testUpdate() throws Exception {
        Lokacion l = insertTestLokacion("Tirane", "Old Address", "1001", null, null);

        l.setAdresa("New Address");
        l.setLatitude(41.0);
        l.setLongitude(20.0);

        lokacionDAO.update(l);

        Lokacion updated = lokacionDAO.findById(l.getLokacionId());
        assertEquals("New Address", updated.getAdresa());
        assertEquals(41.0, updated.getLatitude());
    }

    @Test
    void testDelete() throws Exception {
        Lokacion l = insertTestLokacion("Tirane", "To Delete", "1001", null, null);

        lokacionDAO.delete(l.getLokacionId());

        Lokacion deleted = lokacionDAO.findById(l.getLokacionId());
        assertNull(deleted);
    }

    @Test
    void testCountLocations() throws Exception {
        insertTestLokacion("Tirane", "A1", "1001", null, null);
        insertTestLokacion("Durres", "A2", "2001", null, null);

        int count = lokacionDAO.countLocations();
        assertEquals(2, count);
    }

    @Test
    void testCountLocationsByCity() throws Exception {
        insertTestLokacion("Tirane", "A1", "1001", null, null);
        insertTestLokacion("Tirane", "A2", "1001", null, null);
        insertTestLokacion("Durres", "A3", "2001", null, null);

        assertEquals(2, lokacionDAO.countLocationsByCity("Tirane"));
        assertEquals(1, lokacionDAO.countLocationsByCity("Durres"));
    }

    @Test
    void testFindLocationsWithoutCoordinates() throws Exception {
        insertTestLokacion("Tirane", "No GPS", "1001", null, null);
        insertTestLokacion("Durres", "With GPS", "2001", 41.0, 19.0);

        List<Lokacion> result = lokacionDAO.findLocationsWithoutCoordinates();
        assertEquals(1, result.size());
    }

    @Test
    void testUpdateCoordinates() throws Exception {
        Lokacion l = insertTestLokacion("Tirane", "GPS Update", "1001", null, null);

        lokacionDAO.updateCoordinates(l.getLokacionId(), 41.5, 19.9);

        Lokacion updated = lokacionDAO.findById(l.getLokacionId());
        assertEquals(41.5, updated.getLatitude());
        assertEquals(19.9, updated.getLongitude());
    }

    // helper methods

    private Lokacion insertTestLokacion(
            String city,
            String address,
            String zip,
            Double lat,
            Double lng
    ) throws Exception {

        Lokacion l = new Lokacion();
        l.setQyteti(city);
        l.setAdresa(address);
        l.setRruga("Rruga Test");
        l.setNumri("10");
        l.setZipCode(zip);
        l.setLatitude(lat);
        l.setLongitude(lng);

        lokacionDAO.save(l);
        return l;
    }
}

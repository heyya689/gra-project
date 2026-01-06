package com.gra.tester;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import com.gra.db.DBConnection;

import java.io.InputStreamReader;
import java.sql.*;
import com.gra.dao.BiznesDAO;

import com.gra.model.Biznes;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BiznesDAOTest {
    private BiznesDAO biznesDAO;

    @BeforeAll
    static void initDatabase() throws Exception {
        System.setProperty("env", "test");

        Connection conn = DBConnection.getInstance().getConnection();

        RunScript.execute(
                conn,
                new InputStreamReader(
                        BiznesDAOTest.class
                                .getClassLoader()
                                .getResourceAsStream("schema.sql")
                )
        );
    }

    @BeforeAll
    public void setup() throws Exception {
        System.setProperty("env", "test");
        biznesDAO = new BiznesDAO();
    }


    @AfterEach
    void cleanDatabase() throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        conn.createStatement().execute("DELETE FROM biznes_lokacion");
        conn.createStatement().execute("DELETE FROM lokacion");
        conn.createStatement().execute("DELETE FROM biznes");
    }

    @AfterAll
    static void tearDown() {
        System.clearProperty("env");
    }

    @Test
    public void testFindById() throws Exception {
        int id= insertBiznes("Test");
        Biznes b = biznesDAO.findById(id);
        assertEquals(id, b.getBiznesId());
    }

    @Test
    public void testFindByNipt() throws Exception {
        int id= insertBiznesWithNipt("Test", "TEST-NIPT" );
        Biznes b = biznesDAO.findById(id);

        assertEquals("TEST-NIPT", b.getNipt());
    }

    @Test
    void testFindAll() throws Exception {
        insertBiznes("Biznes 1", LocalDateTime.now());
        insertBiznes("Biznes 2", LocalDateTime.now());

        List<Biznes> result = biznesDAO.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void testFindByCategory() throws Exception {
        int alphaCategoryId = insertKategori("Alpha");
        int betaCategoryId = insertKategori("Beta");

        int biznes1 = insertBiznes("Test Alpha", "Alpha");
        int biznes2 = insertBiznes("Test Gamma", (String)null);  // linked me join
        int biznes3 = insertBiznes("Test Epsilon", "Epsilon");

        linkBiznesKategori(biznes2, betaCategoryId);

        List<Biznes> result = biznesDAO.findByCategory("Alpha");

        assertEquals(1, result.size());
        assertEquals("Test Alpha", result.get(0).getEmri());
    }

    @Test
    void testFindByCity() throws Exception {
        int biznes1 = insertBiznes("Test Alpha");
        int biznes2 = insertBiznes("Test Beta");
        int biznes3 = insertBiznes("Test Gamma");

        int locTirane = insertLokacion("Tirane");
        int locDurres = insertLokacion("Durres");

        linkBiznesLokacion(biznes1, locTirane);
        linkBiznesLokacion(biznes2, locTirane);
        linkBiznesLokacion(biznes3, locDurres);

        List<Biznes> result = biznesDAO.findByCity("Tirane");

        assertEquals(2, result.size());
        assertEquals("Test Alpha", result.get(0).getEmri());
        assertEquals("Test Beta", result.get(1).getEmri());
    }

    @Test
    void testSearchByName() throws Exception {
        insertBiznes("Alpha Store");
        insertBiznes("Beta Market");
        insertBiznes("Super Alpha Shop");

        List<Biznes> result = biznesDAO.searchByName("Alpha");

        assertEquals(2, result.size());
        assertEquals("Alpha Store", result.get(0).getEmri());
        assertEquals("Super Alpha Shop", result.get(1).getEmri());
    }

    @Test
    public void testSaveAndDelete() throws Exception {
        Biznes newBiznes = new Biznes();
        newBiznes.setEmri("Test Biznes");
        newBiznes.setNipt("TEST-NIPT");

        // save
        biznesDAO.save(newBiznes);

        // assert - saved
        Biznes found = biznesDAO.findByNipt("TEST-NIPT");
        assertNotNull(found);
        assertEquals("Test Biznes", found.getEmri());

        // delete
        biznesDAO.delete(found.getBiznesId());

        // assert - deleted
        Biznes deleted = biznesDAO.findByNipt("TEST-NIPT");
        assertNull(deleted);
    }

    @Test
    void testUpdateBiznes() throws Exception {

        Biznes b = new Biznes();
        b.setEmri("Old Name");
        b.setPershkrim("Old Desc");
        b.setKategori("Old Cat");
        b.setNipt("UPD-001");
        b.setLicense("Old License");
        b.setTelefon("111111");
        b.setEmail("old@test.com");
        b.setWebsite("old.com");

        biznesDAO.save(b);

        // fetch biznes
        Biznes saved = biznesDAO.findByNipt("UPD-001");
        assertNotNull(saved);

        // modifikojme fushat
        saved.setEmri("New Name");
        saved.setPershkrim("New Description");
        saved.setKategori("New Category");
        saved.setLicense("New License");
        saved.setTelefon("999999");
        saved.setEmail("new@test.com");
        saved.setWebsite("new.com");

        biznesDAO.update(saved);

        Biznes updated = biznesDAO.findById(saved.getBiznesId());

        assertNotNull(updated);
        assertEquals("New Name", updated.getEmri());
        assertEquals("New Description", updated.getPershkrim());
        assertEquals("New Category", updated.getKategori());
        assertEquals("New License", updated.getLicense());
        assertEquals("999999", updated.getTelefon());
        assertEquals("new@test.com", updated.getEmail());
        assertEquals("new.com", updated.getWebsite());
    }

    @Test
    void testNiptExists() throws Exception {

        Biznes b = new Biznes();
        b.setEmri("Biz 1");
        b.setNipt("EXIST-123");

        biznesDAO.save(b);

        assertTrue(biznesDAO.niptExists("EXIST-123"));
        assertFalse(biznesDAO.niptExists("NOT-EXIST"));
    }

    @Test
    void testCountBusinesses() throws Exception {

        int initialCount = biznesDAO.countBusinesses();

        Biznes b1 = new Biznes();
        b1.setEmri("Biz 1");
        b1.setNipt("CNT-1");
        biznesDAO.save(b1);

        Biznes b2 = new Biznes();
        b2.setEmri("Biz 2");
        b2.setNipt("CNT-2");
        biznesDAO.save(b2);

        int finalCount = biznesDAO.countBusinesses();

        assertEquals(initialCount + 2, finalCount);
    }











    //helper methods
    private int insertBiznes(String emri) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO biznes (emri, nipt) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS
        );
        ps.setString(1, emri);
        ps.setString(2, "TEST-NIPT-" + System.nanoTime());

        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        return rs.getInt(1);
    }

    private void insertBiznes(String name, LocalDateTime createdAt) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO biznes (emri, nipt, created_at) VALUES (?,?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, name);
        ps.setString(2, "TEST-NIPT-" + System.nanoTime());
        ps.setTimestamp(3, Timestamp.valueOf(createdAt));
        ps.executeUpdate();
    }

    private int insertBiznes(String emri, String kategori) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO biznes (emri, nipt, kategori) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        );
        ps.setString(1, emri);
        ps.setString(2, "TEST-NIPT-" + System.nanoTime());
        ps.setString(3, kategori);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        return rs.getInt(1);
    }

    private int insertBiznesWithNipt(String emri, String nipt) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO biznes (emri, nipt) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS
        );
        ps.setString(1, emri);
        ps.setString(2, nipt);
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        return rs.getInt(1);
    }


    private int insertKategori(String emri) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO kategori (emri) VALUES (?)",
                Statement.RETURN_GENERATED_KEYS
        );
        ps.setString(1, emri);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        return rs.getInt(1);
    }


    private void linkBiznesKategori(int biznesId, int kategoriId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO biznes_kategori (biznes_id, kategori_id) VALUES (?, ?)"
        );
        ps.setInt(1, biznesId);
        ps.setInt(2, kategoriId);
        ps.executeUpdate();
    }

    private int insertLokacion(String qyteti) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO lokacion (qyteti) VALUES (?)",
                Statement.RETURN_GENERATED_KEYS
        );
        ps.setString(1, qyteti);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        return rs.getInt(1);
    }

    private void linkBiznesLokacion(int biznesId, int lokacionId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO biznes_lokacion (biznes_id, lokacion_id) VALUES (?, ?)"
        );
        ps.setInt(1, biznesId);
        ps.setInt(2, lokacionId);
        ps.executeUpdate();
    }





}

package com.gra.tester;

import org.junit.jupiter.api.*;

import com.gra.db.DBConnection;

import java.sql.*;
import com.gra.dao.BiznesDAO;

import com.gra.model.Biznes;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BiznesDAOTest extends BaseDAOTest {
    private BiznesDAO biznesDAO;

    protected void cleanDatabase() throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();

        stmt.execute("SET REFERENTIAL_INTEGRITY FALSE");

        stmt.execute("DELETE FROM biznes_imazhe");
        stmt.execute("DELETE FROM biznes");

        stmt.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @BeforeEach
    public void setup() throws Exception {
        super.initDatabase();
        biznesDAO = new BiznesDAO();
        cleanDatabase();
    }

    @Test
    void coverageProbe() throws Exception {
        BiznesDAO dao = new BiznesDAO();
        dao.findAll();
    }

    //findById(int)
    @Test
    public void testFindById() throws Exception {
        int id = insertBiznes("Test");
        Biznes b = biznesDAO.findById(id);
        assertNotNull(b, "Biznes should not be null");
        assertEquals(id, b.getBiznesId());
    }

    //findByNipt(string)
    @Test
    public void testFindByNipt() throws Exception {
        int id = insertBiznesWithNipt("Test", "TEST-NIPT");
        Biznes b = biznesDAO.findByNipt("TEST-NIPT");

        assertNotNull(b, "Biznes should not be null");
        assertEquals("TEST-NIPT", b.getNipt());
    }

    //findAll()
    @Test
    void testFindAll() throws Exception {
        insertBiznes("Biznes 1", LocalDateTime.now());
        insertBiznes("Biznes 2", LocalDateTime.now());

        List<Biznes> result = biznesDAO.findAll();

        assertTrue(result.size() >= 2, "Should have at least 2 businesses");
    }

    //findByCategory(string)
    @Test
    void testFindByCategory() throws Exception {
        int alphaCategoryId = insertKategori("Alpha");
        int betaCategoryId = insertKategori("Beta");

        int biznes1 = insertBiznes("Test Alpha", "Alpha");
        int biznes2 = insertBiznes("Test Gamma", (String)null);  // linked via join
        int biznes3 = insertBiznes("Test Epsilon", "Epsilon");

        linkBiznesKategori(biznes2, betaCategoryId);

        List<Biznes> result = biznesDAO.findByCategory("Alpha");

        assertEquals(1, result.size());
        assertEquals("Test Alpha", result.get(0).getEmri());
    }

    //findByCity(string)
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
        assertTrue(result.stream().anyMatch(b -> "Test Alpha".equals(b.getEmri())));
        assertTrue(result.stream().anyMatch(b -> "Test Beta".equals(b.getEmri())));
    }

    //searchByName(string)
    @Test
    void testSearchByName() throws Exception {
        insertBiznes("Alpha Store");
        insertBiznes("Beta Market");
        insertBiznes("Super Alpha Shop");

        List<Biznes> result = biznesDAO.searchByName("Alpha");

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(b -> "Alpha Store".equals(b.getEmri())));
        assertTrue(result.stream().anyMatch(b -> "Super Alpha Shop".equals(b.getEmri())));
    }

    //save(Biznes) & delete(Biznes)
    @Test
    public void testSaveAndDelete() throws Exception {
        Biznes newBiznes = new Biznes();
        newBiznes.setEmri("Test Biznes");
        newBiznes.setNipt("TEST-NIPT-SAVE");

        // save
        biznesDAO.save(newBiznes);
        assertTrue(newBiznes.getBiznesId() > 0, "ID should be set after save");

        // assert - saved
        Biznes found = biznesDAO.findByNipt("TEST-NIPT-SAVE");
        assertNotNull(found);
        assertEquals("Test Biznes", found.getEmri());

        // delete
        biznesDAO.delete(found.getBiznesId());

        // assert - deleted
        Biznes deleted = biznesDAO.findByNipt("TEST-NIPT-SAVE");
        assertNull(deleted);
    }

    //updateBiznes(Biznes)
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

        // modify fields
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

    //niptExists(string)
    @Test
    void testNiptExists() throws Exception {
        Biznes b = new Biznes();
        b.setEmri("Biz 1");
        b.setNipt("EXIST-123");

        biznesDAO.save(b);

        assertTrue(biznesDAO.niptExists("EXIST-123"));
        assertFalse(biznesDAO.niptExists("NOT-EXIST"));
    }

    //countBusinesses()
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
        int id = rs.getInt(1);
        rs.close();
        ps.close();
        return id;
    }

    private void insertBiznes(String emri, LocalDateTime createdAt) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO biznes (emri, nipt, created_at) VALUES (?,?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, emri);
        ps.setString(2, "TEST-NIPT-" + System.nanoTime());
        ps.setTimestamp(3, Timestamp.valueOf(createdAt));
        ps.executeUpdate();
        ps.close();
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
        int id = rs.getInt(1);
        rs.close();
        ps.close();
        return id;
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
        int id = rs.getInt(1);
        rs.close();
        ps.close();
        return id;
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
        int id = rs.getInt(1);
        rs.close();
        ps.close();
        return id;
    }

    private void linkBiznesKategori(int biznesId, int kategoriId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO biznes_kategori (biznes_id, kategori_id) VALUES (?, ?)"
        );
        ps.setInt(1, biznesId);
        ps.setInt(2, kategoriId);
        ps.executeUpdate();
        ps.close();
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
        int id = rs.getInt(1);
        rs.close();
        ps.close();
        return id;
    }

    private void linkBiznesLokacion(int biznesId, int lokacionId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO biznes_lokacion (biznes_id, lokacion_id) VALUES (?, ?)"
        );
        ps.setInt(1, biznesId);
        ps.setInt(2, lokacionId);
        ps.executeUpdate();
        ps.close();
    }
}
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



    //niptExists(string)


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
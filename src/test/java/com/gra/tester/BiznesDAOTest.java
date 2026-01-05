package com.gra.tester;

import com.gra.db.DBConnection;
import java.sql.*;
import com.gra.dao.BiznesDAO;

import com.gra.model.Biznes;
import org.junit.jupiter.api.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BiznesDAOTest {
    private BiznesDAO biznesDAO;

    @BeforeAll
    public void setup() throws Exception {
        biznesDAO = new BiznesDAO();
    }

    @Test
    public void testFindById() throws Exception {
        Biznes biznes = biznesDAO.findById(1);
        assertNotNull(biznes);
        assertEquals(1, biznes.getBiznesId());
    }

    @Test
    public void testFindByNipt() throws Exception {
        Biznes biznes = biznesDAO.findByNipt("K123456789");
        assertNotNull(biznes);
        assertEquals("K123456789", biznes.getNipt());
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

    public void testSaveAndDelete() throws Exception{
        Biznes newBiznes=new Biznes(100, "Test", "testNipt12");
        biznesDAO.save(newBiznes);
        Biznes found=biznesDAO.findByNipt("testNipt12");
        assertNotNull(found);
        biznesDAO.delete(found.getBiznesId());
        Biznes deleted = biznesDAO.findByNipt("testNipt12");
        assertNull(deleted);
    }








    //helper methods
    private int insertBiznes(String emri) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO biznes (emri) VALUES (?)",
                Statement.RETURN_GENERATED_KEYS
        );
        ps.setString(1, emri);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        return rs.getInt(1);
    }

    private void insertBiznes(String name, LocalDateTime createdAt) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO biznes (name, created_at) VALUES (?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, name);
        ps.setTimestamp(2, Timestamp.valueOf(createdAt));
        ps.executeUpdate();
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

    private int insertBiznes(String emri, String kategori) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO biznes (emri, kategori) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS
        );
        ps.setString(1, emri);
        ps.setString(2, kategori);
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

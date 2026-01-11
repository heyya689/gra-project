package com.gra.tester;

import com.gra.dao.BiznesDAO;
import com.gra.dao.KategoriDAO;
import com.gra.db.DBConnection;
import com.gra.model.Biznes;
import com.gra.model.Kategori;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.*;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class KategoriDAOTest extends BaseDAOTest {

    private KategoriDAO kategoriDAO;
    private BiznesDAO biznesDAO;

    @BeforeEach
    void setup() throws Exception {
        System.setProperty("env", "test");
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();

        kategoriDAO = new KategoriDAO();
        biznesDAO = new BiznesDAO();
    }

    @AfterEach
    void cleanUp() throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        stmt.execute("DELETE FROM biznes_kategori");
        stmt.execute("DELETE FROM biznes");
        stmt.execute("DELETE FROM kategori");
    }

    @AfterAll
    static void tearDown() {
        System.clearProperty("env");
    }

    //findById(int)
    @Test
    void testFindById() throws Exception {
        Kategori kategori = createKategori("Delta");

        Kategori found = kategoriDAO.findById(kategori.getKategoriId());

        assertNotNull(found);
        assertEquals("Delta", found.getEmri());
    }

    //findByEmri(string)
    @Test
    void testFindByEmri() throws Exception {
        createKategori("Lambda");

        Kategori kategori = kategoriDAO.findByEmri("Lambda");

        assertNotNull(kategori);
        assertEquals("Lambda", kategori.getEmri());
    }

    //findAll()
    @Test
    void testFindAll() throws Exception {
        createKategori("A");
        createKategori("B");

        List<Kategori> list = kategoriDAO.findAll();

        assertEquals(2, list.size());
    }



    //searchByEmri(string)
    @Test
    void testSearchByEmri() throws Exception {
        createKategori("Alpha");
        createKategori("Beta");
        createKategori("Zeta");

        List<Kategori> result = kategoriDAO.searchByEmri("et");

        assertEquals(2, result.size());
        assertTrue(result.stream()
                .allMatch(k -> k.getEmri().toLowerCase().contains("et")));
    }

    //delete(int)
    @Test
    void testDeleteCategory() throws Exception {
        Kategori kategori = createKategori("ToDelete");

        kategoriDAO.delete(kategori.getKategoriId());

        assertNull(kategoriDAO.findById(kategori.getKategoriId()));
    }


    //countCategories()
    @Test
    void testCountCategories() throws Exception {
        assertEquals(0, kategoriDAO.countCategories());

        createKategori("A");
        createKategori("B");
        createKategori("C");

        assertEquals(3, kategoriDAO.countCategories());
    }



    //helper methods

    private Kategori createKategori(String emri) throws Exception {
        Kategori k = new Kategori();
        k.setEmri(emri);
        k.setIkona("icon.png");
        k.setPershkrim("Test Kategori");
        kategoriDAO.save(k);
        return k;
    }
}

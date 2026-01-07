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

    //addBusinessToCategory(int, int)
    @Test
    void testAddBusinessToCategory() throws Exception {
        Kategori kategori = createKategori("Epsilon");
        Biznes biznes = createBiznes("Biznes 1");

        kategoriDAO.addBusinessToCategory(
                kategori.getKategoriId(),
                biznes.getBiznesId()
        );

        Kategori loaded = kategoriDAO.findById(kategori.getKategoriId());

        assertEquals(1, loaded.getBizneset().size());
        assertEquals("Biznes 1", loaded.getBizneset().get(0).getEmri());
    }

    //findCategoriesWithBusinesses()
    @Test
    void testFindCategoriesWithBusinesses() throws Exception {
        Kategori kategori = createKategori("Omega");
        Biznes biznes = createBiznes("Biznes Omega");

        kategoriDAO.addBusinessToCategory(
                kategori.getKategoriId(),
                biznes.getBiznesId()
        );

        List<Kategori> result = kategoriDAO.findCategoriesWithBusinesses();

        assertEquals(1, result.size());
        assertEquals("Omega", result.get(0).getEmri());
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

    //removeBusinessFromCategory(int, int)
    @Test
    void testRemoveBusinessFromCategory() throws Exception {
        Kategori kategori = createKategori("Theta");
        Biznes biznes = createBiznes("Biznes Theta");

        kategoriDAO.addBusinessToCategory(
                kategori.getKategoriId(),
                biznes.getBiznesId()
        );

        //check
        assertEquals(1, kategoriDAO.countBusinessesInCategory(kategori.getKategoriId()));

        kategoriDAO.removeBusinessFromCategory(
                kategori.getKategoriId(),
                biznes.getBiznesId()
        );

        assertEquals(0, kategoriDAO.countBusinessesInCategory(kategori.getKategoriId()));
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

    @Test
    void testCountBusinessesInCategory() throws Exception {
        Kategori kategori = createKategori("Sigma");

        Biznes b1 = createBiznes("Biznes 1");
        Biznes b2 = createBiznes("Biznes 2");

        kategoriDAO.addBusinessToCategory(
                kategori.getKategoriId(),
                b1.getBiznesId()
        );
        kategoriDAO.addBusinessToCategory(
                kategori.getKategoriId(),
                b2.getBiznesId()
        );

        assertEquals(
                2,
                kategoriDAO.countBusinessesInCategory(kategori.getKategoriId())
        );
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

    private Biznes createBiznes(String emri) throws Exception {
        Biznes b = new Biznes();
        b.setEmri(emri);
        b.setPershkrim("Test Biznes");
        b.setKategori("Test");
        b.setNipt("NIPT-" + System.nanoTime());
        b.setLicense("LIC-1");
        b.setTelefon("123");
        b.setEmail("test@test.com");
        b.setWebsite("test.com");
        biznesDAO.save(b);
        return b;
    }
}

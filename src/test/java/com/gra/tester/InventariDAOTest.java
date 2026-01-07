package com.gra.tester;

import com.gra.dao.BiznesDAO;
import com.gra.dao.InventariDAO;
import com.gra.db.DBConnection;
import com.gra.model.Biznes;
import com.gra.model.Inventari;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.*;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InventariDAOTest extends BaseDAOTest{

    private InventariDAO inventariDAO;
    private BiznesDAO biznesDAO;
    private Biznes testBiznes;
    private Inventari testInventari;


    @BeforeEach
    void setup() throws Exception {
        System.setProperty("env", "test");

        inventariDAO = new InventariDAO();
        biznesDAO = new BiznesDAO();

        cleanDatabase(); // from BaseDAOTest

        // create biznes
        testBiznes = new Biznes();
        testBiznes.setEmri("Test Biznes");
        testBiznes.setPershkrim("Test Description");
        testBiznes.setKategori("Test Category");
        testBiznes.setNipt("TEST-NIPT");
        testBiznes.setLicense("LIC-001");
        testBiznes.setTelefon("999999");
        testBiznes.setEmail("test@email.com");
        testBiznes.setWebsite("www.test.com");

        biznesDAO.save(testBiznes);

        // create inventari
        testInventari = new Inventari();
        testInventari.setBiznes(testBiznes);
        testInventari.setEmerProdukt("Test Product");
        testInventari.setPershkrim("Test Description");
        testInventari.setSasi(10);
        testInventari.setCmimi(1200.0);
        testInventari.setNjesia("tests");
        testInventari.setKategoria("Test Category");
        testInventari.setActive(true);

        inventariDAO.save(testInventari);
    }


    protected void cleanDatabase() throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();

        stmt.execute("DELETE FROM inventari");
        stmt.execute("DELETE FROM biznes");
    }

    @AfterAll
    static void tearDown() {
        System.clearProperty("env");
    }

    //saveInventari(Inventari)
    @Test
    void testSaveInventari() throws Exception {
        testInventari = new Inventari();
        testInventari.setBiznes(testBiznes);
        testInventari.setEmerProdukt("Test Product");
        testInventari.setPershkrim("Test Description");
        testInventari.setSasi(10);
        testInventari.setCmimi(1200.0);
        testInventari.setNjesia("tests");
        testInventari.setKategoria("Test Category");
        testInventari.setActive(true);

        inventariDAO.save(testInventari);

        assertTrue(testInventari.getInventarId() > 0);
    }

    //findById(int)
    @Test
    void testFindById() throws Exception {
        Inventari found = inventariDAO.findById(testInventari.getInventarId());

        assertNotNull(found);
        assertEquals("Test Product", found.getEmerProdukt());
        assertEquals(testBiznes.getBiznesId(), found.getBiznes().getBiznesId());
    }

    //findAll()
    @Test
    void testFindAll() throws Exception {
        List<Inventari> list = inventariDAO.findAll();
        assertFalse(list.isEmpty());
    }

    //findByBusinessId(int)
    @Test
    void testFindByBusinessId() throws Exception {
        List<Inventari> list = inventariDAO.findByBusinessId(testBiznes.getBiznesId());
        assertEquals(1, list.size());
    }

    //updateInventari(Inventari)
    @Test
    void testUpdateInventari() throws Exception {
        testInventari.setSasi(20);
        testInventari.setCmimi(1100.0);

        inventariDAO.update(testInventari);

        Inventari updated = inventariDAO.findById(testInventari.getInventarId());
        assertEquals(20, updated.getSasi());
        assertEquals(1100.0, updated.getCmimi());
        assertNotNull(updated.getUpdatedAt());
    }

    //updateStock(int, int)
    @Test
    void testUpdateStock() throws Exception {
        inventariDAO.updateStock(testInventari.getInventarId(), 5);

        Inventari updated = inventariDAO.findById(testInventari.getInventarId());
        assertEquals(5, updated.getSasi());
    }

    //updatePrice(int, double)
    @Test
    void testUpdatePrice() throws Exception {
        inventariDAO.updatePrice(testInventari.getInventarId(), 999.99);

        Inventari updated = inventariDAO.findById(testInventari.getInventarId());
        assertEquals(999.99, updated.getCmimi());
    }

    //deactivateItem(int) & activateItem(int)
    @Test
    void testDeactivateActivateItem() throws Exception {
        inventariDAO.deactivateItem(testInventari.getInventarId());
        assertFalse(inventariDAO.findById(testInventari.getInventarId()).isActive());

        inventariDAO.activateItem(testInventari.getInventarId());
        assertTrue(inventariDAO.findById(testInventari.getInventarId()).isActive());
    }

    //findAvailableItems()
    @Test
    void testFindAvailableItems() throws Exception {
        List<Inventari> list = inventariDAO.findAvailableItems();
        assertFalse(list.isEmpty());
    }

    //findByCategory(string)
    @Test
    void testFindByCategory() throws Exception {
        List<Inventari> list = inventariDAO.findByCategory("Test Category");
        assertEquals(1, list.size());
    }

    //findLowStockItems()
    @Test
    void testFindLowStockItems() throws Exception {
        List<Inventari> list = inventariDAO.findLowStockItems(10);
        assertFalse(list.isEmpty());
    }

    //countInventoryItems() & getTotalInventoryValue(int)
    @Test
    void testInventoryStats() throws Exception {
        int count = inventariDAO.countInventoryItems();
        assertTrue(count > 0);

        double total = inventariDAO.getTotalInventoryValue(testBiznes.getBiznesId());
        assertTrue(total > 0);
    }

    //deleteInventari(Inventari)
    @Test
    void testDeleteInventari() throws Exception {
        inventariDAO.delete(testInventari.getInventarId());
        assertNull(inventariDAO.findById(testInventari.getInventarId()));
    }

}

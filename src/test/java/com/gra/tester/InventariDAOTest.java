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





}

package com.gra.dao;

import com.gra.db.DBConnection;
import com.gra.model.Inventari;
import com.gra.model.Biznes;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InventariDAO {

    private BiznesDAO biznesDAO;

    public InventariDAO() {
        this.biznesDAO = new BiznesDAO();
    }

    public Inventari findById(int id) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM inventari WHERE inventar_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return mapResultSetToInventari(rs);
        }
        return null;
    }

    public List<Inventari> findAll() throws Exception {
        List<Inventari> inventariList = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM inventari ORDER BY emer_produkt");

        while (rs.next()) {
            inventariList.add(mapResultSetToInventari(rs));
        }
        return inventariList;
    }

    public List<Inventari> findByBusinessId(int businessId) throws Exception {
        List<Inventari> inventariList = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM inventari WHERE biznes_id = ? ORDER BY emer_produkt";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, businessId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            inventariList.add(mapResultSetToInventari(rs));
        }
        return inventariList;
    }

    public List<Inventari> findByCategory(String category) throws Exception {
        List<Inventari> inventariList = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM inventari WHERE kategoria = ? AND is_active = TRUE ORDER BY emer_produkt";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, category);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            inventariList.add(mapResultSetToInventari(rs));
        }
        return inventariList;
    }

    public List<Inventari> findAvailableItems() throws Exception {
        List<Inventari> inventariList = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM inventari WHERE sasi > 0 AND is_active = TRUE ORDER BY emer_produkt";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            inventariList.add(mapResultSetToInventari(rs));
        }
        return inventariList;
    }

    public List<Inventari> searchByName(String name) throws Exception {
        List<Inventari> inventariList = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM inventari WHERE emer_produkt LIKE ? ORDER BY emer_produkt";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + name + "%");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            inventariList.add(mapResultSetToInventari(rs));
        }
        return inventariList;
    }

    public void save(Inventari inventari) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO inventari (biznes_id, emer_produkt, pershkrim, sasi, " +
                "cmimi, njesia, kategoria, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setInt(1, inventari.getBiznes().getBiznesId());
        ps.setString(2, inventari.getEmerProdukt());
        ps.setString(3, inventari.getPershkrim());
        ps.setInt(4, inventari.getSasi());
        ps.setDouble(5, inventari.getCmimi());
        ps.setString(6, inventari.getNjesia());
        ps.setString(7, inventari.getKategoria());
        ps.setBoolean(8, inventari.isActive());

        ps.executeUpdate();

        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            inventari.setInventarId(generatedKeys.getInt(1));
        }
    }

    public void update(Inventari inventari) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE inventari SET biznes_id=?, emer_produkt=?, pershkrim=?, sasi=?, " +
                "cmimi=?, njesia=?, kategoria=?, is_active=?, updated_at=? WHERE inventar_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, inventari.getBiznes().getBiznesId());
        ps.setString(2, inventari.getEmerProdukt());
        ps.setString(3, inventari.getPershkrim());
        ps.setInt(4, inventari.getSasi());
        ps.setDouble(5, inventari.getCmimi());
        ps.setString(6, inventari.getNjesia());
        ps.setString(7, inventari.getKategoria());
        ps.setBoolean(8, inventari.isActive());
        ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
        ps.setInt(10, inventari.getInventarId());

        ps.executeUpdate();
    }

    public void delete(int inventarId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM inventari WHERE inventar_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, inventarId);
        ps.executeUpdate();
    }

    public void updateStock(int inventarId, int newQuantity) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE inventari SET sasi=?, updated_at=? WHERE inventar_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, newQuantity);
        ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
        ps.setInt(3, inventarId);

        ps.executeUpdate();
    }

    public void updatePrice(int inventarId, double newPrice) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE inventari SET cmimi=?, updated_at=? WHERE inventar_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setDouble(1, newPrice);
        ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
        ps.setInt(3, inventarId);

        ps.executeUpdate();
    }

    public void activateItem(int inventarId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE inventari SET is_active=TRUE, updated_at=? WHERE inventar_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
        ps.setInt(2, inventarId);

        ps.executeUpdate();
    }

    public void deactivateItem(int inventarId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE inventari SET is_active=FALSE, updated_at=? WHERE inventar_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
        ps.setInt(2, inventarId);

        ps.executeUpdate();
    }

    public int countInventoryItems() throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM inventari";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public double getTotalInventoryValue(int businessId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT SUM(sasi * cmimi) as total_value FROM inventari WHERE biznes_id = ? AND is_active = TRUE";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, businessId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getDouble("total_value");
        }
        return 0.0;
    }

    public List<Inventari> findLowStockItems(int threshold) throws Exception {
        List<Inventari> inventariList = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM inventari WHERE sasi <= ? AND is_active = TRUE ORDER BY sasi";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, threshold);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            inventariList.add(mapResultSetToInventari(rs));
        }
        return inventariList;
    }

    private Inventari mapResultSetToInventari(ResultSet rs) throws Exception {
        Inventari inventari = new Inventari();
        inventari.setInventarId(rs.getInt("inventar_id"));

        // Load business
        int biznesId = rs.getInt("biznes_id");
        Biznes biznes = biznesDAO.findById(biznesId);
        inventari.setBiznes(biznes);

        inventari.setEmerProdukt(rs.getString("emer_produkt"));
        inventari.setPershkrim(rs.getString("pershkrim"));
        inventari.setSasi(rs.getInt("sasi"));
        inventari.setCmimi(rs.getDouble("cmimi"));
        inventari.setNjesia(rs.getString("njesia"));
        inventari.setKategoria(rs.getString("kategoria"));
        inventari.setActive(rs.getBoolean("is_active"));
        inventari.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        inventari.setUpdatedAt(rs.getTimestamp("updated_at") != null ?
                rs.getTimestamp("updated_at").toLocalDateTime() : null);

        return inventari;
    }
}
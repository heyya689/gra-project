package com.gra.dao;

import com.gra.db.DBConnection;
import com.gra.model.Biznes;
import com.gra.model.Lokacion;
import com.gra.model.Kategori;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BiznesDAO {

    public Biznes findById(int id) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM biznes WHERE biznes_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Biznes biznes = mapResultSetToBiznes(rs);

            // Load location
            biznes.setLokacion(findLokacionByBiznesId(biznes.getBiznesId()));

            // Load categories
            biznes.setKategorite(findKategoriteByBiznesId(biznes.getBiznesId()));

            return biznes;
        }
        return null;
    }

    public Biznes findByNipt(String nipt) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM biznes WHERE nipt = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nipt);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return mapResultSetToBiznes(rs);
        }
        return null;
    }

    public List<Biznes> findAll() throws Exception {
        List<Biznes> businesses = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM biznes ORDER BY created_at DESC");

        while (rs.next()) {
            businesses.add(mapResultSetToBiznes(rs));
        }
        return businesses;
    }

    public List<Biznes> findByCategory(String category) throws Exception {
        List<Biznes> businesses = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT b.* FROM biznes b " +
                "WHERE b.kategori = ? OR EXISTS (" +
                "  SELECT 1 FROM biznes_kategori bk " +
                "  INNER JOIN kategori k ON bk.kategori_id = k.kategori_id " +
                "  WHERE bk.biznes_id = b.biznes_id AND k.emri = ?" +
                ") ORDER BY b.emri";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, category);
        ps.setString(2, category);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            businesses.add(mapResultSetToBiznes(rs));
        }
        return businesses;
    }

    public List<Biznes> findByCity(String city) throws Exception {
        List<Biznes> businesses = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT b.* FROM biznes b " +
                "INNER JOIN biznes_lokacion bl ON b.biznes_id = bl.biznes_id " +
                "INNER JOIN lokacion l ON bl.lokacion_id = l.lokacion_id " +
                "WHERE l.qyteti = ? ORDER BY b.emri";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, city);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            businesses.add(mapResultSetToBiznes(rs));
        }
        return businesses;
    }

    public List<Biznes> searchByName(String name) throws Exception {
        List<Biznes> businesses = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM biznes WHERE emri LIKE ? ORDER BY emri";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + name + "%");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            businesses.add(mapResultSetToBiznes(rs));
        }
        return businesses;
    }

    public void save(Biznes biznes) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO biznes (emri, pershkrim, kategori, nipt, license, " +
                "telefon, email, website) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setString(1, biznes.getEmri());
        ps.setString(2, biznes.getPershkrim());
        ps.setString(3, biznes.getKategori());
        ps.setString(4, biznes.getNipt());
        ps.setString(5, biznes.getLicense());
        ps.setString(6, biznes.getTelefon());
        ps.setString(7, biznes.getEmail());
        ps.setString(8, biznes.getWebsite());

        ps.executeUpdate();

        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            int biznesId = generatedKeys.getInt(1);
            biznes.setBiznesId(biznesId);

            // Save location if exists
            if (biznes.getLokacion() != null) {
                saveLokacion(biznes.getLokacion(), biznesId);
            }

            // Save categories if exists
            if (biznes.getKategorite() != null && !biznes.getKategorite().isEmpty()) {
                for (Kategori kategori : biznes.getKategorite()) {
                    addKategoriToBiznes(biznesId, kategori.getKategoriId());
                }
            }
        }
    }

    public void update(Biznes biznes) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE biznes SET emri=?, pershkrim=?, kategori=?, nipt=?, license=?, " +
                "telefon=?, email=?, website=?, updated_at=? WHERE biznes_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, biznes.getEmri());
        ps.setString(2, biznes.getPershkrim());
        ps.setString(3, biznes.getKategori());
        ps.setString(4, biznes.getNipt());
        ps.setString(5, biznes.getLicense());
        ps.setString(6, biznes.getTelefon());
        ps.setString(7, biznes.getEmail());
        ps.setString(8, biznes.getWebsite());
        ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
        ps.setInt(10, biznes.getBiznesId());

        ps.executeUpdate();

        // Update location if exists
        if (biznes.getLokacion() != null) {
            updateLokacion(biznes.getLokacion());
        }
    }

    public void delete(int biznesId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();

        // Delete business categories first
        deleteBiznesKategorite(biznesId);

        // Delete business location
        deleteLokacionByBiznesId(biznesId);

        // Then delete business
        String sql = "DELETE FROM biznes WHERE biznes_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, biznesId);
        ps.executeUpdate();
    }

    public boolean niptExists(String nipt) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM biznes WHERE nipt = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nipt);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        return false;
    }

    public int countBusinesses() throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM biznes";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    // Private helper methods
    private Lokacion findLokacionByBiznesId(int biznesId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT l.* FROM lokacion l " +
                "INNER JOIN biznes_lokacion bl ON l.lokacion_id = bl.lokacion_id " +
                "WHERE bl.biznes_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, biznesId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Lokacion lokacion = new Lokacion();
            lokacion.setLokacionId(rs.getInt("lokacion_id"));
            lokacion.setQyteti(rs.getString("qyteti"));
            lokacion.setAdresa(rs.getString("adresa"));
            lokacion.setRruga(rs.getString("rruga"));
            lokacion.setNumri(rs.getString("numri"));
            lokacion.setZipCode(rs.getString("zip_code"));
            lokacion.setLatitude(rs.getDouble("latitude"));
            lokacion.setLongitude(rs.getDouble("longitude"));
            lokacion.setCreatedAt(rs.getTimestamp("created_at") != null ?
                    rs.getTimestamp("created_at").toLocalDateTime() : null);

            return lokacion;
        }
        return null;
    }

    private List<Kategori> findKategoriteByBiznesId(int biznesId) throws Exception {
        List<Kategori> kategorite = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT k.* FROM kategori k " +
                "INNER JOIN biznes_kategori bk ON k.kategori_id = bk.kategori_id " +
                "WHERE bk.biznes_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, biznesId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Kategori kategori = new Kategori();
            kategori.setKategoriId(rs.getInt("kategori_id"));
            kategori.setEmri(rs.getString("emri"));
            kategori.setIkona(rs.getString("ikona"));
            kategori.setPershkrim(rs.getString("pershkrim"));
            kategori.setCreatedAt(rs.getTimestamp("created_at") != null ?
                    rs.getTimestamp("created_at").toLocalDateTime() : null);

            kategorite.add(kategori);
        }
        return kategorite;
    }

    private void saveLokacion(Lokacion lokacion, int biznesId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();

        // First save location
        String sql = "INSERT INTO lokacion (qyteti, adresa, rruga, numri, zip_code, " +
                "latitude, longitude) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setString(1, lokacion.getQyteti());
        ps.setString(2, lokacion.getAdresa());
        ps.setString(3, lokacion.getRruga());
        ps.setString(4, lokacion.getNumri());
        ps.setString(5, lokacion.getZipCode());
        ps.setDouble(6, lokacion.getLatitude() != null ? lokacion.getLatitude() : 0.0);
        ps.setDouble(7, lokacion.getLongitude() != null ? lokacion.getLongitude() : 0.0);

        ps.executeUpdate();

        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            int lokacionId = generatedKeys.getInt(1);
            lokacion.setLokacionId(lokacionId);

            // Link location to business
            linkBiznesToLokacion(biznesId, lokacionId);
        }
    }

    private void updateLokacion(Lokacion lokacion) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE lokacion SET qyteti=?, adresa=?, rruga=?, numri=?, " +
                "zip_code=?, latitude=?, longitude=? WHERE lokacion_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, lokacion.getQyteti());
        ps.setString(2, lokacion.getAdresa());
        ps.setString(3, lokacion.getRruga());
        ps.setString(4, lokacion.getNumri());
        ps.setString(5, lokacion.getZipCode());
        ps.setDouble(6, lokacion.getLatitude() != null ? lokacion.getLatitude() : 0.0);
        ps.setDouble(7, lokacion.getLongitude() != null ? lokacion.getLongitude() : 0.0);
        ps.setInt(8, lokacion.getLokacionId());

        ps.executeUpdate();
    }

    private void linkBiznesToLokacion(int biznesId, int lokacionId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();

        // Remove existing location link
        String deleteSql = "DELETE FROM biznes_lokacion WHERE biznes_id=?";
        PreparedStatement deletePs = conn.prepareStatement(deleteSql);
        deletePs.setInt(1, biznesId);
        deletePs.executeUpdate();

        // Add new location link
        String insertSql = "INSERT INTO biznes_lokacion (biznes_id, lokacion_id) VALUES (?, ?)";
        PreparedStatement insertPs = conn.prepareStatement(insertSql);
        insertPs.setInt(1, biznesId);
        insertPs.setInt(2, lokacionId);
        insertPs.executeUpdate();
    }

    private void addKategoriToBiznes(int biznesId, int kategoriId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();

        // Check if already linked
        String checkSql = "SELECT COUNT(*) FROM biznes_kategori WHERE biznes_id=? AND kategori_id=?";
        PreparedStatement checkPs = conn.prepareStatement(checkSql);
        checkPs.setInt(1, biznesId);
        checkPs.setInt(2, kategoriId);
        ResultSet rs = checkPs.executeQuery();

        if (rs.next() && rs.getInt(1) == 0) {
            // Not linked, insert new
            String sql = "INSERT INTO biznes_kategori (biznes_id, kategori_id) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, biznesId);
            ps.setInt(2, kategoriId);
            ps.executeUpdate();
        }
    }

    private void deleteBiznesKategorite(int biznesId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM biznes_kategori WHERE biznes_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, biznesId);
        ps.executeUpdate();
    }

    private void deleteLokacionByBiznesId(int biznesId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();

        // Get location ID first
        String getSql = "SELECT lokacion_id FROM biznes_lokacion WHERE biznes_id=?";
        PreparedStatement getPs = conn.prepareStatement(getSql);
        getPs.setInt(1, biznesId);
        ResultSet rs = getPs.executeQuery();

        if (rs.next()) {
            int lokacionId = rs.getInt("lokacion_id");

            // Delete from biznes_lokacion
            String deleteLinkSql = "DELETE FROM biznes_lokacion WHERE biznes_id=?";
            PreparedStatement deleteLinkPs = conn.prepareStatement(deleteLinkSql);
            deleteLinkPs.setInt(1, biznesId);
            deleteLinkPs.executeUpdate();

            // Delete location
            String deleteLocSql = "DELETE FROM lokacion WHERE lokacion_id=?";
            PreparedStatement deleteLocPs = conn.prepareStatement(deleteLocSql);
            deleteLocPs.setInt(1, lokacionId);
            deleteLocPs.executeUpdate();
        }
    }

    private Biznes mapResultSetToBiznes(ResultSet rs) throws SQLException {
        Biznes biznes = new Biznes();
        biznes.setBiznesId(rs.getInt("biznes_id"));
        biznes.setEmri(rs.getString("emri"));
        biznes.setPershkrim(rs.getString("pershkrim"));
        biznes.setKategori(rs.getString("kategori"));
        biznes.setNipt(rs.getString("nipt"));
        biznes.setLicense(rs.getString("license"));
        biznes.setTelefon(rs.getString("telefon"));
        biznes.setEmail(rs.getString("email"));
        biznes.setWebsite(rs.getString("website"));
        biznes.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        biznes.setUpdatedAt(rs.getTimestamp("updated_at") != null ?
                rs.getTimestamp("updated_at").toLocalDateTime() : null);

        return biznes;
    }
}
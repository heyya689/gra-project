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

    // ==================== FIND ====================
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

    public List<Biznes> findByCategory(String categoryName) throws Exception {
        List<Biznes> businesses = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT b.* FROM biznes b " +
                "WHERE b.kategori LIKE ? ORDER BY b.emri";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + categoryName + "%");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            businesses.add(mapResultSetToBiznes(rs));
        }
        return businesses;
    }

    // ==================== SAVE ====================
    public void save(Biznes biznes, int kategoriId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();

        try {
            conn.setAutoCommit(false); // Fillojmë një transaksion

            // Save biznes
            String sql = "INSERT INTO biznes (emri, pershkrim, kategori, nipt, license, telefon, email, website) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
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

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int newBiznesId = rs.getInt(1);
                biznes.setBiznesId(newBiznesId);

                // Link with category
                addKategoriToBiznes(newBiznesId, kategoriId);

                System.out.println("DEBUG: Biznesi u ruajt me ID: " + newBiznesId);
                System.out.println("DEBUG: Duke lidhur me kategorinë ID: " + kategoriId);
            }

            conn.commit(); // Përfundo transaksionin
            System.out.println("DEBUG: Transaksioni u krye me sukses!");

        } catch (Exception e) {
            conn.rollback(); // Rikthe nëse ka gabim
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public void update(Biznes biznes) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE biznes SET emri=?, pershkrim=?, kategori=?, nipt=?, license=?, telefon=?, email=?, website=?, updated_at=? WHERE biznes_id=?";
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
    }

    public void delete(int biznesId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();

        // Delete categories first
        deleteBiznesKategorite(biznesId);

        // Delete location
        deleteLokacionByBiznesId(biznesId);

        // Delete biznes
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
        return rs.next() && rs.getInt(1) > 0;
    }

    public int countBusinesses() throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM biznes";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getInt(1) : 0;
    }

    // ==================== CATEGORY LINK METHODS (PUBLIC) ====================
    public void addKategoriToBiznes(int biznesId, int kategoriId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();

        // Kontrollo nëse ekziston lidhja
        String checkSql = "SELECT COUNT(*) FROM biznes_kategori WHERE biznes_id=? AND kategori_id=?";
        PreparedStatement checkPs = conn.prepareStatement(checkSql);
        checkPs.setInt(1, biznesId);
        checkPs.setInt(2, kategoriId);
        ResultSet rs = checkPs.executeQuery();

        if (rs.next() && rs.getInt(1) == 0) {
            String sql = "INSERT INTO biznes_kategori (biznes_id, kategori_id) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, biznesId);
            ps.setInt(2, kategoriId);
            int rows = ps.executeUpdate();
            System.out.println("DEBUG: Rreshtat e ndikuara në biznes_kategori: " + rows);
        } else {
            System.out.println("DEBUG: Lidhja ekziston tashmë!");
        }
    }

    public void removeKategoriFromBiznes(int biznesId, int kategoriId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM biznes_kategori WHERE biznes_id=? AND kategori_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, biznesId);
        ps.setInt(2, kategoriId);
        ps.executeUpdate();
    }

    // ==================== PRIVATE HELPERS ====================
    private Lokacion findLokacionByBiznesId(int biznesId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT l.* FROM lokacion l INNER JOIN biznes_lokacion bl ON l.lokacion_id = bl.lokacion_id WHERE bl.biznes_id = ?";
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
            lokacion.setCreatedAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null);
            return lokacion;
        }
        return null;
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
        String getSql = "SELECT lokacion_id FROM biznes_lokacion WHERE biznes_id=?";
        PreparedStatement getPs = conn.prepareStatement(getSql);
        getPs.setInt(1, biznesId);
        ResultSet rs = getPs.executeQuery();

        if (rs.next()) {
            int lokacionId = rs.getInt("lokacion_id");
            // delete link
            String deleteLinkSql = "DELETE FROM biznes_lokacion WHERE biznes_id=?";
            PreparedStatement deleteLinkPs = conn.prepareStatement(deleteLinkSql);
            deleteLinkPs.setInt(1, biznesId);
            deleteLinkPs.executeUpdate();

            // delete location
            String deleteLocSql = "DELETE FROM lokacion WHERE lokacion_id=?";
            PreparedStatement deleteLocPs = conn.prepareStatement(deleteLocSql);
            deleteLocPs.setInt(1, lokacionId);
            deleteLocPs.executeUpdate();
        }
    }

    public Biznes mapResultSetToBiznes(ResultSet rs) throws SQLException {
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
        biznes.setCreatedAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null);
        biznes.setUpdatedAt(rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null);
        return biznes;
    }
    // Shtoni këto metoda në BiznesDAO.java:

    // ==================== BUSINESS CATEGORIES ====================
    public List<Biznes> findBusinessesByCategoryId(int kategoriId) throws Exception {
        List<Biznes> businesses = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT b.* FROM biznes b " +
                "INNER JOIN biznes_kategori bk ON b.biznes_id = bk.biznes_id " +
                "WHERE bk.kategori_id = ? ORDER BY b.emri";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, kategoriId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            businesses.add(mapResultSetToBiznes(rs));
        }
        return businesses;
    }

    public List<Kategori> getCategoriesForBusiness(int biznesId) throws Exception {
        return findKategoriteByBiznesId(biznesId); // Metoda ekzistuese private, duhet ta bëjmë public
    }

    // Ndryshoni këtë metodë nga private në public:
    public List<Kategori> findKategoriteByBiznesId(int biznesId) throws Exception {
        List<Kategori> kategorite = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT k.* FROM kategori k INNER JOIN biznes_kategori bk ON k.kategori_id = bk.kategori_id WHERE bk.biznes_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, biznesId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Kategori k = new Kategori();
            k.setKategoriId(rs.getInt("kategori_id"));
            k.setEmri(rs.getString("emri"));
            k.setIkona(rs.getString("ikona"));
            k.setPershkrim(rs.getString("pershkrim"));
            k.setCreatedAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null);
            kategorite.add(k);
        }
        return kategorite;
    }

    // ==================== SEARCH METHODS ====================
    public List<Biznes> searchByCity(String qyteti) throws Exception {
        List<Biznes> businesses = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        // Kjo kërkon që të keni një lidhje me lokacion
        String sql = "SELECT b.* FROM biznes b " +
                "INNER JOIN biznes_lokacion bl ON b.biznes_id = bl.biznes_id " +
                "INNER JOIN lokacion l ON bl.lokacion_id = l.lokacion_id " +
                "WHERE l.qyteti LIKE ? ORDER BY b.emri";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + qyteti + "%");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            businesses.add(mapResultSetToBiznes(rs));
        }
        return businesses;
    }

    public List<Biznes> searchByName(String emri) throws Exception {
        List<Biznes> businesses = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM biznes WHERE emri LIKE ? ORDER BY emri";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + emri + "%");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            businesses.add(mapResultSetToBiznes(rs));
        }
        return businesses;
    }

    public List<Biznes> searchByCityAndCategory(String qyteti, String kategoriEmri) throws Exception {
        List<Biznes> businesses = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();

        // Kjo është më komplekse sepse kërkon lidhje të shumta
        String sql = "SELECT DISTINCT b.* FROM biznes b " +
                "LEFT JOIN biznes_lokacion bl ON b.biznes_id = bl.biznes_id " +
                "LEFT JOIN lokacion l ON bl.lokacion_id = l.lokacion_id " +
                "LEFT JOIN biznes_kategori bk ON b.biznes_id = bk.biznes_id " +
                "LEFT JOIN kategori k ON bk.kategori_id = k.kategori_id " +
                "WHERE (? = '' OR l.qyteti LIKE ?) " +
                "AND (? = '' OR k.emri LIKE ? OR b.kategori LIKE ?) " +
                "ORDER BY b.emri";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, qyteti != null ? qyteti : "");
        ps.setString(2, "%" + (qyteti != null ? qyteti : "") + "%");
        ps.setString(3, kategoriEmri != null ? kategoriEmri : "");
        ps.setString(4, "%" + (kategoriEmri != null ? kategoriEmri : "") + "%");
        ps.setString(5, "%" + (kategoriEmri != null ? kategoriEmri : "") + "%");

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            businesses.add(mapResultSetToBiznes(rs));
        }
        return businesses;
    }
}
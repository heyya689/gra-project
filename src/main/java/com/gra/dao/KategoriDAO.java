package com.gra.dao;

import com.gra.db.DBConnection;
import com.gra.model.Kategori;
import com.gra.model.Biznes;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class KategoriDAO {

    private BiznesDAO biznesDAO;
    private Scanner scanner;

    public KategoriDAO() {
        this.biznesDAO = new BiznesDAO();
        this.scanner = new Scanner(System.in);
    }

    // ==================== FIND ====================
    public Kategori findById(int id) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM kategori WHERE kategori_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Kategori kategori = mapResultSetToKategori(rs);

            // Ngarko bizneset e kësaj kategorie
            kategori.setBizneset(findBiznesetByKategoriId(kategori.getKategoriId()));

            return kategori;
        }
        return null;
    }

    public Kategori findByEmri(String emri) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM kategori WHERE emri = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, emri);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return mapResultSetToKategori(rs);
        }
        return null;
    }

    public List<Kategori> findAll() throws Exception {
        List<Kategori> kategorite = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM kategori ORDER BY emri");

        while (rs.next()) {
            kategorite.add(mapResultSetToKategori(rs));
        }
        return kategorite;
    }

    public List<Kategori> findCategoriesWithBusinesses() throws Exception {
        List<Kategori> kategorite = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT k.* FROM kategori k " +
                "INNER JOIN biznes_kategori bk ON k.kategori_id = bk.kategori_id " +
                "GROUP BY k.kategori_id HAVING COUNT(bk.biznes_id) > 0 " +
                "ORDER BY k.emri";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Kategori kategori = mapResultSetToKategori(rs);
            kategori.setBizneset(findBiznesetByKategoriId(kategori.getKategoriId()));
            kategorite.add(kategori);
        }
        return kategorite;
    }

    // ==================== SAVE / UPDATE / DELETE ====================
    public void save(Kategori kategori) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO kategori (emri, ikona, pershkrim) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setString(1, kategori.getEmri());
        ps.setString(2, kategori.getIkona());
        ps.setString(3, kategori.getPershkrim());

        ps.executeUpdate();

        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            kategori.setKategoriId(generatedKeys.getInt(1));
        }
    }

    public void update(Kategori kategori) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE kategori SET emri=?, ikona=?, pershkrim=? WHERE kategori_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, kategori.getEmri());
        ps.setString(2, kategori.getIkona());
        ps.setString(3, kategori.getPershkrim());
        ps.setInt(4, kategori.getKategoriId());

        ps.executeUpdate();
    }

    public void delete(int kategoriId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();

        // Fshi lidhjet në biznes_kategori
        String deleteLinkSql = "DELETE FROM biznes_kategori WHERE kategori_id=?";
        PreparedStatement deleteLinkPs = conn.prepareStatement(deleteLinkSql);
        deleteLinkPs.setInt(1, kategoriId);
        deleteLinkPs.executeUpdate();

        // Fshi kategorinë
        String sql = "DELETE FROM kategori WHERE kategori_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, kategoriId);
        ps.executeUpdate();
    }

    // ==================== BUSINESS LINK ====================
    public void addBusinessToCategory(int kategoriId, int biznesId) throws Exception {
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
            System.out.println("DEBUG [KategoriDAO]: Lidhja u shtua - Biznes ID: " + biznesId + ", Kategori ID: " + kategoriId + ", Rreshta: " + rows);
        } else {
            System.out.println("DEBUG [KategoriDAO]: Lidhja ekziston tashmë!");
        }
    }

    public void removeBusinessFromCategory(int kategoriId, int biznesId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM biznes_kategori WHERE biznes_id=? AND kategori_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, biznesId);
        ps.setInt(2, kategoriId);
        ps.executeUpdate();
    }

    // ==================== COUNTS / SEARCH ====================
    public int countCategories() throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM kategori";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public int countBusinessesInCategory(int kategoriId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM biznes_kategori WHERE kategori_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, kategoriId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public List<Kategori> searchByEmri(String emri) throws Exception {
        List<Kategori> kategorite = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM kategori WHERE emri LIKE ? ORDER BY emri";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + emri + "%");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            kategorite.add(mapResultSetToKategori(rs));
        }
        return kategorite;
    }

    // ==================== FIND BUSINESSES IN CATEGORY ====================
    public List<Biznes> findBiznesetByKategoriId(int kategoriId) throws Exception {
        List<Biznes> businesses = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();

        String sql = "SELECT b.* FROM biznes b " +
                "INNER JOIN biznes_kategori bk ON b.biznes_id = bk.biznes_id " +
                "WHERE bk.kategori_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, kategoriId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Biznes b = biznesDAO.mapResultSetToBiznes(rs);
            businesses.add(b);
        }

        return businesses;
    }

    // ==================== DISPLAY BUSINESSES ====================
    public void showBusinessesInCategory() {
        try {
            System.out.print("\nShkruaj ID-në e kategorisë: ");
            int categoryId = Integer.parseInt(scanner.nextLine());

            Kategori kategori = findById(categoryId);
            if (kategori == null) {
                System.out.println("❌ Kategoria nuk u gjet!");
                return;
            }

            List<Biznes> businesses = kategori.getBizneset();

            if (businesses == null || businesses.isEmpty()) {
                System.out.println("Nuk ka biznese në kategorinë: " + kategori.getEmri());
            } else {
                System.out.println("\n=== BIZNESET E KATEGORISË: " + kategori.getEmri() + " ===");
                for (Biznes b : businesses) {
                    System.out.printf("ID: %d | Emri: %s | NIPT: %s | Email: %s\n",
                            b.getBiznesId(), b.getEmri(), b.getNipt(),
                            b.getEmail() != null ? b.getEmail() : "N/A");
                }
            }
        } catch (Exception e) {
            System.err.println("Gabim: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ==================== MAPPER ====================
    private Kategori mapResultSetToKategori(ResultSet rs) throws SQLException {
        Kategori kategori = new Kategori();
        kategori.setKategoriId(rs.getInt("kategori_id"));
        kategori.setEmri(rs.getString("emri"));
        kategori.setIkona(rs.getString("ikona"));
        kategori.setPershkrim(rs.getString("pershkrim"));
        kategori.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);

        return kategori;
    }
}
package com.gra.dao;

import com.gra.db.DBConnection;
import com.gra.model.FaqjaKategori;
import com.gra.model.FAQ;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FaqjaKategoriDAO {

    public FaqjaKategori findById(int id) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM faqja_kategori WHERE kategori_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            FaqjaKategori kategori = mapResultSetToFaqjaKategori(rs);

            // Load FAQs in this category
            kategori.setFaqs(findFAQsByKategoriId(kategori.getKategoriId()));

            return kategori;
        }
        return null;
    }

    public FaqjaKategori findByEmri(String emri) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM faqja_kategori WHERE emri = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, emri);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return mapResultSetToFaqjaKategori(rs);
        }
        return null;
    }

    public List<FaqjaKategori> findAll() throws Exception {
        List<FaqjaKategori> kategorite = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM faqja_kategori ORDER BY renditja, emri");

        while (rs.next()) {
            kategorite.add(mapResultSetToFaqjaKategori(rs));
        }
        return kategorite;
    }

    public List<FaqjaKategori> findCategoriesWithFAQs() throws Exception {
        List<FaqjaKategori> kategorite = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT fk.* FROM faqja_kategori fk " +
                "INNER JOIN faq_kategori fkc ON fk.kategori_id = fkc.kategori_id " +
                "INNER JOIN faq f ON fkc.faq_id = f.faq_id " +
                "WHERE f.is_active = TRUE " +
                "GROUP BY fk.kategori_id HAVING COUNT(f.faq_id) > 0 " +
                "ORDER BY fk.renditja, fk.emri";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            FaqjaKategori kategori = mapResultSetToFaqjaKategori(rs);
            kategori.setFaqs(findFAQsByKategoriId(kategori.getKategoriId()));
            kategorite.add(kategori);
        }
        return kategorite;
    }

    public void save(FaqjaKategori kategori) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO faqja_kategori (emri, pershkrim, renditja) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setString(1, kategori.getEmri());
        ps.setString(2, kategori.getPershkrim());
        ps.setInt(3, kategori.getRenditja());

        ps.executeUpdate();

        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            kategori.setKategoriId(generatedKeys.getInt(1));
        }
    }

    public void update(FaqjaKategori kategori) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE faqja_kategori SET emri=?, pershkrim=?, renditja=? WHERE kategori_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, kategori.getEmri());
        ps.setString(2, kategori.getPershkrim());
        ps.setInt(3, kategori.getRenditja());
        ps.setInt(4, kategori.getKategoriId());

        ps.executeUpdate();
    }

    public void delete(int kategoriId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();

        // First delete from faq_kategori
        String deleteLinkSql = "DELETE FROM faq_kategori WHERE kategori_id=?";
        PreparedStatement deleteLinkPs = conn.prepareStatement(deleteLinkSql);
        deleteLinkPs.setInt(1, kategoriId);
        deleteLinkPs.executeUpdate();

        // Then delete category
        String sql = "DELETE FROM faqja_kategori WHERE kategori_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, kategoriId);
        ps.executeUpdate();
    }

    public void addFAQToCategory(int kategoriId, int faqId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();

        // Check if already linked
        String checkSql = "SELECT COUNT(*) FROM faq_kategori WHERE kategori_id=? AND faq_id=?";
        PreparedStatement checkPs = conn.prepareStatement(checkSql);
        checkPs.setInt(1, kategoriId);
        checkPs.setInt(2, faqId);
        ResultSet rs = checkPs.executeQuery();

        if (rs.next() && rs.getInt(1) == 0) {
            // Not linked, insert new
            String sql = "INSERT INTO faq_kategori (kategori_id, faq_id) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, kategoriId);
            ps.setInt(2, faqId);
            ps.executeUpdate();
        }
    }

    public void removeFAQFromCategory(int kategoriId, int faqId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM faq_kategori WHERE kategori_id=? AND faq_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, kategoriId);
        ps.setInt(2, faqId);
        ps.executeUpdate();
    }

    public void updateOrder(int kategoriId, int newOrder) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE faqja_kategori SET renditja=? WHERE kategori_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, newOrder);
        ps.setInt(2, kategoriId);
        ps.executeUpdate();
    }

    public int countCategories() throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM faqja_kategori";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public int countFAQsInCategory(int kategoriId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM faq_kategori WHERE kategori_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, kategoriId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public List<FaqjaKategori> searchByEmri(String emri) throws Exception {
        List<FaqjaKategori> kategorite = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM faqja_kategori WHERE emri LIKE ? ORDER BY emri";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + emri + "%");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            kategorite.add(mapResultSetToFaqjaKategori(rs));
        }
        return kategorite;
    }

    // Private helper methods
    private List<FAQ> findFAQsByKategoriId(int kategoriId) throws Exception {
        List<FAQ> faqs = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT f.* FROM faq f " +
                "INNER JOIN faq_kategori fk ON f.faq_id = fk.faq_id " +
                "WHERE fk.kategori_id = ? AND f.is_active = TRUE ORDER BY f.renditja";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, kategoriId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            FAQ faq = new FAQ();
            faq.setFaqId(rs.getInt("faq_id"));
            faq.setPyetje(rs.getString("pyetje"));
            faq.setPergjigje(rs.getString("pergjigje"));
            faq.setRenditja(rs.getInt("renditja"));
            faq.setActive(rs.getBoolean("is_active"));
            faq.setCreatedAt(rs.getTimestamp("created_at") != null ?
                    rs.getTimestamp("created_at").toLocalDateTime() : null);

            faqs.add(faq);
        }
        return faqs;
    }

    private FaqjaKategori mapResultSetToFaqjaKategori(ResultSet rs) throws SQLException {
        FaqjaKategori kategori = new FaqjaKategori();
        kategori.setKategoriId(rs.getInt("kategori_id"));
        kategori.setEmri(rs.getString("emri"));
        kategori.setPershkrim(rs.getString("pershkrim"));
        kategori.setRenditja(rs.getInt("renditja"));
        kategori.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);

        return kategori;
    }
}
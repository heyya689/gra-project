package com.gra.dao;

import com.gra.db.DBConnection;
import com.gra.model.FAQ;
import com.gra.model.FaqjaKategori;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FAQDAO {

    public FAQ findById(int id) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM faq WHERE faq_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            FAQ faq = mapResultSetToFAQ(rs);

            // Load categories
            faq.setKategorite(findKategoriteByFaqId(faq.getFaqId()));

            return faq;
        }
        return null;
    }

    public List<FAQ> findAll() throws Exception {
        List<FAQ> faqs = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM faq ORDER BY renditja, faq_id");

        while (rs.next()) {
            faqs.add(mapResultSetToFAQ(rs));
        }
        return faqs;
    }

    public List<FAQ> findActiveFAQs() throws Exception {
        List<FAQ> faqs = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM faq WHERE is_active = TRUE ORDER BY renditja, faq_id";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            faqs.add(mapResultSetToFAQ(rs));
        }
        return faqs;
    }

    public List<FAQ> findByCategoryId(int kategoriId) throws Exception {
        List<FAQ> faqs = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT f.* FROM faq f " +
                "INNER JOIN faq_kategori fk ON f.faq_id = fk.faq_id " +
                "WHERE fk.kategori_id = ? AND f.is_active = TRUE ORDER BY f.renditja";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, kategoriId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            faqs.add(mapResultSetToFAQ(rs));
        }
        return faqs;
    }

    public List<FAQ> searchByQuestion(String keyword) throws Exception {
        List<FAQ> faqs = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM faq WHERE pyetje LIKE ? AND is_active = TRUE ORDER BY renditja";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + keyword + "%");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            faqs.add(mapResultSetToFAQ(rs));
        }
        return faqs;
    }

    public List<FAQ> searchByAnswer(String keyword) throws Exception {
        List<FAQ> faqs = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM faq WHERE pergjigje LIKE ? AND is_active = TRUE ORDER BY renditja";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + keyword + "%");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            faqs.add(mapResultSetToFAQ(rs));
        }
        return faqs;
    }

    public void save(FAQ faq) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO faq (pyetje, pergjigje, renditja, is_active) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setString(1, faq.getPyetje());
        ps.setString(2, faq.getPergjigje());
        ps.setInt(3, faq.getRenditja());
        ps.setBoolean(4, faq.isActive());

        ps.executeUpdate();

        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            int faqId = generatedKeys.getInt(1);
            faq.setFaqId(faqId);

            // Save categories if exists
            if (faq.getKategorite() != null && !faq.getKategorite().isEmpty()) {
                for (FaqjaKategori kategori : faq.getKategorite()) {
                    addFaqToCategory(faqId, kategori.getKategoriId());
                }
            }
        }
    }

    public void update(FAQ faq) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE faq SET pyetje=?, pergjigje=?, renditja=?, is_active=? WHERE faq_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, faq.getPyetje());
        ps.setString(2, faq.getPergjigje());
        ps.setInt(3, faq.getRenditja());
        ps.setBoolean(4, faq.isActive());
        ps.setInt(5, faq.getFaqId());

        ps.executeUpdate();

        // Update categories
        updateFaqCategories(faq.getFaqId(), faq.getKategorite());
    }

    public void delete(int faqId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();

        // First delete from faq_kategori
        deleteFaqCategories(faqId);

        // Then delete FAQ
        String sql = "DELETE FROM faq WHERE faq_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, faqId);
        ps.executeUpdate();
    }

    public void activate(int faqId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE faq SET is_active=TRUE WHERE faq_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, faqId);
        ps.executeUpdate();
    }

    public void deactivate(int faqId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE faq SET is_active=FALSE WHERE faq_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, faqId);
        ps.executeUpdate();
    }

    public void updateOrder(int faqId, int newOrder) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE faq SET renditja=? WHERE faq_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, newOrder);
        ps.setInt(2, faqId);
        ps.executeUpdate();
    }

    public int countFAQs() throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM faq";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public int countActiveFAQs() throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM faq WHERE is_active = TRUE";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public List<FAQ> findLatestFAQs(int limit) throws Exception {
        List<FAQ> faqs = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM faq WHERE is_active = TRUE ORDER BY created_at DESC LIMIT ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, limit);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            faqs.add(mapResultSetToFAQ(rs));
        }
        return faqs;
    }

    // Private helper methods
    private List<FaqjaKategori> findKategoriteByFaqId(int faqId) throws Exception {
        List<FaqjaKategori> kategorite = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT fk.* FROM faqja_kategori fk " +
                "INNER JOIN faq_kategori fkc ON fk.kategori_id = fkc.kategori_id " +
                "WHERE fkc.faq_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, faqId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            FaqjaKategori kategori = new FaqjaKategori();
            kategori.setKategoriId(rs.getInt("kategori_id"));
            kategori.setEmri(rs.getString("emri"));
            kategori.setPershkrim(rs.getString("pershkrim"));
            kategori.setRenditja(rs.getInt("renditja"));
            kategori.setCreatedAt(rs.getTimestamp("created_at") != null ?
                    rs.getTimestamp("created_at").toLocalDateTime() : null);

            kategorite.add(kategori);
        }
        return kategorite;
    }

    private void addFaqToCategory(int faqId, int kategoriId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();

        // Check if already linked
        String checkSql = "SELECT COUNT(*) FROM faq_kategori WHERE faq_id=? AND kategori_id=?";
        PreparedStatement checkPs = conn.prepareStatement(checkSql);
        checkPs.setInt(1, faqId);
        checkPs.setInt(2, kategoriId);
        ResultSet rs = checkPs.executeQuery();

        if (rs.next() && rs.getInt(1) == 0) {
            // Not linked, insert new
            String sql = "INSERT INTO faq_kategori (faq_id, kategori_id) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, faqId);
            ps.setInt(2, kategoriId);
            ps.executeUpdate();
        }
    }

    private void updateFaqCategories(int faqId, List<FaqjaKategori> kategorite) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();

        // First delete existing categories
        deleteFaqCategories(faqId);

        // Then add new categories
        if (kategorite != null) {
            for (FaqjaKategori kategori : kategorite) {
                addFaqToCategory(faqId, kategori.getKategoriId());
            }
        }
    }

    private void deleteFaqCategories(int faqId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM faq_kategori WHERE faq_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, faqId);
        ps.executeUpdate();
    }

    private FAQ mapResultSetToFAQ(ResultSet rs) throws SQLException {
        FAQ faq = new FAQ();
        faq.setFaqId(rs.getInt("faq_id"));
        faq.setPyetje(rs.getString("pyetje"));
        faq.setPergjigje(rs.getString("pergjigje"));
        faq.setRenditja(rs.getInt("renditja"));
        faq.setActive(rs.getBoolean("is_active"));
        faq.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);

        return faq;
    }
}
package com.gra.dao;

import com.gra.db.DBConnection;
import com.gra.model.Kontakt;
import com.gra.model.User;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class KontaktDAO {

    private UserDAO userDAO;

    public KontaktDAO() {
        this.userDAO = new UserDAO();
    }

    public Kontakt findById(int id) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM kontakt WHERE kontakt_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return mapResultSetToKontakt(rs);
        }
        return null;
    }

    public List<Kontakt> findAll() throws Exception {
        List<Kontakt> kontaktet = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM kontakt ORDER BY created_at DESC");

        while (rs.next()) {
            kontaktet.add(mapResultSetToKontakt(rs));
        }
        return kontaktet;
    }

    public List<Kontakt> findByUserId(int userId) throws Exception {
        List<Kontakt> kontaktet = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM kontakt WHERE user_id = ? ORDER BY created_at DESC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            kontaktet.add(mapResultSetToKontakt(rs));
        }
        return kontaktet;
    }

    public List<Kontakt> findByEmail(String email) throws Exception {
        List<Kontakt> kontaktet = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM kontakt WHERE email = ? ORDER BY created_at DESC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            kontaktet.add(mapResultSetToKontakt(rs));
        }
        return kontaktet;
    }

    public List<Kontakt> findByStatus(String status) throws Exception {
        List<Kontakt> kontaktet = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM kontakt WHERE status = ? ORDER BY created_at DESC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, status);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            kontaktet.add(mapResultSetToKontakt(rs));
        }
        return kontaktet;
    }

    public List<Kontakt> findOpenMessages() throws Exception {
        List<Kontakt> kontaktet = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM kontakt WHERE status IN ('PENDING', 'READ') ORDER BY created_at DESC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            kontaktet.add(mapResultSetToKontakt(rs));
        }
        return kontaktet;
    }

    public List<Kontakt> searchBySubject(String keyword) throws Exception {
        List<Kontakt> kontaktet = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM kontakt WHERE subjekti LIKE ? ORDER BY created_at DESC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + keyword + "%");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            kontaktet.add(mapResultSetToKontakt(rs));
        }
        return kontaktet;
    }

    public void save(Kontakt kontakt) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO kontakt (user_id, email, subjekti, mesazh, status) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        if (kontakt.getUser() != null) {
            ps.setInt(1, kontakt.getUser().getUserId());
        } else {
            ps.setNull(1, Types.INTEGER);
        }

        ps.setString(2, kontakt.getEmail());
        ps.setString(3, kontakt.getSubjekti());
        ps.setString(4, kontakt.getMesazh());
        ps.setString(5, kontakt.getStatus());

        ps.executeUpdate();

        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            kontakt.setKontaktId(generatedKeys.getInt(1));
        }
    }

    public void update(Kontakt kontakt) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE kontakt SET user_id=?, email=?, subjekti=?, mesazh=?, status=?, " +
                "updated_at=? WHERE kontakt_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        if (kontakt.getUser() != null) {
            ps.setInt(1, kontakt.getUser().getUserId());
        } else {
            ps.setNull(1, Types.INTEGER);
        }

        ps.setString(2, kontakt.getEmail());
        ps.setString(3, kontakt.getSubjekti());
        ps.setString(4, kontakt.getMesazh());
        ps.setString(5, kontakt.getStatus());
        ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
        ps.setInt(7, kontakt.getKontaktId());

        ps.executeUpdate();
    }

    public void updateStatus(int kontaktId, String status) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE kontakt SET status=?, updated_at=? WHERE kontakt_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, status);
        ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
        ps.setInt(3, kontaktId);

        ps.executeUpdate();
    }

    public void markAsRead(int kontaktId) throws Exception {
        updateStatus(kontaktId, "READ");
    }

    public void markAsReplied(int kontaktId) throws Exception {
        updateStatus(kontaktId, "REPLIED");
    }

    public void markAsClosed(int kontaktId) throws Exception {
        updateStatus(kontaktId, "CLOSED");
    }

    public void delete(int kontaktId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM kontakt WHERE kontakt_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, kontaktId);
        ps.executeUpdate();
    }

    public void deleteOldMessages(int days) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM kontakt WHERE created_at < DATE_SUB(NOW(), INTERVAL ? DAY) AND status = 'CLOSED'";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, days);
        ps.executeUpdate();
    }

    public int countMessages() throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM kontakt";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public int countMessagesByStatus(String status) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM kontakt WHERE status = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, status);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public int countUnreadMessages() throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM kontakt WHERE status = 'PENDING'";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public List<Kontakt> findLatestMessages(int limit) throws Exception {
        List<Kontakt> kontaktet = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM kontakt ORDER BY created_at DESC LIMIT ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, limit);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            kontaktet.add(mapResultSetToKontakt(rs));
        }
        return kontaktet;
    }

    private Kontakt mapResultSetToKontakt(ResultSet rs) throws Exception {
        Kontakt kontakt = new Kontakt();
        kontakt.setKontaktId(rs.getInt("kontakt_id"));

        // Load user if exists
        int userId = rs.getInt("user_id");
        if (userId > 0) {
            User user = userDAO.findById(userId);
            kontakt.setUser(user);
        }

        kontakt.setEmail(rs.getString("email"));
        kontakt.setSubjekti(rs.getString("subjekti"));
        kontakt.setMesazh(rs.getString("mesazh"));
        kontakt.setStatus(rs.getString("status"));
        kontakt.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        kontakt.setUpdatedAt(rs.getTimestamp("updated_at") != null ?
                rs.getTimestamp("updated_at").toLocalDateTime() : null);

        return kontakt;
    }
}
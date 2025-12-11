package com.gra.dao;

import com.gra.db.DBConnection;
import com.gra.model.Notifikime;
import com.gra.model.User;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotifikimeDAO {

    private UserDAO userDAO;

    public NotifikimeDAO() {
        this.userDAO = new UserDAO();
    }

    public Notifikime findById(int id) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM notifikime WHERE njoftim_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return mapResultSetToNotifikime(rs);
        }
        return null;
    }

    public List<Notifikime> findAll() throws Exception {
        List<Notifikime> notifikimet = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM notifikime ORDER BY data DESC");

        while (rs.next()) {
            notifikimet.add(mapResultSetToNotifikime(rs));
        }
        return notifikimet;
    }

    public List<Notifikime> findByUserId(int userId) throws Exception {
        List<Notifikime> notifikimet = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM notifikime WHERE user_id = ? ORDER BY data DESC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            notifikimet.add(mapResultSetToNotifikime(rs));
        }
        return notifikimet;
    }

    public List<Notifikime> findUnreadByUserId(int userId) throws Exception {
        List<Notifikime> notifikimet = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM notifikime WHERE user_id = ? AND lexuar = FALSE ORDER BY data DESC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            notifikimet.add(mapResultSetToNotifikime(rs));
        }
        return notifikimet;
    }

    public List<Notifikime> findByType(String type) throws Exception {
        List<Notifikime> notifikimet = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM notifikime WHERE tipi = ? ORDER BY data DESC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, type);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            notifikimet.add(mapResultSetToNotifikime(rs));
        }
        return notifikimet;
    }

    public List<Notifikime> findRecentNotifications(int days) throws Exception {
        List<Notifikime> notifikimet = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM notifikime WHERE data >= DATE_SUB(NOW(), INTERVAL ? DAY) ORDER BY data DESC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, days);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            notifikimet.add(mapResultSetToNotifikime(rs));
        }
        return notifikimet;
    }

    public void save(Notifikime notifikim) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO notifikime (user_id, titulli, mesazh, tipi, lexuar) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setInt(1, notifikim.getUser().getUserId());
        ps.setString(2, notifikim.getTitulli());
        ps.setString(3, notifikim.getMesazh());
        ps.setString(4, notifikim.getTipi());
        ps.setBoolean(5, notifikim.isLexuar());

        ps.executeUpdate();

        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            notifikim.setNjoftimId(generatedKeys.getInt(1));
        }
    }

    public void update(Notifikime notifikim) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE notifikime SET user_id=?, titulli=?, mesazh=?, tipi=?, lexuar=? WHERE njoftim_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, notifikim.getUser().getUserId());
        ps.setString(2, notifikim.getTitulli());
        ps.setString(3, notifikim.getMesazh());
        ps.setString(4, notifikim.getTipi());
        ps.setBoolean(5, notifikim.isLexuar());
        ps.setInt(6, notifikim.getNjoftimId());

        ps.executeUpdate();
    }

    public void markAsRead(int njoftimId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE notifikime SET lexuar=TRUE WHERE njoftim_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, njoftimId);
        ps.executeUpdate();
    }

    public void markAllAsRead(int userId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE notifikime SET lexuar=TRUE WHERE user_id=? AND lexuar=FALSE";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        ps.executeUpdate();
    }

    public void markAsUnread(int njoftimId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE notifikime SET lexuar=FALSE WHERE njoftim_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, njoftimId);
        ps.executeUpdate();
    }

    public void delete(int njoftimId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM notifikime WHERE njoftim_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, njoftimId);
        ps.executeUpdate();
    }

    public void deleteOldNotifications(int days) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM notifikime WHERE data < DATE_SUB(NOW(), INTERVAL ? DAY)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, days);
        ps.executeUpdate();
    }

    public int countNotifications() throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM notifikime";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public int countUnreadNotifications(int userId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM notifikime WHERE user_id = ? AND lexuar = FALSE";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public int countNotificationsByType(String type) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM notifikime WHERE tipi = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, type);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public void sendNotificationToAllUsers(String titulli, String mesazh, String tipi) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();

        // Get all users
        List<User> users = userDAO.findAll();

        for (User user : users) {
            Notifikime notifikim = new Notifikime();
            notifikim.setUser(user);
            notifikim.setTitulli(titulli);
            notifikim.setMesazh(mesazh);
            notifikim.setTipi(tipi);
            notifikim.setLexuar(false);

            save(notifikim);
        }
    }

    public void sendNotificationToUser(int userId, String titulli, String mesazh, String tipi) throws Exception {
        User user = userDAO.findById(userId);
        if (user != null) {
            Notifikime notifikim = new Notifikime();
            notifikim.setUser(user);
            notifikim.setTitulli(titulli);
            notifikim.setMesazh(mesazh);
            notifikim.setTipi(tipi);
            notifikim.setLexuar(false);

            save(notifikim);
        }
    }

    private Notifikime mapResultSetToNotifikime(ResultSet rs) throws Exception {
        Notifikime notifikim = new Notifikime();
        notifikim.setNjoftimId(rs.getInt("njoftim_id"));

        // Load user
        int userId = rs.getInt("user_id");
        User user = userDAO.findById(userId);
        notifikim.setUser(user);

        notifikim.setTitulli(rs.getString("titulli"));
        notifikim.setMesazh(rs.getString("mesazh"));
        notifikim.setTipi(rs.getString("tipi"));
        notifikim.setLexuar(rs.getBoolean("lexuar"));
        notifikim.setData(rs.getTimestamp("data") != null ?
                rs.getTimestamp("data").toLocalDateTime() : null);

        return notifikim;
    }
}
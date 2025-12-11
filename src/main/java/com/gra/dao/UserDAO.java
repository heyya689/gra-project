package com.gra.dao;

import com.gra.db.DBConnection;
import com.gra.model.User;
import com.gra.model.Preferenca;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public User findById(int id) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM user WHERE user_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            User user = mapResultSetToUser(rs);

            // Load user's preferences
            user.setPreferenca(findPreferencaByUserId(user.getUserId()));

            return user;
        }
        return null;
    }

    public User findByEmail(String email) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM user WHERE email = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            User user = mapResultSetToUser(rs);
            user.setPreferenca(findPreferencaByUserId(user.getUserId()));
            return user;
        }
        return null;
    }

    public List<User> findAll() throws Exception {
        List<User> users = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM user ORDER BY created_at DESC");

        while (rs.next()) {
            User user = mapResultSetToUser(rs);
            users.add(user);
        }
        return users;
    }

    public List<User> findByRole(String role) throws Exception {
        List<User> users = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT u.* FROM user u " +
                "INNER JOIN user_role ur ON u.user_id = ur.user_id " +
                "INNER JOIN role r ON ur.role_id = r.role_id " +
                "WHERE r.emri = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, role);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            users.add(mapResultSetToUser(rs));
        }
        return users;
    }

    public void save(User user) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();

        // Versioni pa 'phone'
        String sql = "INSERT INTO user (name, email, password) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setString(1, user.getName());
        ps.setString(2, user.getEmail());
        ps.setString(3, user.getPassword());
        // Mos përfshi 'phone' nëse kolona nuk ekziston

        ps.executeUpdate();

        // Get generated ID
        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            user.setUserId(generatedKeys.getInt(1));
        }

        // Save preferences if exists
        if (user.getPreferenca() != null) {
            savePreferenca(user.getPreferenca(), user.getUserId());
        }
    }

    public void update(User user) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE user SET name=?, email=?, password=?, phone=?, updated_at=? WHERE user_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, user.getName());
        ps.setString(2, user.getEmail());
        ps.setString(3, user.getPassword());
        ps.setString(4, user.getPhone());
        ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
        ps.setInt(6, user.getUserId());

        ps.executeUpdate();

        // Update preferences if exists
        if (user.getPreferenca() != null) {
            updatePreferenca(user.getPreferenca());
        }
    }

    public void delete(int userId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();

        // First delete preferences (foreign key constraint)
        deletePreferenca(userId);

        // Then delete user
        String sql = "DELETE FROM user WHERE user_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        ps.executeUpdate();
    }

    public boolean emailExists(String email) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM user WHERE email = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        return false;
    }

    public int countUsers() throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM user";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    // Private helper methods for preferences
    private Preferenca findPreferencaByUserId(int userId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM preferenca WHERE user_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Preferenca preferenca = new Preferenca();
            preferenca.setPreferencaId(rs.getInt("preferenca_id"));
            preferenca.setNjoftimeAktive(rs.getBoolean("njoftime_aktive"));
            preferenca.setGjuha(rs.getString("gjuha"));
            preferenca.setTema(rs.getString("tema"));
            preferenca.setEmailNotifications(rs.getBoolean("email_notifications"));
            preferenca.setSmsNotifications(rs.getBoolean("sms_notifications"));
            preferenca.setCreatedAt(rs.getTimestamp("created_at") != null ?
                    rs.getTimestamp("created_at").toLocalDateTime() : null);
            preferenca.setUpdatedAt(rs.getTimestamp("updated_at") != null ?
                    rs.getTimestamp("updated_at").toLocalDateTime() : null);

            return preferenca;
        }
        return null;
    }

    private void savePreferenca(Preferenca preferenca, int userId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO preferenca (user_id, njoftime_aktive, gjuha, tema, " +
                "email_notifications, sms_notifications) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setInt(1, userId);
        ps.setBoolean(2, preferenca.isNjoftimeAktive());
        ps.setString(3, preferenca.getGjuha());
        ps.setString(4, preferenca.getTema());
        ps.setBoolean(5, preferenca.isEmailNotifications());
        ps.setBoolean(6, preferenca.isSmsNotifications());

        ps.executeUpdate();

        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            preferenca.setPreferencaId(generatedKeys.getInt(1));
        }
    }

    private void updatePreferenca(Preferenca preferenca) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE preferenca SET njoftime_aktive=?, gjuha=?, tema=?, " +
                "email_notifications=?, sms_notifications=?, updated_at=? WHERE preferenca_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setBoolean(1, preferenca.isNjoftimeAktive());
        ps.setString(2, preferenca.getGjuha());
        ps.setString(3, preferenca.getTema());
        ps.setBoolean(4, preferenca.isEmailNotifications());
        ps.setBoolean(5, preferenca.isSmsNotifications());
        ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
        ps.setInt(7, preferenca.getPreferencaId());

        ps.executeUpdate();
    }

    private void deletePreferenca(int userId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM preferenca WHERE user_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        ps.executeUpdate();
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));

        // VETËM NËSE I KE KOLONA NË DATABAZË:
        // user.setRole(UserRole.valueOf(rs.getString("role")));
        // user.setCreatedAt(rs.getTimestamp("created_at"));
        // user.setUpdatedAt(rs.getTimestamp("updated_at"));

        return user;
    }
}
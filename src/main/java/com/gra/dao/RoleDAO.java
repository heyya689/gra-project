package com.gra.dao;

import com.gra.db.DBConnection;
import com.gra.model.Role;
import com.gra.model.User;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO {

    public Role findById(int id) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM role WHERE role_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return mapResultSetToRole(rs);
        }
        return null;
    }

    public Role findByEmri(String emri) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM role WHERE emri = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, emri);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return mapResultSetToRole(rs);
        }
        return null;
    }

    public List<Role> findAll() throws Exception {
        List<Role> roles = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM role ORDER BY emri");

        while (rs.next()) {
            roles.add(mapResultSetToRole(rs));
        }
        return roles;
    }

    public void save(Role role) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO role (emri, description, permissions) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setString(1, role.getEmri());
        ps.setString(2, role.getDescription());
        ps.setString(3, role.getPermissions());

        ps.executeUpdate();

        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            role.setRoleId(generatedKeys.getInt(1));
        }
    }

    public void update(Role role) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE role SET emri=?, description=?, permissions=? WHERE role_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, role.getEmri());
        ps.setString(2, role.getDescription());
        ps.setString(3, role.getPermissions());
        ps.setInt(4, role.getRoleId());

        ps.executeUpdate();
    }

    public void delete(int roleId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();

        // First delete from user_role table
        String deleteUserRoleSql = "DELETE FROM user_role WHERE role_id=?";
        PreparedStatement ps1 = conn.prepareStatement(deleteUserRoleSql);
        ps1.setInt(1, roleId);
        ps1.executeUpdate();

        // Then delete role
        String sql = "DELETE FROM role WHERE role_id=?";
        PreparedStatement ps2 = conn.prepareStatement(sql);
        ps2.setInt(1, roleId);
        ps2.executeUpdate();
    }

    public void assignRoleToUser(int userId, int roleId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();

        // Check if already assigned
        String checkSql = "SELECT COUNT(*) FROM user_role WHERE user_id=? AND role_id=?";
        PreparedStatement checkPs = conn.prepareStatement(checkSql);
        checkPs.setInt(1, userId);
        checkPs.setInt(2, roleId);
        ResultSet rs = checkPs.executeQuery();

        if (rs.next() && rs.getInt(1) == 0) {
            // Not assigned, insert new
            String sql = "INSERT INTO user_role (user_id, role_id) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, roleId);
            ps.executeUpdate();
        }
    }

    public void removeRoleFromUser(int userId, int roleId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM user_role WHERE user_id=? AND role_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        ps.setInt(2, roleId);
        ps.executeUpdate();
    }

    public List<Role> findRolesByUserId(int userId) throws Exception {
        List<Role> roles = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT r.* FROM role r " +
                "INNER JOIN user_role ur ON r.role_id = ur.role_id " +
                "WHERE ur.user_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            roles.add(mapResultSetToRole(rs));
        }
        return roles;
    }

    public List<User> findUsersByRoleId(int roleId) throws Exception {
        List<User> users = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT u.* FROM user u " +
                "INNER JOIN user_role ur ON u.user_id = ur.user_id " +
                "WHERE ur.role_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, roleId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            User user = new User();
            user.setUserId(rs.getInt("user_id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            users.add(user);
        }
        return users;
    }

    private Role mapResultSetToRole(ResultSet rs) throws SQLException {
        Role role = new Role();
        role.setRoleId(rs.getInt("role_id"));
        role.setEmri(rs.getString("emri"));
        role.setDescription(rs.getString("description"));
        role.setPermissions(rs.getString("permissions"));
        role.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);

        return role;
    }
}
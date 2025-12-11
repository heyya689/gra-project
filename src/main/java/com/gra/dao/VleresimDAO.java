package com.gra.dao;

import com.gra.db.DBConnection;
import com.gra.model.Vleresim;
import com.gra.model.User;
import com.gra.model.Biznes;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VleresimDAO {

    private UserDAO userDAO;
    private BiznesDAO biznesDAO;

    public VleresimDAO() {
        this.userDAO = new UserDAO();
        this.biznesDAO = new BiznesDAO();
    }

    public Vleresim findById(int id) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM vleresim WHERE vleresim_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return mapResultSetToVleresim(rs);
        }
        return null;
    }

    public List<Vleresim> findAll() throws Exception {
        List<Vleresim> vleresimet = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM vleresim ORDER BY created_at DESC");

        while (rs.next()) {
            vleresimet.add(mapResultSetToVleresim(rs));
        }
        return vleresimet;
    }

    public List<Vleresim> findByUserId(int userId) throws Exception {
        List<Vleresim> vleresimet = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM vleresim WHERE user_id = ? ORDER BY created_at DESC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            vleresimet.add(mapResultSetToVleresim(rs));
        }
        return vleresimet;
    }

    public List<Vleresim> findByBusinessId(int businessId) throws Exception {
        List<Vleresim> vleresimet = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM vleresim WHERE biznes_id = ? ORDER BY created_at DESC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, businessId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            vleresimet.add(mapResultSetToVleresim(rs));
        }
        return vleresimet;
    }

    public List<Vleresim> findByRating(int rating) throws Exception {
        List<Vleresim> vleresimet = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM vleresim WHERE rating = ? ORDER BY created_at DESC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, rating);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            vleresimet.add(mapResultSetToVleresim(rs));
        }
        return vleresimet;
    }

    public List<Vleresim> findApprovedReviews() throws Exception {
        List<Vleresim> vleresimet = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM vleresim WHERE is_approved = TRUE ORDER BY created_at DESC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            vleresimet.add(mapResultSetToVleresim(rs));
        }
        return vleresimet;
    }

    public List<Vleresim> findPendingReviews() throws Exception {
        List<Vleresim> vleresimet = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM vleresim WHERE is_approved = FALSE ORDER BY created_at DESC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            vleresimet.add(mapResultSetToVleresim(rs));
        }
        return vleresimet;
    }

    public List<Vleresim> searchByComment(String keyword) throws Exception {
        List<Vleresim> vleresimet = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM vleresim WHERE koment LIKE ? ORDER BY created_at DESC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + keyword + "%");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            vleresimet.add(mapResultSetToVleresim(rs));
        }
        return vleresimet;
    }

    public void save(Vleresim vleresim) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO vleresim (user_id, biznes_id, rating, koment, is_approved) " +
                "VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setInt(1, vleresim.getUser().getUserId());
        ps.setInt(2, vleresim.getBiznes().getBiznesId());
        ps.setInt(3, vleresim.getRating());
        ps.setString(4, vleresim.getKoment());
        ps.setBoolean(5, vleresim.isApproved());

        ps.executeUpdate();

        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            vleresim.setVleresimId(generatedKeys.getInt(1));
        }
    }

    public void update(Vleresim vleresim) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE vleresim SET user_id=?, biznes_id=?, rating=?, koment=?, " +
                "is_approved=?, updated_at=? WHERE vleresim_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, vleresim.getUser().getUserId());
        ps.setInt(2, vleresim.getBiznes().getBiznesId());
        ps.setInt(3, vleresim.getRating());
        ps.setString(4, vleresim.getKoment());
        ps.setBoolean(5, vleresim.isApproved());
        ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
        ps.setInt(7, vleresim.getVleresimId());

        ps.executeUpdate();
    }

    public void approveReview(int vleresimId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE vleresim SET is_approved=TRUE, updated_at=? WHERE vleresim_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
        ps.setInt(2, vleresimId);
        ps.executeUpdate();
    }

    public void rejectReview(int vleresimId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE vleresim SET is_approved=FALSE, updated_at=? WHERE vleresim_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
        ps.setInt(2, vleresimId);
        ps.executeUpdate();
    }

    public void delete(int vleresimId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM vleresim WHERE vleresim_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, vleresimId);
        ps.executeUpdate();
    }

    public double getAverageRatingByBusinessId(int businessId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT AVG(rating) as average FROM vleresim WHERE biznes_id = ? AND is_approved = TRUE";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, businessId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getDouble("average");
        }
        return 0.0;
    }

    public int countReviewsByBusinessId(int businessId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM vleresim WHERE biznes_id = ? AND is_approved = TRUE";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, businessId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public int countReviewsByUserId(int userId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM vleresim WHERE user_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public int countTotalReviews() throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM vleresim";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public int countPendingReviews() throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM vleresim WHERE is_approved = FALSE";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public List<Vleresim> findLatestReviews(int limit) throws Exception {
        List<Vleresim> vleresimet = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM vleresim WHERE is_approved = TRUE ORDER BY created_at DESC LIMIT ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, limit);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            vleresimet.add(mapResultSetToVleresim(rs));
        }
        return vleresimet;
    }

    private Vleresim mapResultSetToVleresim(ResultSet rs) throws Exception {
        Vleresim vleresim = new Vleresim();
        vleresim.setVleresimId(rs.getInt("vleresim_id"));

        // Load user
        int userId = rs.getInt("user_id");
        User user = userDAO.findById(userId);
        vleresim.setUser(user);

        // Load business
        int biznesId = rs.getInt("biznes_id");
        Biznes biznes = biznesDAO.findById(biznesId);
        vleresim.setBiznes(biznes);

        vleresim.setRating(rs.getInt("rating"));
        vleresim.setKoment(rs.getString("koment"));
        vleresim.setApproved(rs.getBoolean("is_approved"));
        vleresim.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        vleresim.setUpdatedAt(rs.getTimestamp("updated_at") != null ?
                rs.getTimestamp("updated_at").toLocalDateTime() : null);

        return vleresim;
    }
}
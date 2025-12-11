package com.gra.dao;

import com.gra.db.DBConnection;
import com.gra.model.Rezervim;
import com.gra.model.User;
import com.gra.model.Biznes;
import com.gra.model.Inventari;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RezervimDAO {

    private UserDAO userDAO;
    private BiznesDAO biznesDAO;
    private InventariDAO inventariDAO;

    public RezervimDAO() {
        this.userDAO = new UserDAO();
        this.biznesDAO = new BiznesDAO();
        this.inventariDAO = new InventariDAO();
    }

    public Rezervim findById(int id) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM rezervim WHERE rezervim_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return mapResultSetToRezervim(rs);
        }
        return null;
    }

    public List<Rezervim> findAll() throws Exception {
        List<Rezervim> rezervimet = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM rezervim ORDER BY data DESC");

        while (rs.next()) {
            rezervimet.add(mapResultSetToRezervim(rs));
        }
        return rezervimet;
    }

    public List<Rezervim> findByUserId(int userId) throws Exception {
        List<Rezervim> rezervimet = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM rezervim WHERE user_id = ? ORDER BY data DESC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            rezervimet.add(mapResultSetToRezervim(rs));
        }
        return rezervimet;
    }

    public List<Rezervim> findByBusinessId(int businessId) throws Exception {
        List<Rezervim> rezervimet = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM rezervim WHERE biznes_id = ? ORDER BY data DESC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, businessId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            rezervimet.add(mapResultSetToRezervim(rs));
        }
        return rezervimet;
    }

    public List<Rezervim> findByStatus(String status) throws Exception {
        List<Rezervim> rezervimet = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM rezervim WHERE status = ? ORDER BY data DESC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, status);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            rezervimet.add(mapResultSetToRezervim(rs));
        }
        return rezervimet;
    }

    public List<Rezervim> findUpcomingReservations() throws Exception {
        List<Rezervim> rezervimet = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM rezervim WHERE data > NOW() AND status IN ('PENDING', 'CONFIRMED') " +
                "ORDER BY data ASC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            rezervimet.add(mapResultSetToRezervim(rs));
        }
        return rezervimet;
    }

    public List<Rezervim> findPastReservations() throws Exception {
        List<Rezervim> rezervimet = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM rezervim WHERE data <= NOW() ORDER BY data DESC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            rezervimet.add(mapResultSetToRezervim(rs));
        }
        return rezervimet;
    }

    public void save(Rezervim rezervim) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO rezervim (user_id, biznes_id, inventar_id, data, " +
                "numri_personave, shënime, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setInt(1, rezervim.getUser().getUserId());
        ps.setInt(2, rezervim.getBiznes().getBiznesId());

        if (rezervim.getInventar() != null) {
            ps.setInt(3, rezervim.getInventar().getInventarId());
        } else {
            ps.setNull(3, Types.INTEGER);
        }

        ps.setTimestamp(4, Timestamp.valueOf(rezervim.getData()));
        ps.setInt(5, rezervim.getNumriPersonave());
        ps.setString(6, rezervim.getShënime());
        ps.setString(7, rezervim.getStatus());

        ps.executeUpdate();

        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            rezervim.setRezervimId(generatedKeys.getInt(1));
        }

        // Update inventory if needed
        if (rezervim.getInventar() != null) {
            // Decrease inventory stock
            rezervim.getInventar().decreaseStock(1);
            inventariDAO.update(rezervim.getInventar());
        }
    }

    public void update(Rezervim rezervim) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE rezervim SET user_id=?, biznes_id=?, inventar_id=?, data=?, " +
                "numri_personave=?, shënime=?, status=?, updated_at=? WHERE rezervim_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, rezervim.getUser().getUserId());
        ps.setInt(2, rezervim.getBiznes().getBiznesId());

        if (rezervim.getInventar() != null) {
            ps.setInt(3, rezervim.getInventar().getInventarId());
        } else {
            ps.setNull(3, Types.INTEGER);
        }

        ps.setTimestamp(4, Timestamp.valueOf(rezervim.getData()));
        ps.setInt(5, rezervim.getNumriPersonave());
        ps.setString(6, rezervim.getShënime());
        ps.setString(7, rezervim.getStatus());
        ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
        ps.setInt(9, rezervim.getRezervimId());

        ps.executeUpdate();
    }

    public void updateStatus(int rezervimId, String status) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE rezervim SET status=?, updated_at=? WHERE rezervim_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, status);
        ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
        ps.setInt(3, rezervimId);

        ps.executeUpdate();
    }

    public void delete(int rezervimId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();

        // Get reservation first to restore inventory if needed
        Rezervim rezervim = findById(rezervimId);

        // Delete reservation
        String sql = "DELETE FROM rezervim WHERE rezervim_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, rezervimId);
        ps.executeUpdate();

        // Restore inventory if reservation had inventory item
        if (rezervim != null && rezervim.getInventar() != null) {
            rezervim.getInventar().increaseStock(1);
            inventariDAO.update(rezervim.getInventar());
        }
    }

    public int countReservations() throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM rezervim";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public int countReservationsByStatus(String status) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM rezervim WHERE status = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, status);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public List<Rezervim> findReservationsByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws Exception {
        List<Rezervim> rezervimet = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM rezervim WHERE data BETWEEN ? AND ? ORDER BY data";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setTimestamp(1, Timestamp.valueOf(startDate));
        ps.setTimestamp(2, Timestamp.valueOf(endDate));
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            rezervimet.add(mapResultSetToRezervim(rs));
        }
        return rezervimet;
    }

    private Rezervim mapResultSetToRezervim(ResultSet rs) throws Exception {
        Rezervim rezervim = new Rezervim();
        rezervim.setRezervimId(rs.getInt("rezervim_id"));

        // Load user
        int userId = rs.getInt("user_id");
        User user = userDAO.findById(userId);
        rezervim.setUser(user);

        // Load business
        int biznesId = rs.getInt("biznes_id");
        Biznes biznes = biznesDAO.findById(biznesId);
        rezervim.setBiznes(biznes);

        // Load inventory if exists
        int inventarId = rs.getInt("inventar_id");
        if (inventarId > 0) {
            Inventari inventar = inventariDAO.findById(inventarId);
            rezervim.setInventar(inventar);
        }

        rezervim.setData(rs.getTimestamp("data").toLocalDateTime());
        rezervim.setNumriPersonave(rs.getInt("numri_personave"));
        rezervim.setShënime(rs.getString("shënime"));
        rezervim.setStatus(rs.getString("status"));
        rezervim.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        rezervim.setUpdatedAt(rs.getTimestamp("updated_at") != null ?
                rs.getTimestamp("updated_at").toLocalDateTime() : null);

        return rezervim;
    }
}
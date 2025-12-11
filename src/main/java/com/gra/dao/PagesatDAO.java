package com.gra.dao;

import com.gra.db.DBConnection;
import com.gra.model.Pagesat;
import com.gra.model.Rezervim;
import com.gra.model.PagesatDetaje;
import com.gra.model.PagesatHistorik;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PagesatDAO {

    private RezervimDAO rezervimDAO;

    public PagesatDAO() {
        this.rezervimDAO = new RezervimDAO();
    }

    public Pagesat findById(int id) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM pagesat WHERE pagesa_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Pagesat pagesa = mapResultSetToPagesat(rs);

            // Load payment details
            pagesa.setDetaje(findPagesatDetajeByPagesaId(pagesa.getPagesaId()));

            // Load payment history
            pagesa.setHistoriku(findPagesatHistorikByPagesaId(pagesa.getPagesaId()));

            return pagesa;
        }
        return null;
    }

    public Pagesat findByTransactionId(String transactionId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM pagesat WHERE transaction_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, transactionId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return mapResultSetToPagesat(rs);
        }
        return null;
    }

    public List<Pagesat> findAll() throws Exception {
        List<Pagesat> pagesat = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM pagesat ORDER BY created_at DESC");

        while (rs.next()) {
            pagesat.add(mapResultSetToPagesat(rs));
        }
        return pagesat;
    }

    public List<Pagesat> findByReservationId(int reservationId) throws Exception {
        List<Pagesat> pagesat = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM pagesat WHERE rezervim_id = ? ORDER BY created_at DESC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, reservationId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            pagesat.add(mapResultSetToPagesat(rs));
        }
        return pagesat;
    }

    public List<Pagesat> findByStatus(String status) throws Exception {
        List<Pagesat> pagesat = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM pagesat WHERE status = ? ORDER BY created_at DESC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, status);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            pagesat.add(mapResultSetToPagesat(rs));
        }
        return pagesat;
    }

    public List<Pagesat> findByPaymentMethod(String method) throws Exception {
        List<Pagesat> pagesat = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM pagesat WHERE metoda = ? ORDER BY created_at DESC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, method);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            pagesat.add(mapResultSetToPagesat(rs));
        }
        return pagesat;
    }

    public List<Pagesat> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws Exception {
        List<Pagesat> pagesat = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM pagesat WHERE payment_date BETWEEN ? AND ? ORDER BY payment_date";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setTimestamp(1, Timestamp.valueOf(startDate));
        ps.setTimestamp(2, Timestamp.valueOf(endDate));
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            pagesat.add(mapResultSetToPagesat(rs));
        }
        return pagesat;
    }

    public void save(Pagesat pagesa) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO pagesat (rezervim_id, shuma, metoda, status, transaction_id, payment_date) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setInt(1, pagesa.getRezervim().getRezervimId());
        ps.setDouble(2, pagesa.getShuma());
        ps.setString(3, pagesa.getMetoda());
        ps.setString(4, pagesa.getStatus());
        ps.setString(5, pagesa.getTransactionId());

        if (pagesa.getPaymentDate() != null) {
            ps.setTimestamp(6, Timestamp.valueOf(pagesa.getPaymentDate()));
        } else {
            ps.setNull(6, Types.TIMESTAMP);
        }

        ps.executeUpdate();

        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            int pagesaId = generatedKeys.getInt(1);
            pagesa.setPagesaId(pagesaId);

            // Save payment details if exists
            if (pagesa.getDetaje() != null) {
                savePagesatDetaje(pagesa.getDetaje(), pagesaId);
            }

            // Save initial history record
            if (pagesa.getHistoriku() != null && !pagesa.getHistoriku().isEmpty()) {
                for (PagesatHistorik historik : pagesa.getHistoriku()) {
                    savePagesatHistorik(historik, pagesaId);
                }
            }
        }
    }

    public void update(Pagesat pagesa) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE pagesat SET rezervim_id=?, shuma=?, metoda=?, status=?, " +
                "transaction_id=?, payment_date=? WHERE pagesa_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, pagesa.getRezervim().getRezervimId());
        ps.setDouble(2, pagesa.getShuma());
        ps.setString(3, pagesa.getMetoda());
        ps.setString(4, pagesa.getStatus());
        ps.setString(5, pagesa.getTransactionId());

        if (pagesa.getPaymentDate() != null) {
            ps.setTimestamp(6, Timestamp.valueOf(pagesa.getPaymentDate()));
        } else {
            ps.setNull(6, Types.TIMESTAMP);
        }

        ps.setInt(7, pagesa.getPagesaId());

        ps.executeUpdate();

        // Update payment details if exists
        if (pagesa.getDetaje() != null) {
            updatePagesatDetaje(pagesa.getDetaje());
        }
    }

    public void updateStatus(int pagesaId, String status) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE pagesat SET status=? WHERE pagesa_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, status);
        ps.setInt(2, pagesaId);

        ps.executeUpdate();

        // Add to history
        addHistoryRecord(pagesaId, status, "Statusi u ndryshua në: " + status);
    }

    public void completePayment(int pagesaId, String transactionId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE pagesat SET status='COMPLETED', transaction_id=?, payment_date=NOW() WHERE pagesa_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, transactionId);
        ps.setInt(2, pagesaId);

        ps.executeUpdate();

        // Add to history
        addHistoryRecord(pagesaId, "COMPLETED", "Pagesa u përfundua me transaksion: " + transactionId);
    }

    public void refundPayment(int pagesaId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE pagesat SET status='REFUNDED' WHERE pagesa_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, pagesaId);
        ps.executeUpdate();

        // Add to history
        addHistoryRecord(pagesaId, "REFUNDED", "Pagesa u rimbursua");
    }

    public void delete(int pagesaId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();

        // Delete payment history first
        deletePagesatHistorik(pagesaId);

        // Delete payment details
        deletePagesatDetaje(pagesaId);

        // Then delete payment
        String sql = "DELETE FROM pagesat WHERE pagesa_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, pagesaId);
        ps.executeUpdate();
    }

    public double getTotalRevenue() throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT SUM(shuma) as total FROM pagesat WHERE status = 'COMPLETED'";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getDouble("total");
        }
        return 0.0;
    }

    public double getTotalRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT SUM(shuma) as total FROM pagesat WHERE status = 'COMPLETED' " +
                "AND payment_date BETWEEN ? AND ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setTimestamp(1, Timestamp.valueOf(startDate));
        ps.setTimestamp(2, Timestamp.valueOf(endDate));
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getDouble("total");
        }
        return 0.0;
    }

    public int countPayments() throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM pagesat";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public int countPaymentsByStatus(String status) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM pagesat WHERE status = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, status);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    // Private helper methods
    private PagesatDetaje findPagesatDetajeByPagesaId(int pagesaId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM pagesat_detaje WHERE pagesa_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, pagesaId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            PagesatDetaje detaje = new PagesatDetaje();
            detaje.setDetajeId(rs.getInt("detaje_id"));
            detaje.setReference(rs.getString("reference"));
            detaje.setCardLastFour(rs.getString("card_last_four"));
            detaje.setCardType(rs.getString("card_type"));
            detaje.setPaymentGateway(rs.getString("payment_gateway"));
            detaje.setGatewayResponse(rs.getString("gateway_response"));
            detaje.setIpAddress(rs.getString("ip_address"));
            detaje.setUserAgent(rs.getString("user_agent"));
            detaje.setCreatedAt(rs.getTimestamp("created_at") != null ?
                    rs.getTimestamp("created_at").toLocalDateTime() : null);

            return detaje;
        }
        return null;
    }

    private List<PagesatHistorik> findPagesatHistorikByPagesaId(int pagesaId) throws Exception {
        List<PagesatHistorik> historiku = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM pagesat_historik WHERE pagesa_id = ? ORDER BY data";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, pagesaId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            PagesatHistorik historik = new PagesatHistorik();
            historik.setHistorikId(rs.getInt("historik_id"));
            historik.setStatus(rs.getString("status"));
            historik.setMesazh(rs.getString("mesazh"));
            historik.setData(rs.getTimestamp("data") != null ?
                    rs.getTimestamp("data").toLocalDateTime() : null);

            historiku.add(historik);
        }
        return historiku;
    }

    private void savePagesatDetaje(PagesatDetaje detaje, int pagesaId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO pagesat_detaje (pagesa_id, reference, card_last_four, card_type, " +
                "payment_gateway, gateway_response, ip_address, user_agent) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setInt(1, pagesaId);
        ps.setString(2, detaje.getReference());
        ps.setString(3, detaje.getCardLastFour());
        ps.setString(4, detaje.getCardType());
        ps.setString(5, detaje.getPaymentGateway());
        ps.setString(6, detaje.getGatewayResponse());
        ps.setString(7, detaje.getIpAddress());
        ps.setString(8, detaje.getUserAgent());

        ps.executeUpdate();

        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            detaje.setDetajeId(generatedKeys.getInt(1));
        }
    }

    private void updatePagesatDetaje(PagesatDetaje detaje) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE pagesat_detaje SET reference=?, card_last_four=?, card_type=?, " +
                "payment_gateway=?, gateway_response=?, ip_address=?, user_agent=? " +
                "WHERE detaje_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, detaje.getReference());
        ps.setString(2, detaje.getCardLastFour());
        ps.setString(3, detaje.getCardType());
        ps.setString(4, detaje.getPaymentGateway());
        ps.setString(5, detaje.getGatewayResponse());
        ps.setString(6, detaje.getIpAddress());
        ps.setString(7, detaje.getUserAgent());
        ps.setInt(8, detaje.getDetajeId());

        ps.executeUpdate();
    }

    private void savePagesatHistorik(PagesatHistorik historik, int pagesaId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO pagesat_historik (pagesa_id, status, mesazh) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setInt(1, pagesaId);
        ps.setString(2, historik.getStatus());
        ps.setString(3, historik.getMesazh());

        ps.executeUpdate();

        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            historik.setHistorikId(generatedKeys.getInt(1));
        }
    }

    private void addHistoryRecord(int pagesaId, String status, String message) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO pagesat_historik (pagesa_id, status, mesazh) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, pagesaId);
        ps.setString(2, status);
        ps.setString(3, message);

        ps.executeUpdate();
    }

    private void deletePagesatDetaje(int pagesaId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM pagesat_detaje WHERE pagesa_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, pagesaId);
        ps.executeUpdate();
    }

    private void deletePagesatHistorik(int pagesaId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM pagesat_historik WHERE pagesa_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, pagesaId);
        ps.executeUpdate();
    }

    private Pagesat mapResultSetToPagesat(ResultSet rs) throws Exception {
        Pagesat pagesa = new Pagesat();
        pagesa.setPagesaId(rs.getInt("pagesa_id"));

        // Load reservation
        int rezervimId = rs.getInt("rezervim_id");
        Rezervim rezervim = rezervimDAO.findById(rezervimId);
        pagesa.setRezervim(rezervim);

        pagesa.setShuma(rs.getDouble("shuma"));
        pagesa.setMetoda(rs.getString("metoda"));
        pagesa.setStatus(rs.getString("status"));
        pagesa.setTransactionId(rs.getString("transaction_id"));
        pagesa.setPaymentDate(rs.getTimestamp("payment_date") != null ?
                rs.getTimestamp("payment_date").toLocalDateTime() : null);
        pagesa.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);

        return pagesa;
    }
}
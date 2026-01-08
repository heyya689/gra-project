package com.gra.dao;

import com.gra.db.DBConnection;
import com.gra.model.PagesatDetaje;
import java.sql.*;

public class PagesatDetajeDAO {

    public void save(PagesatDetaje detaje) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        // Përdorim fushat që ekzistojnë në Modelin tënd
        String sql = "INSERT INTO pagesat_detaje (pagesa_id, reference, card_last_four, " +
                "card_type, payment_gateway, gateway_response, ip_address, user_agent) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = conn.prepareStatement(sql);

        // Kujdes: detaje.getPagesa().getPagesaId() sepse në model ke objektin Pagesat
        ps.setInt(1, detaje.getPagesa().getPagesaId());
        ps.setString(2, detaje.getReference());
        ps.setString(3, detaje.getCardLastFour());
        ps.setString(4, detaje.getCardType());
        ps.setString(5, detaje.getPaymentGateway());
        ps.setString(6, detaje.getGatewayResponse());
        ps.setString(7, detaje.getIpAddress());
        ps.setString(8, detaje.getUserAgent());

        ps.executeUpdate();
    }

    public PagesatDetaje findByPagesaId(int pagesaId) throws Exception {
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

            if (rs.getTimestamp("created_at") != null) {
                detaje.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            }

            return detaje;
        }
        return null;
    }
}
package com.gra.dao;

import com.gra.db.DBConnection;
import com.gra.model.PagesatHistorik;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PagesatHistorikDAO {

    /**
     * Ruan një rekord të ri në historikun e pagesave.
     */
    public void save(PagesatHistorik historik) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        // Sigurohu që emrat e kolonave në SQL përputhen me modelin (status, mesazh, data)
        String sql = "INSERT INTO pagesat_historik (pagesa_id, status, mesazh, data) VALUES (?, ?, ?, ?)";

        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        // Marrim ID-në nga objekti Pagesat brenda historikut
        if (historik.getPagesa() != null) {
            ps.setInt(1, historik.getPagesa().getPagesaId());
        } else {
            throw new Exception("Historiku duhet të jetë i lidhur me një objekt Pagesat!");
        }

        ps.setString(2, historik.getStatus());
        ps.setString(3, historik.getMesazh());
        ps.setTimestamp(4, Timestamp.valueOf(historik.getData()));

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            historik.setHistorikId(rs.getInt(1));
        }
    }

    /**
     * Gjen të gjithë historikun e lëvizjeve për një pagesë specifike.
     */
    public List<PagesatHistorik> findByPagesaId(int pagesaId) throws Exception {
        List<PagesatHistorik> lista = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM pagesat_historik WHERE pagesa_id = ? ORDER BY data DESC";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, pagesaId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            PagesatHistorik h = new PagesatHistorik();
            h.setHistorikId(rs.getInt("historik_id"));
            h.setStatus(rs.getString("status"));
            h.setMesazh(rs.getString("mesazh"));

            if (rs.getTimestamp("data") != null) {
                h.setData(rs.getTimestamp("data").toLocalDateTime());
            }

            lista.add(h);
        }
        return lista;
    }

    /**
     * Fshin historikun e një pagese (përdoret kryesisht gjatë fshirjes së të dhënave testuese).
     */
    public void deleteByPagesaId(int pagesaId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM pagesat_historik WHERE pagesa_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, pagesaId);
        ps.executeUpdate();
    }
}
package com.gra.dao;

import com.gra.db.DBConnection;
import com.gra.model.BiznesImazhe;
import com.gra.model.Biznes;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BiznesImazheDAO {

    private BiznesDAO biznesDAO;

    public BiznesImazheDAO() {
        this.biznesDAO = new BiznesDAO();
    }

    public BiznesImazhe findById(int id) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM biznes_imazhe WHERE imazh_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return mapResultSetToBiznesImazhe(rs);
        }
        return null;
    }

    public List<BiznesImazhe> findAll() throws Exception {
        List<BiznesImazhe> imazhet = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM biznes_imazhe ORDER BY biznes_id, renditja");

        while (rs.next()) {
            imazhet.add(mapResultSetToBiznesImazhe(rs));
        }
        return imazhet;
    }

    public List<BiznesImazhe> findByBusinessId(int businessId) throws Exception {
        List<BiznesImazhe> imazhet = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM biznes_imazhe WHERE biznes_id = ? ORDER BY is_primary DESC, renditja";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, businessId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            imazhet.add(mapResultSetToBiznesImazhe(rs));
        }
        return imazhet;
    }

    public BiznesImazhe findPrimaryImageByBusinessId(int businessId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM biznes_imazhe WHERE biznes_id = ? AND is_primary = TRUE";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, businessId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return mapResultSetToBiznesImazhe(rs);
        }
        return null;
    }

    public List<BiznesImazhe> findImagesByDescription(String keyword) throws Exception {
        List<BiznesImazhe> imazhet = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM biznes_imazhe WHERE pershkrim LIKE ? ORDER BY biznes_id";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + keyword + "%");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            imazhet.add(mapResultSetToBiznesImazhe(rs));
        }
        return imazhet;
    }

    public void save(BiznesImazhe imazh) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO biznes_imazhe (biznes_id, url, pershkrim, is_primary, renditja) " +
                "VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setInt(1, imazh.getBiznes().getBiznesId());
        ps.setString(2, imazh.getUrl());
        ps.setString(3, imazh.getPershkrim());
        ps.setBoolean(4, imazh.isPrimary());
        ps.setInt(5, imazh.getRenditja());

        ps.executeUpdate();

        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            imazh.setImazhId(generatedKeys.getInt(1));
        }

        // If this is set as primary, unset other primaries for this business
        if (imazh.isPrimary()) {
            unsetOtherPrimaries(imazh.getBiznes().getBiznesId(), imazh.getImazhId());
        }
    }

    public void update(BiznesImazhe imazh) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE biznes_imazhe SET biznes_id=?, url=?, pershkrim=?, is_primary=?, " +
                "renditja=? WHERE imazh_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, imazh.getBiznes().getBiznesId());
        ps.setString(2, imazh.getUrl());
        ps.setString(3, imazh.getPershkrim());
        ps.setBoolean(4, imazh.isPrimary());
        ps.setInt(5, imazh.getRenditja());
        ps.setInt(6, imazh.getImazhId());

        ps.executeUpdate();

        // If this is set as primary, unset other primaries for this business
        if (imazh.isPrimary()) {
            unsetOtherPrimaries(imazh.getBiznes().getBiznesId(), imazh.getImazhId());
        }
    }

    public void delete(int imazhId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM biznes_imazhe WHERE imazh_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, imazhId);
        ps.executeUpdate();
    }

    public void deleteByBusinessId(int businessId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM biznes_imazhe WHERE biznes_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, businessId);
        ps.executeUpdate();
    }

    public void setAsPrimary(int imazhId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();

        // First get the business ID from this image
        String getBiznesSql = "SELECT biznes_id FROM biznes_imazhe WHERE imazh_id = ?";
        PreparedStatement getPs = conn.prepareStatement(getBiznesSql);
        getPs.setInt(1, imazhId);
        ResultSet rs = getPs.executeQuery();

        if (rs.next()) {
            int businessId = rs.getInt("biznes_id");

            // Unset other primaries for this business
            String unsetSql = "UPDATE biznes_imazhe SET is_primary = FALSE WHERE biznes_id = ?";
            PreparedStatement unsetPs = conn.prepareStatement(unsetSql);
            unsetPs.setInt(1, businessId);
            unsetPs.executeUpdate();

            // Set this image as primary
            String setSql = "UPDATE biznes_imazhe SET is_primary = TRUE WHERE imazh_id = ?";
            PreparedStatement setPs = conn.prepareStatement(setSql);
            setPs.setInt(1, imazhId);
            setPs.executeUpdate();
        }
    }

    public void updateImageOrder(int imazhId, int newOrder) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE biznes_imazhe SET renditja = ? WHERE imazh_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, newOrder);
        ps.setInt(2, imazhId);
        ps.executeUpdate();
    }

    public int countImages() throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM biznes_imazhe";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public int countImagesByBusinessId(int businessId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM biznes_imazhe WHERE biznes_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, businessId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public List<BiznesImazhe> findImagesWithoutDescription() throws Exception {
        List<BiznesImazhe> imazhet = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM biznes_imazhe WHERE pershkrim IS NULL OR pershkrim = '' ORDER BY biznes_id";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            imazhet.add(mapResultSetToBiznesImazhe(rs));
        }
        return imazhet;
    }

    private void unsetOtherPrimaries(int businessId, int currentImageId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE biznes_imazhe SET is_primary = FALSE WHERE biznes_id = ? AND imazh_id != ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, businessId);
        ps.setInt(2, currentImageId);
        ps.executeUpdate();
    }

    private BiznesImazhe mapResultSetToBiznesImazhe(ResultSet rs) throws Exception {
        BiznesImazhe imazh = new BiznesImazhe();
        imazh.setImazhId(rs.getInt("imazh_id"));

        // Load business
        int biznesId = rs.getInt("biznes_id");
        Biznes biznes = biznesDAO.findById(biznesId);
        imazh.setBiznes(biznes);

        imazh.setUrl(rs.getString("url"));
        imazh.setPershkrim(rs.getString("pershkrim"));
        imazh.setPrimary(rs.getBoolean("is_primary"));
        imazh.setRenditja(rs.getInt("renditja"));
        imazh.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);

        return imazh;
    }
}
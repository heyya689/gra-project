package com.gra.dao;

import com.gra.db.DBConnection;
import com.gra.model.Lokacion;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LokacionDAO {

    public Lokacion findById(int id) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM lokacion WHERE lokacion_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return mapResultSetToLokacion(rs);
        }
        return null;
    }

    public List<Lokacion> findAll() throws Exception {
        List<Lokacion> lokacione = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM lokacion ORDER BY qyteti, adresa");

        while (rs.next()) {
            lokacione.add(mapResultSetToLokacion(rs));
        }
        return lokacione;
    }

    public List<Lokacion> findByCity(String city) throws Exception {
        List<Lokacion> lokacione = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM lokacion WHERE qyteti = ? ORDER BY adresa";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, city);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            lokacione.add(mapResultSetToLokacion(rs));
        }
        return lokacione;
    }

    public List<Lokacion> findByAddress(String address) throws Exception {
        List<Lokacion> lokacione = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM lokacion WHERE adresa LIKE ? OR rruga LIKE ? ORDER BY qyteti";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + address + "%");
        ps.setString(2, "%" + address + "%");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            lokacione.add(mapResultSetToLokacion(rs));
        }
        return lokacione;
    }

    public List<Lokacion> findByZipCode(String zipCode) throws Exception {
        List<Lokacion> lokacione = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM lokacion WHERE zip_code = ? ORDER BY qyteti, adresa";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, zipCode);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            lokacione.add(mapResultSetToLokacion(rs));
        }
        return lokacione;
    }

    public List<Lokacion> findNearbyLocations(double latitude, double longitude, double radiusKm) throws Exception {
        List<Lokacion> lokacione = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();

        // Using Haversine formula to find locations within radius
        String sql = "SELECT *, " +
                "(6371 * acos(cos(radians(?)) * cos(radians(latitude)) * " +
                "cos(radians(longitude) - radians(?)) + sin(radians(?)) * sin(radians(latitude)))) " +
                "AS distance FROM lokacion WHERE latitude IS NOT NULL AND longitude IS NOT NULL " +
                "HAVING distance < ? ORDER BY distance";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setDouble(1, latitude);
        ps.setDouble(2, longitude);
        ps.setDouble(3, latitude);
        ps.setDouble(4, radiusKm);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Lokacion lokacion = mapResultSetToLokacion(rs);
            lokacione.add(lokacion);
        }
        return lokacione;
    }

    public void save(Lokacion lokacion) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO lokacion (qyteti, adresa, rruga, numri, zip_code, latitude, longitude) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setString(1, lokacion.getQyteti());
        ps.setString(2, lokacion.getAdresa());
        ps.setString(3, lokacion.getRruga());
        ps.setString(4, lokacion.getNumri());
        ps.setString(5, lokacion.getZipCode());

        if (lokacion.getLatitude() != null) {
            ps.setDouble(6, lokacion.getLatitude());
        } else {
            ps.setNull(6, Types.DOUBLE);
        }

        if (lokacion.getLongitude() != null) {
            ps.setDouble(7, lokacion.getLongitude());
        } else {
            ps.setNull(7, Types.DOUBLE);
        }

        ps.executeUpdate();

        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            lokacion.setLokacionId(generatedKeys.getInt(1));
        }
    }

    public void update(Lokacion lokacion) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE lokacion SET qyteti=?, adresa=?, rruga=?, numri=?, zip_code=?, " +
                "latitude=?, longitude=? WHERE lokacion_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, lokacion.getQyteti());
        ps.setString(2, lokacion.getAdresa());
        ps.setString(3, lokacion.getRruga());
        ps.setString(4, lokacion.getNumri());
        ps.setString(5, lokacion.getZipCode());

        if (lokacion.getLatitude() != null) {
            ps.setDouble(6, lokacion.getLatitude());
        } else {
            ps.setNull(6, Types.DOUBLE);
        }

        if (lokacion.getLongitude() != null) {
            ps.setDouble(7, lokacion.getLongitude());
        } else {
            ps.setNull(7, Types.DOUBLE);
        }

        ps.setInt(8, lokacion.getLokacionId());

        ps.executeUpdate();
    }

    public void delete(int lokacionId) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM lokacion WHERE lokacion_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, lokacionId);
        ps.executeUpdate();
    }

    public int countLocations() throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM lokacion";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public int countLocationsByCity(String city) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM lokacion WHERE qyteti = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, city);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public List<String> getAllCities() throws Exception {
        List<String> cities = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT DISTINCT qyteti FROM lokacion ORDER BY qyteti";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            cities.add(rs.getString("qyteti"));
        }
        return cities;
    }

    public List<Lokacion> findLocationsWithoutCoordinates() throws Exception {
        List<Lokacion> lokacione = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM lokacion WHERE latitude IS NULL OR longitude IS NULL";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            lokacione.add(mapResultSetToLokacion(rs));
        }
        return lokacione;
    }

    public void updateCoordinates(int lokacionId, double latitude, double longitude) throws Exception {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE lokacion SET latitude=?, longitude=? WHERE lokacion_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setDouble(1, latitude);
        ps.setDouble(2, longitude);
        ps.setInt(3, lokacionId);

        ps.executeUpdate();
    }

    private Lokacion mapResultSetToLokacion(ResultSet rs) throws SQLException {
        Lokacion lokacion = new Lokacion();
        lokacion.setLokacionId(rs.getInt("lokacion_id"));
        lokacion.setQyteti(rs.getString("qyteti"));
        lokacion.setAdresa(rs.getString("adresa"));
        lokacion.setRruga(rs.getString("rruga"));
        lokacion.setNumri(rs.getString("numri"));
        lokacion.setZipCode(rs.getString("zip_code"));
        lokacion.setLatitude(rs.getDouble("latitude"));
        lokacion.setLongitude(rs.getDouble("longitude"));
        lokacion.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);

        return lokacion;
    }
}
package com.gra.services;

import com.gra.dao.LokacionDAO;
import com.gra.model.Lokacion;

import java.util.List;

public class LokacionSolver {

    private LokacionDAO lokacionDAO;

    public LokacionSolver() {
        this.lokacionDAO = new LokacionDAO();
    }

    public Lokacion getLokacionById(int lokacionId) throws Exception {
        return lokacionDAO.findById(lokacionId);
    }

    public List<Lokacion> getAllLokacionet() throws Exception {
        return lokacionDAO.findAll();
    }

    public List<Lokacion> getLokacionetByCity(String qyteti) throws Exception {
        return lokacionDAO.findByCity(qyteti);
    }

    public List<Lokacion> getLokacionetByAddress(String address) throws Exception {
        return lokacionDAO.findByAddress(address);
    }

    public List<Lokacion> getLokacionetByZipCode(String zipCode) throws Exception {
        return lokacionDAO.findByZipCode(zipCode);
    }

    public List<Lokacion> getLokacionePaKoordinata() throws Exception {
        return lokacionDAO.findLocationsWithoutCoordinates();
    }

    public List<String> getTeGjithaQytetet() throws Exception {
        return lokacionDAO.getAllCities();
    }

    public List<Lokacion> getLokacioneAfer(double latitude, double longitude, double radiusKm) throws Exception {
        return lokacionDAO.findNearbyLocations(latitude, longitude, radiusKm);
    }

    public void shtoLokacion(Lokacion lokacion) throws Exception {
        lokacionDAO.save(lokacion);
    }

    public void perditesoLokacion(Lokacion lokacion) throws Exception {
        lokacionDAO.update(lokacion);
    }

    public void fshiLokacion(int lokacionId) throws Exception {
        lokacionDAO.delete(lokacionId);
    }

    public void perditesoKoordinata(int lokacionId, double latitude, double longitude) throws Exception {
        lokacionDAO.updateCoordinates(lokacionId, latitude, longitude);
    }

    public int numerTotalLokacionesh() throws Exception {
        return lokacionDAO.countLocations();
    }

    public int numerLokacioneshSipasQytetit(String qyteti) throws Exception {
        return lokacionDAO.countLocationsByCity(qyteti);
    }
}

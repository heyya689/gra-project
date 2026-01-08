package com.gra.services;

import com.gra.dao.BiznesDAO;
import com.gra.model.Biznes;

import java.util.List;

public class BiznesSolver {

    private BiznesDAO biznesDAO;

    public BiznesSolver() {
        this.biznesDAO = new BiznesDAO();
    }

    public Biznes registerBiznes(Biznes biznes) throws Exception {
        if (biznesDAO.niptExists(biznes.getNipt())) {
            throw new Exception("Business with this NIPT exists");
        }
        biznesDAO.save(biznes);
        return biznes;
    }

    public Biznes getBusinessesById(int id) throws Exception {
        return biznesDAO.findById(id);
    }

    public Biznes getBusinessesByNipt(String nipt) throws Exception {
        return biznesDAO.findByNipt(nipt);
    }

    public List<Biznes> getAllBusinesses() throws Exception {
        return biznesDAO.findAll();
    }

    public List<Biznes> getBusinessesByCategory(String category) throws Exception {
        return biznesDAO.findByCategory(category);
    }

    public List<Biznes> getBusinessesByCity(String city) throws Exception {
        return biznesDAO.findByCity(city);
    }

    public List<Biznes> searchBusinessesByName(String name) throws Exception {
        return biznesDAO.searchByName(name);
    }

    public void updateBiznes(Biznes biznes) throws Exception {
        biznesDAO.update(biznes);
    }

    public void deleteBiznes(int biznesId) throws Exception {
        biznesDAO.delete(biznesId);
    }

    public int countBusinesses() throws Exception {
        return biznesDAO.countBusinesses();
    }
}

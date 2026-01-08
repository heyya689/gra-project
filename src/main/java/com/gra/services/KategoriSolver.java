package com.gra.services;

import com.gra.dao.KategoriDAO;
import com.gra.model.Kategori;

import java.util.List;

public class KategoriSolver {

    private KategoriDAO kategoriDAO;

    public KategoriSolver() {
        this.kategoriDAO = new KategoriDAO();
    }

    public Kategori getKategoriById(int kategoriId) throws Exception {
        return kategoriDAO.findById(kategoriId);
    }

    public Kategori getKategoriByEmri(String emri) throws Exception {
        return kategoriDAO.findByEmri(emri);
    }

    public List<Kategori> getAllKategorite() throws Exception {
        return kategoriDAO.findAll();
    }

    public List<Kategori> getKategoriteMeBiznese() throws Exception {
        return kategoriDAO.findCategoriesWithBusinesses();
    }

    public List<Kategori> searchKategoriByEmri(String emri) throws Exception {
        return kategoriDAO.searchByEmri(emri);
    }

    public void krijoKategori(Kategori kategori) throws Exception {
        kategoriDAO.save(kategori);
    }

    public void perditesoKategori(Kategori kategori) throws Exception {
        kategoriDAO.update(kategori);
    }

    public void fshiKategori(int kategoriId) throws Exception {
        kategoriDAO.delete(kategoriId);
    }

    public void shtoBiznesNeKategori(int kategoriId, int biznesId) throws Exception {
        kategoriDAO.addBusinessToCategory(kategoriId, biznesId);
    }

    public void hiqBiznesNgaKategori(int kategoriId, int biznesId) throws Exception {
        kategoriDAO.removeBusinessFromCategory(kategoriId, biznesId);
    }

    public int numerKategori() throws Exception {
        return kategoriDAO.countCategories();
    }

    public int numerBizneseshNeKategori(int kategoriId) throws Exception {
        return kategoriDAO.countBusinessesInCategory(kategoriId);
    }
}


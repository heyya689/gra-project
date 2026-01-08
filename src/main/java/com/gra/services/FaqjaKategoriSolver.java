package com.gra.services;

import com.gra.dao.FaqjaKategoriDAO;
import com.gra.model.FaqjaKategori;

import java.util.List;

public class FaqjaKategoriSolver {

    private FaqjaKategoriDAO kategoriDAO;

    public FaqjaKategoriSolver() {
        this.kategoriDAO = new FaqjaKategoriDAO();
    }

    public FaqjaKategori createCategory(FaqjaKategori kategori) throws Exception {
        kategoriDAO.save(kategori);
        return kategori;
    }

    public FaqjaKategori getCategoryById(int kategoriId) throws Exception {
        return kategoriDAO.findById(kategoriId);
    }

    public FaqjaKategori getCategoryByName(String emri) throws Exception {
        return kategoriDAO.findByEmri(emri);
    }

    public List<FaqjaKategori> getAllCategories() throws Exception {
        return kategoriDAO.findAll();
    }

    public List<FaqjaKategori> getCategoriesWithFAQs() throws Exception {
        return kategoriDAO.findCategoriesWithFAQs();
    }

    public List<FaqjaKategori> searchCategoriesByName(String keyword) throws Exception {
        return kategoriDAO.searchByEmri(keyword);
    }

    public void updateCategory(FaqjaKategori kategori) throws Exception {
        kategoriDAO.update(kategori);
    }

    public void updateCategoryOrder(int kategoriId, int newOrder) throws Exception {
        kategoriDAO.updateOrder(kategoriId, newOrder);
    }

    public void addFAQToCategory(int kategoriId, int faqId) throws Exception {
        kategoriDAO.addFAQToCategory(kategoriId, faqId);
    }

    public void removeFAQFromCategory(int kategoriId, int faqId) throws Exception {
        kategoriDAO.removeFAQFromCategory(kategoriId, faqId);
    }

    public void deleteCategory(int kategoriId) throws Exception {
        kategoriDAO.delete(kategoriId);
    }

    public int countCategories() throws Exception {
        return kategoriDAO.countCategories();
    }

    public int countFAQsInCategory(int kategoriId) throws Exception {
        return kategoriDAO.countFAQsInCategory(kategoriId);
    }
}

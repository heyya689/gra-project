package com.gra.services;

import com.gra.dao.InventariDAO;
import com.gra.model.Inventari;

import java.util.List;

public class InventariSolver {

    private InventariDAO inventariDAO;

    public InventariSolver() {
        this.inventariDAO = new InventariDAO();
    }

    public Inventari getInventarById(int inventarId) throws Exception {
        return inventariDAO.findById(inventarId);
    }

    public List<Inventari> getAllInventar() throws Exception {
        return inventariDAO.findAll();
    }

    public List<Inventari> getInventarByBiznes(int biznesId) throws Exception {
        return inventariDAO.findByBusinessId(biznesId);
    }

    public List<Inventari> getInventarByKategori(String kategori) throws Exception {
        return inventariDAO.findByCategory(kategori);
    }

    public List<Inventari> getAvailableInventar() throws Exception {
        return inventariDAO.findAvailableItems();
    }

    public List<Inventari> searchInventarByName(String name) throws Exception {
        return inventariDAO.searchByName(name);
    }

    public List<Inventari> getLowStockItems(int threshold) throws Exception {
        return inventariDAO.findLowStockItems(threshold);
    }

    public Inventari createInventar(Inventari inventari) throws Exception {
        inventariDAO.save(inventari);
        return inventari;
    }

    public void updateInventar(Inventari inventari) throws Exception {
        inventariDAO.update(inventari);
    }

    public void updateStock(int inventarId, int newQuantity) throws Exception {
        inventariDAO.updateStock(inventarId, newQuantity);
    }

    public void updatePrice(int inventarId, double newPrice) throws Exception {
        inventariDAO.updatePrice(inventarId, newPrice);
    }

    public void activateInventar(int inventarId) throws Exception {
        inventariDAO.activateItem(inventarId);
    }

    public void deactivateInventar(int inventarId) throws Exception {
        inventariDAO.deactivateItem(inventarId);
    }

    public void deleteInventar(int inventarId) throws Exception {
        inventariDAO.delete(inventarId);
    }

    public int getTotalInventarCount() throws Exception {
        return inventariDAO.countInventoryItems();
    }

    public double getTotalInventoryValue(int biznesId) throws Exception {
        return inventariDAO.getTotalInventoryValue(biznesId);
    }
}

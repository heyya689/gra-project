package com.gra.model;

import java.time.LocalDateTime;

public class Inventari {
    private int inventarId;
    private Biznes biznes;
    private String emerProdukt;
    private String pershkrim;
    private int sasi;
    private double cmimi;
    private String njesia;
    private String kategoria;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Inventari() {
        this.isActive = true;
    }

    public Inventari(int inventarId, String emerProdukt, int sasi, double cmimi) {
        this();
        this.inventarId = inventarId;
        this.emerProdukt = emerProdukt;
        this.sasi = sasi;
        this.cmimi = cmimi;
    }

    public void updateStock(int sasia) {
        this.sasi = sasia;
        this.updatedAt = LocalDateTime.now();
    }

    public void updatePrice(double cmimi) {
        if (cmimi >= 0) {
            this.cmimi = cmimi;
            this.updatedAt = LocalDateTime.now();
        }
    }

    public boolean isAvailable() {
        return isActive && sasi > 0;
    }

    public void decreaseStock(int quantity) {
        if (quantity > 0 && this.sasi >= quantity) {
            this.sasi -= quantity;
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void increaseStock(int quantity) {
        if (quantity > 0) {
            this.sasi += quantity;
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void activate() {
        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
    }

    public double getTotalValue() {
        return sasi * cmimi;
    }

    // Getters and Setters
    public int getInventarId() { return inventarId; }
    public void setInventarId(int inventarId) { this.inventarId = inventarId; }

    public Biznes getBiznes() { return biznes; }
    public void setBiznes(Biznes biznes) { this.biznes = biznes; }

    public String getEmerProdukt() { return emerProdukt; }
    public void setEmerProdukt(String emerProdukt) { this.emerProdukt = emerProdukt; }

    public String getPershkrim() { return pershkrim; }
    public void setPershkrim(String pershkrim) { this.pershkrim = pershkrim; }

    public int getSasi() { return sasi; }
    public void setSasi(int sasi) {
        this.sasi = sasi;
        this.updatedAt = LocalDateTime.now();
    }

    public double getCmimi() { return cmimi; }
    public void setCmimi(double cmimi) {
        this.cmimi = cmimi;
        this.updatedAt = LocalDateTime.now();
    }

    public String getNjesia() { return njesia; }
    public void setNjesia(String njesia) { this.njesia = njesia; }

    public String getKategoria() { return kategoria; }
    public void setKategoria(String kategoria) { this.kategoria = kategoria; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) {
        this.isActive = active;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
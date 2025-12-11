package com.gra.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Kategori {
    private int kategoriId;
    private String emri;
    private String ikona;
    private String pershkrim;
    private LocalDateTime createdAt;
    private List<Biznes> bizneset;

    public Kategori() {
        this.bizneset = new ArrayList<>();
    }

    public Kategori(int kategoriId, String emri, String ikona) {
        this();
        this.kategoriId = kategoriId;
        this.emri = emri;
        this.ikona = ikona;
    }

    public void addBiznes(Biznes biznes) {
        if (!bizneset.contains(biznes)) {
            bizneset.add(biznes);
            biznes.addKategori(this);
        }
    }

    public void removeBiznes(Biznes biznes) {
        bizneset.remove(biznes);
        biznes.removeKategori(this);
    }

    public int getBiznesCount() {
        return bizneset.size();
    }

    public String getDisplayName() {
        if (ikona != null && !ikona.isEmpty()) {
            return ikona + " " + emri;
        }
        return emri;
    }

    // Getters and Setters
    public int getKategoriId() { return kategoriId; }
    public void setKategoriId(int kategoriId) { this.kategoriId = kategoriId; }

    public String getEmri() { return emri; }
    public void setEmri(String emri) { this.emri = emri; }

    public String getIkona() { return ikona; }
    public void setIkona(String ikona) { this.ikona = ikona; }

    public String getPershkrim() { return pershkrim; }
    public void setPershkrim(String pershkrim) { this.pershkrim = pershkrim; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<Biznes> getBizneset() { return bizneset; }
    public void setBizneset(List<Biznes> bizneset) { this.bizneset = bizneset; }
}
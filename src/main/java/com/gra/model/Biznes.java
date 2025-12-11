package com.gra.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Biznes {
    private int biznesId;
    private String emri;
    private String pershkrim;
    private String kategori;
    private String nipt;
    private String license;
    private String telefon;
    private String email;
    private String website;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Lokacion lokacion;
    private List<Inventari> inventari;
    private List<Rezervim> rezervimet;
    private List<Vleresim> vleresimet;
    private List<BiznesImazhe> imazhet;
    private List<Kategori> kategorite;

    public Biznes() {
        this.inventari = new ArrayList<>();
        this.rezervimet = new ArrayList<>();
        this.vleresimet = new ArrayList<>();
        this.imazhet = new ArrayList<>();
        this.kategorite = new ArrayList<>();
    }

    public Biznes(int biznesId, String emri, String nipt) {
        this();
        this.biznesId = biznesId;
        this.emri = emri;
        this.nipt = nipt;
    }

    public void addInventar(Inventari item) {
        if (!inventari.contains(item)) {
            inventari.add(item);
            item.setBiznes(this);
        }
    }

    public void removeInventar(int itemId) {
        inventari.removeIf(item -> item.getInventarId() == itemId);
    }

    public List<Inventari> listInventar() {
        return new ArrayList<>(inventari);
    }

    public List<Inventari> listInventarActive() {
        List<Inventari> active = new ArrayList<>();
        for (Inventari item : inventari) {
            if (item.isActive()) {
                active.add(item);
            }
        }
        return active;
    }

    public void addKategori(Kategori kategori) {
        if (!kategorite.contains(kategori)) {
            kategorite.add(kategori);
        }
    }

    public void removeKategori(Kategori kategori) {
        kategorite.remove(kategori);
    }

    public void addImazh(BiznesImazhe imazh) {
        if (!imazhet.contains(imazh)) {
            imazhet.add(imazh);
            imazh.setBiznes(this);
        }
    }

    public BiznesImazhe getPrimaryImage() {
        for (BiznesImazhe imazh : imazhet) {
            if (imazh.isPrimary()) {
                return imazh;
            }
        }
        return imazhet.isEmpty() ? null : imazhet.get(0);
    }

    public double getAverageRating() {
        if (vleresimet.isEmpty()) return 0.0;
        double sum = 0;
        for (Vleresim v : vleresimet) {
            sum += v.getRating();
        }
        return sum / vleresimet.size();
    }

    // Getters and Setters
    public int getBiznesId() { return biznesId; }
    public void setBiznesId(int biznesId) { this.biznesId = biznesId; }

    public String getEmri() { return emri; }
    public void setEmri(String emri) { this.emri = emri; }

    public String getPershkrim() { return pershkrim; }
    public void setPershkrim(String pershkrim) { this.pershkrim = pershkrim; }

    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }

    public String getNipt() { return nipt; }
    public void setNipt(String nipt) { this.nipt = nipt; }

    public String getLicense() { return license; }
    public void setLicense(String license) { this.license = license; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Lokacion getLokacion() { return lokacion; }
    public void setLokacion(Lokacion lokacion) { this.lokacion = lokacion; }

    public List<Inventari> getInventari() { return inventari; }
    public void setInventari(List<Inventari> inventari) { this.inventari = inventari; }

    public List<Rezervim> getRezervimet() { return rezervimet; }
    public void setRezervimet(List<Rezervim> rezervimet) { this.rezervimet = rezervimet; }

    public List<Vleresim> getVleresimet() { return vleresimet; }
    public void setVleresimet(List<Vleresim> vleresimet) { this.vleresimet = vleresimet; }

    public List<BiznesImazhe> getImazhet() { return imazhet; }
    public void setImazhet(List<BiznesImazhe> imazhet) { this.imazhet = imazhet; }

    public List<Kategori> getKategorite() { return kategorite; }
    public void setKategorite(List<Kategori> kategorite) { this.kategorite = kategorite; }
}
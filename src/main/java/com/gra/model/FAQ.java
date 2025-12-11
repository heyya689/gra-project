package com.gra.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FAQ {
    private int faqId;
    private String pyetje;
    private String pergjigje;
    private int renditja;
    private boolean isActive;
    private LocalDateTime createdAt;
    private List<FaqjaKategori> kategorite;

    public FAQ() {
        this.isActive = true;
        this.kategorite = new ArrayList<>();
    }

    public FAQ(int faqId, String pyetje, String pergjigje) {
        this();
        this.faqId = faqId;
        this.pyetje = pyetje;
        this.pergjigje = pergjigje;
    }

    public String getAnswer() {
        return pergjigje;
    }

    public void addKategori(FaqjaKategori kategori) {
        if (!kategorite.contains(kategori)) {
            kategorite.add(kategori);
        }
    }

    public void removeKategori(FaqjaKategori kategori) {
        kategorite.remove(kategori);
    }

    public List<String> getKategoriEmer() {
        List<String> emrat = new ArrayList<>();
        for (FaqjaKategori k : kategorite) {
            emrat.add(k.getEmri());
        }
        return emrat;
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public String getShortAnswer(int maxLength) {
        if (pergjigje.length() <= maxLength) {
            return pergjigje;
        }
        return pergjigje.substring(0, maxLength - 3) + "...";
    }

    // Getters and Setters
    public int getFaqId() { return faqId; }
    public void setFaqId(int faqId) { this.faqId = faqId; }

    public String getPyetje() { return pyetje; }
    public void setPyetje(String pyetje) { this.pyetje = pyetje; }

    public String getPergjigje() { return pergjigje; }
    public void setPergjigje(String pergjigje) { this.pergjigje = pergjigje; }

    public int getRenditja() { return renditja; }
    public void setRenditja(int renditja) { this.renditja = renditja; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<FaqjaKategori> getKategorite() { return kategorite; }
    public void setKategorite(List<FaqjaKategori> kategorite) { this.kategorite = kategorite; }
}
package com.gra.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FaqjaKategori {
    private int kategoriId;
    private String emri;
    private String pershkrim;
    private int renditja;
    private LocalDateTime createdAt;
    private List<FAQ> faqs;

    public FaqjaKategori() {
        this.faqs = new ArrayList<>();
    }

    public FaqjaKategori(int kategoriId, String emri) {
        this();
        this.kategoriId = kategoriId;
        this.emri = emri;
    }

    public void addFAQ(FAQ faq) {
        if (!faqs.contains(faq)) {
            faqs.add(faq);
            faq.addKategori(this);
        }
    }

    public void removeFAQ(FAQ faq) {
        faqs.remove(faq);
        faq.removeKategori(this);
    }

    public List<FAQ> getActiveFAQs() {
        List<FAQ> active = new ArrayList<>();
        for (FAQ faq : faqs) {
            if (faq.isActive()) {
                active.add(faq);
            }
        }
        return active;
    }

    public int getFAQCount() {
        return faqs.size();
    }

    // Getters and Setters
    public int getKategoriId() { return kategoriId; }
    public void setKategoriId(int kategoriId) { this.kategoriId = kategoriId; }

    public String getEmri() { return emri; }
    public void setEmri(String emri) { this.emri = emri; }

    public String getPershkrim() { return pershkrim; }
    public void setPershkrim(String pershkrim) { this.pershkrim = pershkrim; }

    public int getRenditja() { return renditja; }
    public void setRenditja(int renditja) { this.renditja = renditja; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<FAQ> getFaqs() { return faqs; }
    public void setFaqs(List<FAQ> faqs) { this.faqs = faqs; }
}
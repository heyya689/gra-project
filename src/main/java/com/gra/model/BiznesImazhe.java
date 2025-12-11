package com.gra.model;

import java.time.LocalDateTime;

public class BiznesImazhe {
    private int imazhId;
    private Biznes biznes;
    private String url;
    private String pershkrim;
    private boolean isPrimary;
    private int renditja;
    private LocalDateTime createdAt;

    public BiznesImazhe() {
        this.isPrimary = false;
        this.createdAt = LocalDateTime.now();
    }

    public BiznesImazhe(int imazhId, Biznes biznes, String url, String pershkrim) {
        this();
        this.imazhId = imazhId;
        this.biznes = biznes;
        this.url = url;
        this.pershkrim = pershkrim;
    }

    public String getFullUrl() {
        // Në aplikacion real, kjo do të shtonte domain-in
        if (url.startsWith("http")) {
            return url;
        }
        return "https://gra-system.com/uploads/" + url;
    }

    public void setAsPrimary() {
        this.isPrimary = true;
    }

    public void removeAsPrimary() {
        this.isPrimary = false;
    }

    public boolean isImage() {
        if (url == null) return false;
        String lowerUrl = url.toLowerCase();
        return lowerUrl.endsWith(".jpg") ||
                lowerUrl.endsWith(".jpeg") ||
                lowerUrl.endsWith(".png") ||
                lowerUrl.endsWith(".gif");
    }

    public String getFileExtension() {
        if (url == null) return "";
        int lastDot = url.lastIndexOf('.');
        if (lastDot != -1) {
            return url.substring(lastDot + 1).toLowerCase();
        }
        return "";
    }

    // Getters and Setters
    public int getImazhId() { return imazhId; }
    public void setImazhId(int imazhId) { this.imazhId = imazhId; }

    public Biznes getBiznes() { return biznes; }
    public void setBiznes(Biznes biznes) { this.biznes = biznes; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getPershkrim() { return pershkrim; }
    public void setPershkrim(String pershkrim) { this.pershkrim = pershkrim; }

    public boolean isPrimary() { return isPrimary; }
    public void setPrimary(boolean primary) { isPrimary = primary; }

    public int getRenditja() { return renditja; }
    public void setRenditja(int renditja) { this.renditja = renditja; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
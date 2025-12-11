package com.gra.model;

import java.time.LocalDateTime;

public class Lokacion {
    private int lokacionId;
    private String qyteti;
    private String adresa;
    private String rruga;
    private String numri;
    private String zipCode;
    private Double latitude;
    private Double longitude;
    private LocalDateTime createdAt;

    public Lokacion() {}

    public Lokacion(int lokacionId, String qyteti, String adresa) {
        this.lokacionId = lokacionId;
        this.qyteti = qyteti;
        this.adresa = adresa;
    }

    public String formatAddress() {
        StringBuilder address = new StringBuilder();

        if (rruga != null && !rruga.isEmpty()) {
            address.append(rruga);
            if (numri != null && !numri.isEmpty()) {
                address.append(" ").append(numri);
            }
            address.append(", ");
        }

        if (adresa != null && !adresa.isEmpty()) {
            address.append(adresa).append(", ");
        }

        if (qyteti != null && !qyteti.isEmpty()) {
            address.append(qyteti);
        }

        if (zipCode != null && !zipCode.isEmpty()) {
            address.append(" ").append(zipCode);
        }

        return address.toString().trim();
    }

    public String getGoogleMapsLink() {
        if (latitude != null && longitude != null) {
            return String.format("https://maps.google.com/?q=%f,%f", latitude, longitude);
        }
        return null;
    }

    public boolean hasCoordinates() {
        return latitude != null && longitude != null;
    }

    public double calculateDistance(Lokacion other) {
        if (!this.hasCoordinates() || !other.hasCoordinates()) {
            return -1;
        }

        // Formula e Haversine për llogaritjen e distancës
        final int R = 6371; // Rrezja e Tokës në km

        double latDistance = Math.toRadians(other.latitude - this.latitude);
        double lonDistance = Math.toRadians(other.longitude - this.longitude);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(other.latitude))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // Distanca në km
    }

    // Getters and Setters
    public int getLokacionId() { return lokacionId; }
    public void setLokacionId(int lokacionId) { this.lokacionId = lokacionId; }

    public String getQyteti() { return qyteti; }
    public void setQyteti(String qyteti) { this.qyteti = qyteti; }

    public String getAdresa() { return adresa; }
    public void setAdresa(String adresa) { this.adresa = adresa; }

    public String getRruga() { return rruga; }
    public void setRruga(String rruga) { this.rruga = rruga; }

    public String getNumri() { return numri; }
    public void setNumri(String numri) { this.numri = numri; }

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
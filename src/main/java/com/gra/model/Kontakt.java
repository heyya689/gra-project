package com.gra.model;

import java.time.LocalDateTime;

public class Kontakt {
    private int kontaktId;
    private User user;
    private String email;
    private String subjekti;
    private String mesazh;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Kontakt() {
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Kontakt(int kontaktId, User user, String email, String subjekti, String mesazh) {
        this();
        this.kontaktId = kontaktId;
        this.user = user;
        this.email = email;
        this.subjekti = subjekti;
        this.mesazh = mesazh;
    }

    public void sendMessage() {
        System.out.println("Dërgohet mesazhi i kontaktit...");
        System.out.println("Nga: " + (user != null ? user.getEmail() : email));
        System.out.println("Subjekti: " + subjekti);
        System.out.println("Mesazhi: " + mesazh);

        // Në aplikacion real, kjo do të dërgohej në sistemin e ticket-ave
        this.status = "SENT";
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsRead() {
        if (status.equals("PENDING") || status.equals("SENT")) {
            this.status = "READ";
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void reply() {
        if (status.equals("READ")) {
            this.status = "REPLIED";
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void close() {
        this.status = "CLOSED";
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isOpen() {
        return !status.equals("CLOSED");
    }

    public String getPreview() {
        if (mesazh.length() > 100) {
            return mesazh.substring(0, 97) + "...";
        }
        return mesazh;
    }

    // Getters and Setters
    public int getKontaktId() { return kontaktId; }
    public void setKontaktId(int kontaktId) { this.kontaktId = kontaktId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSubjekti() { return subjekti; }
    public void setSubjekti(String subjekti) { this.subjekti = subjekti; }

    public String getMesazh() { return mesazh; }
    public void setMesazh(String mesazh) { this.mesazh = mesazh; }

    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
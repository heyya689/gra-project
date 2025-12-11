package com.gra.model;

import java.time.LocalDateTime;

public class Vleresim {
    private int vleresimId;
    private User user;
    private Biznes biznes;
    private int rating;
    private String koment;
    private boolean isApproved;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Vleresim() {
        this.isApproved = false;
    }

    public Vleresim(int vleresimId, User user, Biznes biznes, int rating, String koment) {
        this();
        this.vleresimId = vleresimId;
        this.user = user;
        this.biznes = biznes;
        this.rating = rating;
        this.koment = koment;
    }

    public void submit() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        // Në aplikacion real, kjo do të dërgohej për miratim
        this.isApproved = true; // Për thjeshtësi, e miratojmë automatikisht

        System.out.println("Vlerësimi u dërgua për miratim!");
    }

    public void edit(String koment, int rating) {
        if (koment != null && !koment.isEmpty()) {
            this.koment = koment;
        }
        if (rating >= 1 && rating <= 5) {
            this.rating = rating;
        }
        this.updatedAt = LocalDateTime.now();
        System.out.println("Vlerësimi u përditësua!");
    }

    public void delete() {
        this.koment = "[I fshirë]";
        this.isApproved = false;
        this.updatedAt = LocalDateTime.now();
        System.out.println("Vlerësimi u fshi!");
    }

    public void approve() {
        this.isApproved = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void reject() {
        this.isApproved = false;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isValid() {
        return rating >= 1 && rating <= 5 &&
                koment != null && !koment.trim().isEmpty() &&
                user != null && biznes != null;
    }

    public String getRatingStars() {
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            if (i < rating) {
                stars.append("★");
            } else {
                stars.append("☆");
            }
        }
        return stars.toString();
    }

    // Getters and Setters
    public int getVleresimId() { return vleresimId; }
    public void setVleresimId(int vleresimId) { this.vleresimId = vleresimId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Biznes getBiznes() { return biznes; }
    public void setBiznes(Biznes biznes) { this.biznes = biznes; }

    public int getRating() { return rating; }
    public void setRating(int rating) {
        if (rating >= 1 && rating <= 5) {
            this.rating = rating;
        }
    }

    public String getKoment() { return koment; }
    public void setKoment(String koment) { this.koment = koment; }

    public boolean isApproved() { return isApproved; }
    public void setApproved(boolean approved) { isApproved = approved; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
package com.gra.model;

import java.time.LocalDateTime;

public class Preferenca {
    private int preferencaId;
    private User user;
    private boolean njoftimeAktive;
    private String gjuha;
    private String tema;
    private boolean emailNotifications;
    private boolean smsNotifications;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Preferenca() {
        this.njoftimeAktive = true;
        this.gjuha = "sq";
        this.tema = "light";
        this.emailNotifications = true;
        this.smsNotifications = false;
    }

    public Preferenca(User user) {
        this();
        this.user = user;
    }

    public void enableNotifications() {
        this.njoftimeAktive = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void disableNotifications() {
        this.njoftimeAktive = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void changeLanguage(String gjuha) {
        if (gjuha != null && !gjuha.isEmpty()) {
            this.gjuha = gjuha;
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void toggleEmailNotifications() {
        this.emailNotifications = !this.emailNotifications;
        this.updatedAt = LocalDateTime.now();
    }

    public void toggleSmsNotifications() {
        this.smsNotifications = !this.smsNotifications;
        this.updatedAt = LocalDateTime.now();
    }

    public void changeTheme(String tema) {
        if (tema != null && (tema.equals("light") || tema.equals("dark"))) {
            this.tema = tema;
            this.updatedAt = LocalDateTime.now();
        }
    }

    // Getters and Setters
    public int getPreferencaId() { return preferencaId; }
    public void setPreferencaId(int preferencaId) { this.preferencaId = preferencaId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public boolean isNjoftimeAktive() { return njoftimeAktive; }
    public void setNjoftimeAktive(boolean njoftimeAktive) {
        this.njoftimeAktive = njoftimeAktive;
        this.updatedAt = LocalDateTime.now();
    }

    public String getGjuha() { return gjuha; }
    public void setGjuha(String gjuha) {
        this.gjuha = gjuha;
        this.updatedAt = LocalDateTime.now();
    }

    public String getTema() { return tema; }
    public void setTema(String tema) {
        this.tema = tema;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isEmailNotifications() { return emailNotifications; }
    public void setEmailNotifications(boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isSmsNotifications() { return smsNotifications; }
    public void setSmsNotifications(boolean smsNotifications) {
        this.smsNotifications = smsNotifications;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
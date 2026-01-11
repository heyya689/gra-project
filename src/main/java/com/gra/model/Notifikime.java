package com.gra.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Notifikime {
    // Lista e vëzhguesve
    private static List<NotificationObserver> observers = new ArrayList<>();

    private int njoftimId;
    private User user;
    private String titulli;
    private String mesazh;
    private String tipi;
    private boolean lexuar;
    private LocalDateTime data;

    // --- METODAT E OBSERVER PATTERN ---
    public static void attach(NotificationObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public static void detach(NotificationObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (NotificationObserver observer : observers) {
            observer.update(this);
        }
    }

    // --- KONSTRUKTORËT ---
    public Notifikime() {
        this.lexuar = false;
        this.data = LocalDateTime.now();
    }

    public Notifikime(int njoftimId, User user, String titulli, String mesazh) {
        this();
        this.njoftimId = njoftimId;
        this.user = user;
        this.titulli = titulli;
        this.mesazh = mesazh;
    }

    // --- METODAT KRYESORE ---
    public void send() {
        System.out.println("Dërgohet njoftimi: " + titulli);
        System.out.println("Për: " + (user != null ? user.getEmail() : "Unknown"));

        // Njoftojmë vëzhguesit automatikisht
        notifyObservers();
    }

    public void markAsRead() {
        if (!lexuar) {
            this.lexuar = true;
            System.out.println("Njoftimi u shënua si i lexuar.");
        }
    }

    public void markAsUnread() {
        this.lexuar = false;
    }

    public String getPreview() {
        if (mesazh != null && mesazh.length() > 50) {
            return mesazh.substring(0, 47) + "...";
        }
        return mesazh;
    }

    public String getFormattedDate() {
        return data.toString();
    }

    public boolean isRecent() {
        LocalDateTime twentyFourHoursAgo = LocalDateTime.now().minusHours(24);
        return data.isAfter(twentyFourHoursAgo);
    }

    // --- GETTERS AND SETTERS ---
    public int getNjoftimId() { return njoftimId; }
    public void setNjoftimId(int njoftimId) { this.njoftimId = njoftimId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getTitulli() { return titulli; }
    public void setTitulli(String titulli) { this.titulli = titulli; }

    public String getMesazh() { return mesazh; }
    public void setMesazh(String mesazh) { this.mesazh = mesazh; }

    public String getTipi() { return tipi; }
    public void setTipi(String tipi) { this.tipi = tipi; }

    public boolean isLexuar() { return lexuar; }
    public void setLexuar(boolean lexuar) { this.lexuar = lexuar; }

    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }
}
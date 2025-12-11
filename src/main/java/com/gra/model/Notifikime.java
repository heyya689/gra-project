package com.gra.model;

import java.time.LocalDateTime;

public class Notifikime {
    private int njoftimId;
    private User user;
    private String titulli;
    private String mesazh;
    private String tipi;
    private boolean lexuar;
    private LocalDateTime data;

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

    public void send() {
        System.out.println("Dërgohet njoftimi: " + titulli);
        System.out.println("Për: " + (user != null ? user.getEmail() : "Unknown"));
        System.out.println("Mesazhi: " + mesazh);

        // Në aplikacion real, kjo do të dërgohej në email/push notification
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
        if (mesazh.length() > 50) {
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

    // Getters and Setters
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
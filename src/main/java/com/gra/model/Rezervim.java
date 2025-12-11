package com.gra.model;

import java.time.LocalDateTime;

public class Rezervim {
    private int rezervimId;
    private User user;
    private Biznes biznes;
    private Inventari inventar;
    private LocalDateTime data;
    private int numriPersonave;
    private String shënime;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Pagesat pagesa;

    public Rezervim() {
        this.status = "PENDING";
    }

    public void create() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = "PENDING";
        System.out.println("Rezervimi u krijua me sukses!");
    }

    public void cancel() {
        if (!status.equals("CANCELLED") && !status.equals("COMPLETED")) {
            this.status = "CANCELLED";
            this.updatedAt = LocalDateTime.now();
            System.out.println("Rezervimi u anullua.");

            // Refund payment if exists
            if (pagesa != null && pagesa.getStatus().equals("COMPLETED")) {
                pagesa.refund();
            }
        }
    }

    public void confirm() {
        if (status.equals("PENDING")) {
            this.status = "CONFIRMED";
            this.updatedAt = LocalDateTime.now();
            System.out.println("Rezervimi u konfirmua!");
        }
    }

    public void complete() {
        if (status.equals("CONFIRMED")) {
            this.status = "COMPLETED";
            this.updatedAt = LocalDateTime.now();
            System.out.println("Rezervimi u shënuar si i përfunduar.");
        }
    }

    public boolean canBeCancelled() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cancellationDeadline = data.minusHours(24); // 24 orë para
        return now.isBefore(cancellationDeadline) &&
                !status.equals("CANCELLED") &&
                !status.equals("COMPLETED");
    }

    public boolean isActive() {
        return status.equals("PENDING") || status.equals("CONFIRMED");
    }

    // Getters and Setters
    public int getRezervimId() { return rezervimId; }
    public void setRezervimId(int rezervimId) { this.rezervimId = rezervimId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Biznes getBiznes() { return biznes; }
    public void setBiznes(Biznes biznes) { this.biznes = biznes; }

    public Inventari getInventar() { return inventar; }
    public void setInventar(Inventari inventar) { this.inventar = inventar; }

    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }

    public int getNumriPersonave() { return numriPersonave; }
    public void setNumriPersonave(int numriPersonave) { this.numriPersonave = numriPersonave; }

    public String getShënime() { return shënime; }
    public void setShënime(String shënime) { this.shënime = shënime; }

    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Pagesat getPagesa() { return pagesa; }
    public void setPagesa(Pagesat pagesa) { this.pagesa = pagesa; }
}
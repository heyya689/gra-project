package com.gra.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pagesat {
    private int pagesaId;
    private Rezervim rezervim;
    private double shuma;
    private String metoda;
    private String status;
    private String transactionId;
    private LocalDateTime paymentDate;
    private LocalDateTime createdAt;
    private PagesatDetaje detaje;
    private List<PagesatHistorik> historiku;

    public Pagesat() {
        this.status = "PENDING";
        this.historiku = new ArrayList<>();
    }

    public Pagesat(int pagesaId, double shuma, String metoda) {
        this();
        this.pagesaId = pagesaId;
        this.shuma = shuma;
        this.metoda = metoda;
    }

    public boolean processPayment() {
        if (status.equals("PENDING")) {
            // Simulojmë procesimin e pagesës
            this.status = "COMPLETED";
            this.paymentDate = LocalDateTime.now();
            this.transactionId = "TXN_" + System.currentTimeMillis();

            // Krijo historik
            PagesatHistorik historik = new PagesatHistorik();
            historik.setStatus("COMPLETED");
            historik.setMesazh("Pagesa u procesua me sukses");
            historiku.add(historik);

            // Update rezervimi
            if (rezervim != null) {
                rezervim.confirm();
            }

            System.out.println("Pagesa u procesua me sukses!");
            return true;
        }
        return false;
    }

    public boolean refund() {
        if (status.equals("COMPLETED")) {
            this.status = "REFUNDED";

            // Krijo historik
            PagesatHistorik historik = new PagesatHistorik();
            historik.setStatus("REFUNDED");
            historik.setMesazh("Pagesa u rimbursua");
            historiku.add(historik);

            System.out.println("Pagesa u rimbursua me sukses!");
            return true;
        }
        return false;
    }

    public String checkStatus() {
        return status;
    }

    public void addToHistory(String status, String message) {
        PagesatHistorik historik = new PagesatHistorik();
        historik.setStatus(status);
        historik.setMesazh(message);
        historik.setData(LocalDateTime.now());
        historiku.add(historik);
    }

    public boolean isCompleted() {
        return status.equals("COMPLETED");
    }

    public boolean isPending() {
        return status.equals("PENDING");
    }

    public boolean isFailed() {
        return status.equals("FAILED");
    }

    public boolean isRefunded() {
        return status.equals("REFUNDED");
    }

    // Getters and Setters
    public int getPagesaId() { return pagesaId; }
    public void setPagesaId(int pagesaId) { this.pagesaId = pagesaId; }

    public Rezervim getRezervim() { return rezervim; }
    public void setRezervim(Rezervim rezervim) { this.rezervim = rezervim; }

    public double getShuma() { return shuma; }
    public void setShuma(double shuma) { this.shuma = shuma; }

    public String getMetoda() { return metoda; }
    public void setMetoda(String metoda) { this.metoda = metoda; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public PagesatDetaje getDetaje() { return detaje; }
    public void setDetaje(PagesatDetaje detaje) { this.detaje = detaje; }

    public List<PagesatHistorik> getHistoriku() { return historiku; }
    public void setHistoriku(List<PagesatHistorik> historiku) { this.historiku = historiku; }
}
package com.gra.model;

import java.time.LocalDateTime;

public class PagesatHistorik {
    private int historikId;
    private Pagesat pagesa;
    private String status;
    private String mesazh;
    private LocalDateTime data;

    public PagesatHistorik() {
        this.data = LocalDateTime.now();
    }

    public PagesatHistorik(String status, String mesazh) {
        this();
        this.status = status;
        this.mesazh = mesazh;
    }

    public void addRecord() {
        System.out.println("U shtua rekord historik: " + status + " - " + mesazh);
    }

    public String getFormattedDate() {
        return data.toString();
    }

    public boolean isSuccess() {
        return status != null &&
                (status.equals("COMPLETED") ||
                        status.equals("SUCCESS") ||
                        status.equals("APPROVED"));
    }

    public boolean isFailure() {
        return status != null &&
                (status.equals("FAILED") ||
                        status.equals("DECLINED") ||
                        status.equals("ERROR"));
    }

    // Getters and Setters
    public int getHistorikId() { return historikId; }
    public void setHistorikId(int historikId) { this.historikId = historikId; }

    public Pagesat getPagesa() { return pagesa; }
    public void setPagesa(Pagesat pagesa) { this.pagesa = pagesa; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMesazh() { return mesazh; }
    public void setMesazh(String mesazh) { this.mesazh = mesazh; }

    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }
}
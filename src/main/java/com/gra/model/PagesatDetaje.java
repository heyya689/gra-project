package com.gra.model;

import java.time.LocalDateTime;

public class PagesatDetaje {
    private int detajeId;
    private Pagesat pagesa;
    private String reference;
    private String cardLastFour;
    private String cardType;
    private String paymentGateway;
    private String gatewayResponse;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime createdAt;

    public PagesatDetaje() {}

    public PagesatDetaje(int detajeId, String reference) {
        this.detajeId = detajeId;
        this.reference = reference;
    }

    public boolean validateReference() {
        return reference != null && reference.length() >= 10;
    }

    public void setGatewayResponse(String response) {
        this.gatewayResponse = response;
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    public boolean isResponseSuccessful() {
        if (gatewayResponse == null) return false;
        String response = gatewayResponse.toUpperCase();
        return response.contains("SUCCESS") || response.contains("APPROVED");
    }

    // Getters and Setters
    public int getDetajeId() { return detajeId; }
    public void setDetajeId(int detajeId) { this.detajeId = detajeId; }

    public Pagesat getPagesa() { return pagesa; }
    public void setPagesa(Pagesat pagesa) { this.pagesa = pagesa; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public String getCardLastFour() { return cardLastFour; }
    public void setCardLastFour(String cardLastFour) { this.cardLastFour = cardLastFour; }

    public String getCardType() { return cardType; }
    public void setCardType(String cardType) { this.cardType = cardType; }

    public String getPaymentGateway() { return paymentGateway; }
    public void setPaymentGateway(String paymentGateway) { this.paymentGateway = paymentGateway; }

    public String getGatewayResponse() { return gatewayResponse; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
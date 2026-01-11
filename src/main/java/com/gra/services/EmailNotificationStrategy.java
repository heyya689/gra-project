package com.gra.services;

import com.gra.model.Notifikime;

// Strategjia për Email
class EmailNotificationStrategy implements NotificationStrategy {
    @Override
    public void sendMessage(Notifikime njoftim) {
        System.out.println("📧 Duke dërguar Email te: " + njoftim.getUser().getEmail());
        System.out.println("Subjekti: " + njoftim.getTitulli());
    }
}

// Strategjia për SMS
class SMSNotificationStrategy implements NotificationStrategy {
    @Override
    public void sendMessage(Notifikime njoftim) {
        System.out.println("📱 Duke dërguar SMS te përdoruesi: " + njoftim.getUser().getName());
        System.out.println("Mesazhi: " + njoftim.getPreview());
    }
}
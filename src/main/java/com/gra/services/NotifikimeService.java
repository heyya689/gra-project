package com.gra.services;

import com.gra.model.Notifikime;
import com.gra.dao.NotifikimeDAO; // Supozojmë se e ke këtë klasë

public class NotifikimeService {
    private NotificationStrategy strategy;
    private final NotifikimeDAO dao;

    public NotifikimeService() {
        this.dao = new NotifikimeDAO();
        // Vendosim një strategji default (opsionale)
        this.strategy = new EmailNotificationStrategy();
    }

    // Lejon ndryshimin e strategjisë në runtime
    public void setStrategy(NotificationStrategy strategy) {
        this.strategy = strategy;
    }

    public void procesoDergimin(Notifikime njoftim) {
        // 1. Validimi (Logjika e Biznesit)
        if (njoftim == null || njoftim.getMesazh().isEmpty()) {
            System.out.println("❌ Gabim: Njoftimi është i zbrazët.");
            return;
        }

        // 2. Ruajtja në Database përmes DAO
        try {
            dao.save(njoftim);
        } catch (Exception e) {
            System.out.println("⚠️ Nuk u ruajt në DB: " + e.getMessage());
        }

        // 3. Dërgimi sipas strategjisë së zgjedhur
        if (strategy != null) {
            strategy.sendMessage(njoftim);
        }

        // 4. Thirrja e metodës send() të modelit për të aktivizuar Observer-at
        njoftim.send();
    }
}
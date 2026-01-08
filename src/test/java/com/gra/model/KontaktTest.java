package com.gra.model;

import java.time.LocalDateTime;

public class KontaktTest {

    public static void main(String[] args) {
        System.out.println("🧪 Duke nisur testimin për Kontakt.java...");

        testCikliStatusit();
        testPreviewMesazhit();
        testLogjikaEDergimit();
        testUpdateTimestamps();

        System.out.println("\n✅ Të gjitha testet për Kontakt.java kaluan me sukses!");
    }

    private static void testCikliStatusit() {
        Kontakt k = new Kontakt();

        // 1. Fillimi: PENDING
        assert k.getStatus().equals("PENDING") : "Gabim: Statusi fillestar duhet të jetë PENDING";
        assert k.isOpen() : "Gabim: Mesazhi i ri duhet të jetë i hapur";

        // 2. Mark as Read (vetëm nëse është PENDING ose SENT)
        k.markAsRead();
        assert k.getStatus().equals("READ") : "Gabim: Statusi duhet të ishte READ";

        // 3. Reply (vetëm nëse është READ)
        k.reply();
        assert k.getStatus().equals("REPLIED") : "Gabim: Statusi duhet të ishte REPLIED";

        // 4. Close
        k.close();
        assert k.getStatus().equals("CLOSED") : "Gabim: Statusi duhet të ishte CLOSED";
        assert !k.isOpen() : "Gabim: Mesazhi i mbyllur nuk duhet të jetë i hapur";

        System.out.println("  - Testi i Ciklit të Statusit: OK");
    }

    private static void testPreviewMesazhit() {
        Kontakt k = new Kontakt();

        // Rasti 1: Mesazh i shkurtër
        k.setMesazh("Përshëndetje!");
        assert k.getPreview().equals("Përshëndetje!") : "Gabim: Preview i shkurtër nuk duhet të ndryshojë";

        // Rasti 2: Mesazh i gjatë (> 100 karaktere)
        String mesazhIGjate = "Ky është një mesazh shumë i gjatë i cili shërben për të testuar nëse " +
                "metoda getPreview funksionon saktë dhe e shkurton tekstin në limitin e duhur.";
        k.setMesazh(mesazhIGjate);

        String preview = k.getPreview();
        assert preview.length() == 100 : "Gabim: Preview duhet të jetë saktësisht 100 karaktere";
        assert preview.endsWith("...") : "Gabim: Preview duhet të përfundojë me '...'";

        System.out.println("  - Testi i Preview-t: OK");
    }

    private static void testLogjikaEDergimit() {
        Kontakt k = new Kontakt();
        k.setSubjekti("Ankesë");
        k.setMesazh("Nuk funksionon kodi.");
        k.setEmail("test@example.com");

        k.sendMessage(); // Kjo do të printojë në konsolë
        assert k.getStatus().equals("SENT") : "Gabim: Pas dërgimit statusi duhet të jetë SENT";

        System.out.println("  - Testi i Dërgimit: OK");
    }

    private static void testUpdateTimestamps() {
        Kontakt k = new Kontakt();
        LocalDateTime kohaePare = k.getUpdatedAt();

        // Presim pak që të ndryshojë koha (opsionale në teste kaq të shpejta)
        k.markAsRead();
        assert k.getUpdatedAt() != null : "Gabim: updatedAt duhet të përditësohet";

        System.out.println("  - Testi i Timestamps: OK");
    }
}
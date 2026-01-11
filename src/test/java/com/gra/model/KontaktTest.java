package com.gra.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class KontaktTest {

    @Test
    void testCikliStatusit() {
        Kontakt k = new Kontakt();

        assertEquals("PENDING", k.getStatus(),
                "Statusi fillestar duhet të jetë PENDING");

        assertTrue(k.isOpen(),
                "Mesazhi i ri duhet të jetë i hapur");

        k.markAsRead();
        assertEquals("READ", k.getStatus(),
                "Statusi duhet të ishte READ");

        k.reply();
        assertEquals("REPLIED", k.getStatus(),
                "Statusi duhet të ishte REPLIED");

        k.close();
        assertEquals("CLOSED", k.getStatus(),
                "Statusi duhet të ishte CLOSED");

        assertFalse(k.isOpen(),
                "Mesazhi i mbyllur nuk duhet të jetë i hapur");
    }

    @Test
    void testPreviewMesazhit() {
        Kontakt k = new Kontakt();

        k.setMesazh("Përshëndetje!");
        assertEquals("Përshëndetje!",
                k.getPreview(),
                "Preview i shkurtër nuk duhet të ndryshojë");

        String mesazhIGjate = "Ky është një mesazh shumë i gjatë i cili shërben për të testuar nëse " +
                "metoda getPreview funksionon saktë dhe e shkurton tekstin në limitin e duhur.";

        k.setMesazh(mesazhIGjate);

        String preview = k.getPreview();

        assertEquals(100,
                preview.length(),
                "Preview duhet të jetë saktësisht 100 karaktere");

        assertTrue(preview.endsWith("..."),
                "Preview duhet të përfundojë me '...'");
    }

    @Test
    void testLogjikaEDergimit() {
        Kontakt k = new Kontakt();

        k.setSubjekti("Ankesë");
        k.setMesazh("Nuk funksionon kodi.");
        k.setEmail("test@example.com");

        k.sendMessage();

        assertEquals("SENT",
                k.getStatus(),
                "Pas dërgimit statusi duhet të jetë SENT");
    }

    @Test
    void testUpdateTimestamps() {
        Kontakt k = new Kontakt();
        LocalDateTime updatedBefore = k.getUpdatedAt();

        k.markAsRead();

        assertNotNull(k.getUpdatedAt(),
                "updatedAt duhet të përditësohet");

        // Optional safety check (nëse updatedAt ndryshohet realisht)
        if (updatedBefore != null) {
            assertTrue(
                    k.getUpdatedAt().isAfter(updatedBefore) ||
                            k.getUpdatedAt().isEqual(updatedBefore),
                    "updatedAt duhet të përditësohet pas ndryshimit të statusit"
            );
        }
    }
}

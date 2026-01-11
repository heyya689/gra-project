package com.gra.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class NotifikimeTest {

    @Test
    void testStatusiLeximit() {
        Notifikime n = new Notifikime();

        assertFalse(n.isLexuar(),
                "Njoftimi i ri duhet të jetë i palexuar");

        n.markAsRead();
        assertTrue(n.isLexuar(),
                "Njoftimi duhet të jetë i lexuar");

        n.markAsUnread();
        assertFalse(n.isLexuar(),
                "Njoftimi duhet të kthehej në i palexuar");
    }

    @Test
    void testPreviewMesazhit() {
        Notifikime n = new Notifikime();

        n.setMesazh("Rezervimi juaj u konfirmua.");
        assertEquals("Rezervimi juaj u konfirmua.",
                n.getPreview(),
                "Preview i shkurtër nuk duhet të ndryshojë");

        String mesazhIGjate =
                "Ky është një njoftim shumë i rëndësishëm që kërkon vëmendjen tuaj të menjëhershme.";

        n.setMesazh(mesazhIGjate);

        String preview = n.getPreview();

        assertEquals(50,
                preview.length(),
                "Preview duhet të jetë saktësisht 50 karaktere");

        assertTrue(preview.endsWith("..."),
                "Preview i gjatë duhet të përfundojë me '...'");
    }

    @Test
    void testNjoftimetEFundit() {
        Notifikime n = new Notifikime();

        assertTrue(n.isRecent(),
                "Njoftimi i sapokrijuar duhet të jetë recent");

        n.setData(LocalDateTime.now().minusDays(2));
        assertFalse(n.isRecent(),
                "Njoftimi i krijuar para 48 orësh nuk duhet të jetë recent");
    }

    @Test
    void testKonstruktoriDheDergimi() {
        User user = new User();
        user.setEmail("klienti@gmail.com");

        Notifikime n = new Notifikime(
                10,
                user,
                "Mirësevini",
                "Llogaria u krijua."
        );

        assertEquals(10, n.getNjoftimId());
        assertEquals("klienti@gmail.com", n.getUser().getEmail());

        // vetëm verifikojmë që nuk hedh exception
        assertDoesNotThrow(n::send,
                "Dërgimi i njoftimit nuk duhet të dështojë");
    }
}

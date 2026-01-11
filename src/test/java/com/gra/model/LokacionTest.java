package com.gra.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LokacionTest {

    @Test
    void testFormatimiAdreses() {
        Lokacion lok = new Lokacion();
        lok.setRruga("Rruga e Durrësit");
        lok.setNumri("15");
        lok.setAdresa("Kati 2");
        lok.setQyteti("Tiranë");
        lok.setZipCode("1001");

        String adresaEPlote = lok.formatAddress();

        assertTrue(adresaEPlote.contains("Rruga e Durrësit 15"),
                "Gabim në formatimin e rrugës dhe numrit");

        assertTrue(adresaEPlote.contains("Tiranë 1001"),
                "Gabim në qytet dhe zip code");
    }

    @Test
    void testLlogaritjaDistances() {
        // Lokacioni 1: Tiranë (Sheshi Skënderbej)
        Lokacion tirana = new Lokacion();
        tirana.setLatitude(41.3275);
        tirana.setLongitude(19.8187);

        // Lokacioni 2: Durrës (Qendra)
        Lokacion durres = new Lokacion();
        durres.setLatitude(41.3246);
        durres.setLongitude(19.4560);

        double distanca = tirana.calculateDistance(durres);

        // Distanca ajrore Tiranë–Durrës ≈ 30–31 km
        assertTrue(distanca > 30 && distanca < 32,
                "Distanca e llogaritur nuk është brenda pritshmërive: " + distanca);

        assertEquals(0.0,
                tirana.calculateDistance(tirana),
                "Distanca me veten duhet të jetë 0");
    }

    @Test
    void testValidimiKoordinatave() {
        Lokacion lok1 = new Lokacion(); // pa koordinata

        Lokacion lok2 = new Lokacion();
        lok2.setLatitude(41.0);
        lok2.setLongitude(19.0);

        assertEquals(-1,
                lok1.calculateDistance(lok2),
                "Duhet të kthente -1 kur mungojnë koordinatat");

        assertFalse(lok1.hasCoordinates(),
                "hasCoordinates duhet të jetë false kur mungojnë koordinatat");

        assertTrue(lok2.hasCoordinates(),
                "hasCoordinates duhet të jetë true kur koordinatat janë të pranishme");
    }
}

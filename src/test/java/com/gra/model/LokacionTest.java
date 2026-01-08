package com.gra.model;

public class LokacionTest {

    public static void main(String[] args) {
        System.out.println("🧪 Duke nisur testimin për Lokacion.java...");

        testFormatimiAdreses();
        testLlogaritjaDistances();
        testValidimiKoordinatave();

        System.out.println("\n✅ Të gjitha testet për Lokacion.java kaluan me sukses!");
    }

    private static void testFormatimiAdreses() {
        Lokacion lok = new Lokacion();
        lok.setRruga("Rruga e Durrësit");
        lok.setNumri("15");
        lok.setAdresa("Kati 2");
        lok.setQyteti("Tiranë");
        lok.setZipCode("1001");

        String adresaEPlote = lok.formatAddress();

        // Verifikojmë që pjesët janë bashkuar saktë me presje dhe hapësira
        assert adresaEPlote.contains("Rruga e Durrësit 15") : "Gabim në formatimin e rrugës dhe numrit";
        assert adresaEPlote.contains("Tiranë 1001") : "Gabim në qytet dhe zip code";

        System.out.println("  - Testi i Formatimit të Adresës: OK");
    }

    private static void testLlogaritjaDistances() {
        // Lokacioni 1: Tiranë (Sheshi Skënderbej)
        Lokacion tirana = new Lokacion();
        tirana.setLatitude(41.3275);
        tirana.setLongitude(19.8187);

        // Lokacioni 2: Durrës (Qendra)
        Lokacion durres = new Lokacion();
        durres.setLatitude(41.3246);
        durres.setLongitude(19.4560);

        double distanca = tirana.calculateDistance(durres);

        // Distanca ajrore Tiranë-Durrës është rreth 30-31 km
        assert distanca > 30 && distanca < 32 : "Gabim: Distanca e llogaritur " + distanca + " km nuk është brenda pritshmërive";

        // Testo distancën me veten (duhet të jetë 0)
        assert tirana.calculateDistance(tirana) == 0.0 : "Gabim: Distanca me veten duhet të jetë 0";

        System.out.println("  - Testi i Formulës Haversine (Distanca): OK");
    }

    private static void testValidimiKoordinatave() {
        Lokacion lok1 = new Lokacion(); // Pa koordinata
        Lokacion lok2 = new Lokacion();
        lok2.setLatitude(41.0);
        lok2.setLongitude(19.0);

        // Duhet të kthejë -1 sepse lok1 nuk ka koordinata
        assert lok1.calculateDistance(lok2) == -1 : "Gabim: Duhet të kthente -1 për koordinata mungesë";
        assert !lok1.hasCoordinates() : "Gabim: hasCoordinates duhet të jetë false";
        assert lok2.hasCoordinates() : "Gabim: hasCoordinates duhet të jetë true";

        System.out.println("  - Testi i Validimit të Koordinatave: OK");
    }
}
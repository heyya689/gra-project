package com.gra.model;

import java.time.LocalDateTime;

public class RezervimTest {

    public static void main(String[] args) {
        System.out.println("🧪 Duke nisur testimin për Rezervim.java...");

        testRrjedhaEStatusit();
        testLogjikaEAnullimitMeKohe();
        testIntegrimiIPageses();
        testStatusetAktive();

        System.out.println("\n✅ Të gjitha testet për Rezervim.java kaluan me sukses!");
    }

    private static void testRrjedhaEStatusit() {
        Rezervim r = new Rezervim();
        r.create();

        // 1. Fillimi: PENDING
        assert r.getStatus().equals("PENDING") : "Gabim: Statusi fillestar duhet të jetë PENDING";

        // 2. Konfirmimi: PENDING -> CONFIRMED
        r.confirm();
        assert r.getStatus().equals("CONFIRMED") : "Gabim: Statusi duhet të ishte CONFIRMED";

        // 3. Përfundimi: CONFIRMED -> COMPLETED
        r.complete();
        assert r.getStatus().equals("COMPLETED") : "Gabim: Statusi duhet të ishte COMPLETED";

        // 4. Siguria: Një rezervim i përfunduar nuk mund të anullohet
        r.cancel();
        assert r.getStatus().equals("COMPLETED") : "Gabim: Rezervimi COMPLETED nuk duhet të lejonte anullimin";

        System.out.println("  - Testi i Rrjedhës së Statusit: OK");
    }

    private static void testLogjikaEAnullimitMeKohe() {
        Rezervim r = new Rezervim();
        r.setStatus("CONFIRMED");

        // Rasti 1: Rezervimi është pas 2 ditësh (Duhet të lejohet anullimi)
        r.setData(LocalDateTime.now().plusHours(48));
        assert r.canBeCancelled() : "Gabim: Duhet të lejohej anullimi 48 orë para";

        // Rasti 2: Rezervimi është pas 2 orësh (NUK duhet të lejohet anullimi)
        r.setData(LocalDateTime.now().plusHours(2));
        assert !r.canBeCancelled() : "Gabim: Nuk duhet të lejohej anullimi vetëm 2 orë para";

        System.out.println("  - Testi i Logjikës së Kohës (24h rule): OK");
    }

    private static void testIntegrimiIPageses() {
        Rezervim r = new Rezervim();
        Pagesat p = new Pagesat(1, 2000.0, "CARD");
        p.setStatus("COMPLETED"); // Pagesa është kryer

        r.setPagesa(p);
        r.setStatus("CONFIRMED");

        // Kur anullojmë rezervimin, duhet të thirret automatikisht refund() i pagesës
        r.cancel();
        assert r.getStatus().equals("CANCELLED");
        assert p.getStatus().equals("REFUNDED") : "Gabim: Pagesa duhet të ishte rimbursuar pas anullimit";

        System.out.println("  - Testi i Integrimit Rezervim-Pagesë: OK");
    }

    private static void testStatusetAktive() {
        Rezervim r = new Rezervim();

        r.setStatus("PENDING");
        assert r.isActive() : "Gabim: PENDING duhet të jetë aktiv";

        r.setStatus("CONFIRMED");
        assert r.isActive() : "Gabim: CONFIRMED duhet të jetë aktiv";

        r.setStatus("CANCELLED");
        assert !r.isActive() : "Gabim: CANCELLED nuk duhet të jetë aktiv";

        System.out.println("  - Testi i Gjendjes Aktive: OK");
    }
}
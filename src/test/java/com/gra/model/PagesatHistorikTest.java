package com.gra.model;

import java.time.LocalDateTime;

public class PagesatHistorikTest {

    public static void main(String[] args) {
        System.out.println("🧪 Duke nisur testimin për PagesatHistorik.java...");

        testIdentifikimiStatusit();
        testInicializimiIDates();
        testKonstruktoret();

        System.out.println("\n✅ Të gjitha testet për PagesatHistorik.java kaluan me sukses!");
    }

    private static void testIdentifikimiStatusit() {
        // Testo raste suksesi
        PagesatHistorik h1 = new PagesatHistorik("COMPLETED", "Pagesa u krye");
        assert h1.isSuccess() : "Gabim: COMPLETED duhet të njihet si sukses";
        assert !h1.isFailure() : "Gabim: Suksesi nuk duhet të njihet si dështim";

        PagesatHistorik h2 = new PagesatHistorik("APPROVED", "OK");
        assert h2.isSuccess() : "Gabim: APPROVED duhet të njihet si sukses";

        // Testo raste dështimi
        PagesatHistorik h3 = new PagesatHistorik("FAILED", "Mungesë fondesh");
        assert h3.isFailure() : "Gabim: FAILED duhet të njihet si dështim";
        assert !h3.isSuccess() : "Gabim: Dështimi nuk duhet të njihet si sukses";

        PagesatHistorik h4 = new PagesatHistorik("DECLINED", "Kodi i gabuar");
        assert h4.isFailure() : "Gabim: DECLINED duhet të njihet si dështim";

        System.out.println("  - Testi i Identifikimit të Statusit: OK");
    }

    private static void testInicializimiIDates() {
        PagesatHistorik h = new PagesatHistorik();

        // Verifikojmë që data nuk është null dhe është koha e tanishme
        assert h.getData() != null : "Gabim: Data duhet të inicializohej automatikisht";
        assert h.getData().isBefore(LocalDateTime.now().plusSeconds(1)) : "Gabim: Data e regjistrimit është e pasaktë";

        System.out.println("  - Testi i Inicializimit të Datës: OK");
    }

    private static void testKonstruktoret() {
        PagesatHistorik h = new PagesatHistorik("ERROR", "Lidhja dështoi");

        assert h.getStatus().equals("ERROR");
        assert h.getMesazh().equals("Lidhja dështoi");

        // Verifikojmë metodën e printimit (opsionale)
        h.addRecord();

        System.out.println("  - Testi i Konstruktorëve: OK");
    }
}
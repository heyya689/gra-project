package com.gra.model;

import java.util.List;

public class FAQTest {

    public static void main(String[] args) {
        System.out.println("🧪 Duke nisur testimin e korrigjuar për FAQ.java...");

        testLogjikaEStatusit();
        testShkurtimiIPergjigjes();
        testMenaxhimiKategorive();
        testKonstruktoriMeParametra();

        System.out.println("\n✅ Të gjitha testet për FAQ.java kaluan me sukses!");
    }

    private static void testLogjikaEStatusit() {
        FAQ faq = new FAQ();

        // Default duhet te jete true
        assert faq.isActive() : "Gabim: FAQ duhet të jetë aktive si default";

        faq.deactivate();
        assert !faq.isActive() : "Gabim: FAQ duhet të ishte jo-aktive pas deactivate()";

        faq.activate();
        assert faq.isActive() : "Gabim: FAQ duhet të ishte aktive pas activate()";

        System.out.println("  - Testi i Statusit: OK");
    }

    private static void testShkurtimiIPergjigjes() {
        FAQ faq = new FAQ();
        faq.setPergjigje("Ky eshte nje tekst i gjate per test."); // 34 karaktere

        // Test kur nuk duhet te shkurtohet
        assert faq.getShortAnswer(50).equals(faq.getPergjigje()) : "Gabim: Nuk duhej shkurtuar";

        // Test kur duhet te shkurtohet ne 10
        String shortAns = faq.getShortAnswer(10);
        assert shortAns.length() <= 10 : "Gabim: Tejkalon gjatesine 10";
        assert shortAns.endsWith("...") : "Gabim: Duhet te perfundoje me ...";

        System.out.println("  - Testi i Shkurtimit: OK");
    }

    private static void testMenaxhimiKategorive() {
        FAQ faq = new FAQ();

        // Krijojme kategorine (duke perdorur settera qe te jemi te sigurt me modelin tend)
        FaqjaKategori k1 = new FaqjaKategori();
        k1.setKategoriId(1);
        k1.setEmri("Ndihme");

        faq.addKategori(k1);
        faq.addKategori(k1); // Provo duplikat

        // Testo nese contains punon (varet nese ke bere override equals te FaqjaKategori)
        // Nese nuk ke bere override equals, ky assert mund te deshtoje nese jane objekte te ndryshme
        assert faq.getKategorite().size() == 1 : "Gabim: U shtua duplikat";

        List<String> emrat = faq.getKategoriEmer();
        assert emrat.get(0).equals("Ndihme") : "Gabim: Emri i kategorise nuk eshte korrekt";

        System.out.println("  - Testi i Kategorive: OK");
    }

    private static void testKonstruktoriMeParametra() {
        FAQ faq = new FAQ(5, "Pyetje?", "Pergjigje.");

        assert faq.getFaqId() == 5;
        assert faq.getPyetje().equals("Pyetje?");
        assert faq.getAnswer().equals("Pergjigje.");

        System.out.println("  - Testi i Konstruktorit: OK");
    }
}
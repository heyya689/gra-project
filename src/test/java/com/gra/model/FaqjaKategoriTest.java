package com.gra.model;

import java.util.List;

public class FaqjaKategoriTest {

    public static void main(String[] args) {
        System.out.println("🧪 Duke nisur testimin për FaqjaKategori.java...");

        testMenaxhimiFAQ();
        testFiltrimiActiveFAQs();
        testKonstruktoriDheSetter();

        System.out.println("\n✅ Të gjitha testet për FaqjaKategori.java kaluan me sukses!");
    }

    private static void testMenaxhimiFAQ() {
        FaqjaKategori kategoria = new FaqjaKategori(1, "Teknike");
        FAQ faq1 = new FAQ(101, "Si te lidhem?", "Perdorni kabllon.");

        // Testo shtimin
        kategoria.addFAQ(faq1);
        assert kategoria.getFAQCount() == 1 : "Gabim: Duhet te kishte 1 FAQ";

        // Testo parandalimin e duplikateve
        kategoria.addFAQ(faq1);
        assert kategoria.getFAQCount() == 1 : "Gabim: Nuk duhen lejuar FAQ duplikat";

        // Testo heqjen
        kategoria.removeFAQ(faq1);
        assert kategoria.getFAQCount() == 0 : "Gabim: Lista duhet te ishte bosh";

        System.out.println("  - Testi i menaxhimit të FAQ-ve: OK");
    }

    private static void testFiltrimiActiveFAQs() {
        FaqjaKategori kategoria = new FaqjaKategori(2, "Pagesat");

        FAQ f1 = new FAQ(1, "P1", "D1");
        f1.setActive(true);

        FAQ f2 = new FAQ(2, "P2", "D2");
        f2.setActive(false);

        kategoria.addFAQ(f1);
        kategoria.addFAQ(f2);

        // Testo nese getActiveFAQs kthen vetem ato me status true
        List<FAQ> activeList = kategoria.getActiveFAQs();
        assert activeList.size() == 1 : "Gabim: Duhet te kishte vetem 1 FAQ aktive";
        assert activeList.get(0).getPyetje().equals("P1") : "Gabim: FAQ e gjetur nuk eshte ajo e duhura";

        System.out.println("  - Testi i filtrimit të FAQ-ve aktive: OK");
    }

    private static void testKonstruktoriDheSetter() {
        FaqjaKategori kat = new FaqjaKategori();
        kat.setEmri("Marketing");
        kat.setPershkrim("Kategoria per reklama");
        kat.setRenditja(5);

        assert kat.getEmri().equals("Marketing");
        assert kat.getPershkrim().equals("Kategoria per reklama");
        assert kat.getRenditja() == 5;
        assert kat.getFaqs() != null : "Gabim: Lista e faqs duhet te inicializohet ne konstruktor";

        System.out.println("  - Testi i atributeve bazë: OK");
    }
}
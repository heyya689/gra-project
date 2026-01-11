package com.gra.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FaqjaKategoriTest {

    @Test
    void testMenaxhimiFAQ() {
        FaqjaKategori kategoria = new FaqjaKategori(1, "Teknike");
        FAQ faq1 = new FAQ(101, "Si te lidhem?", "Perdorni kabllon.");

        kategoria.addFAQ(faq1);
        assertEquals(1, kategoria.getFAQCount(),
                "Duhet te kishte 1 FAQ");

        kategoria.addFAQ(faq1);
        assertEquals(1, kategoria.getFAQCount(),
                "Nuk duhen lejuar FAQ duplikat");

        kategoria.removeFAQ(faq1);
        assertEquals(0, kategoria.getFAQCount(),
                "Lista duhet te ishte bosh");
    }

    @Test
    void testFiltrimiActiveFAQs() {
        FaqjaKategori kategoria = new FaqjaKategori(2, "Pagesat");

        FAQ f1 = new FAQ(1, "P1", "D1");
        f1.setActive(true);

        FAQ f2 = new FAQ(2, "P2", "D2");
        f2.setActive(false);

        kategoria.addFAQ(f1);
        kategoria.addFAQ(f2);

        List<FAQ> activeList = kategoria.getActiveFAQs();

        assertEquals(1, activeList.size(),
                "Duhet te kishte vetem 1 FAQ aktive");

        assertEquals("P1",
                activeList.get(0).getPyetje(),
                "FAQ e gjetur nuk eshte ajo e duhura");
    }

    @Test
    void testKonstruktoriDheSetter() {
        FaqjaKategori kat = new FaqjaKategori();

        kat.setEmri("Marketing");
        kat.setPershkrim("Kategoria per reklama");
        kat.setRenditja(5);

        assertEquals("Marketing", kat.getEmri());
        assertEquals("Kategoria per reklama", kat.getPershkrim());
        assertEquals(5, kat.getRenditja());
        assertNotNull(kat.getFaqs(),
                "Lista e faqs duhet te inicializohet ne konstruktor");
    }
}

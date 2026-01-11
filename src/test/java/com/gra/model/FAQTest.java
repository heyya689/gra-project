package com.gra.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FAQTest {

    @Test
    void testLogjikaEStatusit() {
        FAQ faq = new FAQ();

        assertTrue(faq.isActive(),
                "FAQ duhet të jetë aktive si default");

        faq.deactivate();
        assertFalse(faq.isActive(),
                "FAQ duhet të ishte jo-aktive pas deactivate()");

        faq.activate();
        assertTrue(faq.isActive(),
                "FAQ duhet të ishte aktive pas activate()");
    }

    @Test
    void testShkurtimiIPergjigjes() {
        FAQ faq = new FAQ();
        faq.setPergjigje("Ky eshte nje tekst i gjate per test.");

        assertEquals(faq.getPergjigje(),
                faq.getShortAnswer(50),
                "Nuk duhej shkurtuar");

        String shortAns = faq.getShortAnswer(10);

        assertTrue(shortAns.length() <= 10,
                "Tejkalon gjatesine 10");

        assertTrue(shortAns.endsWith("..."),
                "Duhet te perfundoje me ...");
    }

    @Test
    void testMenaxhimiKategorive() {
        FAQ faq = new FAQ();

        FaqjaKategori k1 = new FaqjaKategori();
        k1.setKategoriId(1);
        k1.setEmri("Ndihme");

        faq.addKategori(k1);
        faq.addKategori(k1);

        assertEquals(1,
                faq.getKategorite().size(),
                "Nuk duhet te lejohen kategori duplikat");

        List<String> emrat = faq.getKategoriEmer();

        assertEquals("Ndihme",
                emrat.get(0),
                "Emri i kategorise nuk eshte korrekt");
    }

    @Test
    void testKonstruktoriMeParametra() {
        FAQ faq = new FAQ(5, "Pyetje?", "Pergjigje.");

        assertEquals(5, faq.getFaqId());
        assertEquals("Pyetje?", faq.getPyetje());
        assertEquals("Pergjigje.", faq.getAnswer());
    }
}

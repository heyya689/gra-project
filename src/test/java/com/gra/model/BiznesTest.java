package com.gra.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BiznesTest {

    @Test
    void testMenaxhimiInventarit() {
        Biznes b = new Biznes(1, "Restorant Test", "L12345678A");
        Inventari item1 = new Inventari(101, "Sallatë", 5, 300.0);

        b.addInventar(item1);

        assertEquals(1, b.getInventari().size(),
                "Inventari duhet të kishte 1 element");

        List<Inventari> activeItems = b.listInventarActive();
        assertEquals(1, activeItems.size(),
                "Duhet të ishte 1 produkt aktiv");

        b.removeInventar(101);
        assertEquals(0, b.getInventari().size(),
                "Inventari duhet të ishte bosh pas fshirjes");
    }

    @Test
    void testLlogaritjaMesataresVleresimeve() {
        Biznes b = new Biznes();

        assertEquals(0.0, b.getAverageRating(),
                "Mesatarja duhet të jetë 0 kur nuk ka vlerësime");

        Vleresim v1 = new Vleresim();
        v1.setRating(5);

        Vleresim v2 = new Vleresim();
        v2.setRating(3);

        b.getVleresimet().add(v1);
        b.getVleresimet().add(v2);

        assertEquals(4.0, b.getAverageRating(),
                "Mesatarja duhet të ishte 4.0");
    }

    @Test
    void testImazhiPrimar() {
        Biznes b = new Biznes();

        BiznesImazhe img1 = new BiznesImazhe();
        img1.setUrl("foto1.jpg");
        img1.setPrimary(false);

        BiznesImazhe img2 = new BiznesImazhe();
        img2.setUrl("foto2.jpg");
        img2.setPrimary(true);

        b.addImazh(img1);
        b.addImazh(img2);

        assertNotNull(b.getPrimaryImage(),
                "Biznesi duhet të ketë imazh primar");

        assertEquals("foto2.jpg",
                b.getPrimaryImage().getUrl(),
                "Nuk u gjet imazhi primar i duhur");
    }

    @Test
    void testShtimiKategorive() {
        Biznes b = new Biznes();
        Kategori k = new Kategori(1, "Ushqim", "🍴");

        b.addKategori(k);
        b.addKategori(k);

        assertEquals(1, b.getKategorite().size(),
                "Nuk duhen lejuar kategori duplikat");
    }
}

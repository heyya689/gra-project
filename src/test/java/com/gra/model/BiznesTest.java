package com.gra.model;

import java.util.List;

public class BiznesTest {

    public static void main(String[] args) {
        System.out.println("🧪 Duke nisur testimin për Biznes.java...");

        testMenaxhimiInventarit();
        testLlogaritjaMesataresVleresimeve();
        testImazhiPrimar();
        testShtimiKategorive();

        System.out.println("\n✅ Të gjitha testet për Biznes.java kaluan me sukses!");
    }

    private static void testMenaxhimiInventarit() {
        Biznes b = new Biznes(1, "Restorant Test", "L12345678A");
        Inventari item1 = new Inventari(101, "Sallatë", 5, 300.0);

        b.addInventar(item1);

        // Testo nëse u shtua
        assert b.getInventari().size() == 1 : "Gabim: Inventari duhet të kishte 1 element";

        // Testo nëse listInventarActive punon (item1 është aktiv default)
        List<Inventari> activeItems = b.listInventarActive();
        assert activeItems.size() == 1 : "Gabim: Duhet të ishte 1 produkt aktiv";

        // Testo heqjen
        b.removeInventar(101);
        assert b.getInventari().size() == 0 : "Gabim: Inventari duhet të ishte bosh pas fshirjes";

        System.out.println("  - Testi i Inventarit: OK");
    }

    private static void testLlogaritjaMesataresVleresimeve() {
        Biznes b = new Biznes();

        // Test 1: Kur nuk ka vlerësime
        assert b.getAverageRating() == 0.0 : "Gabim: Mesatarja duhet të jetë 0 kur nuk ka vlerësime";

        // Test 2: Me vlerësime (5 dhe 3, mesatarja duhet të jetë 4.0)
        Vleresim v1 = new Vleresim();
        v1.setRating(5);
        Vleresim v2 = new Vleresim();
        v2.setRating(3);

        b.getVleresimet().add(v1);
        b.getVleresimet().add(v2);

        assert b.getAverageRating() == 4.0 : "Gabim: Mesatarja duhet të ishte 4.0";

        System.out.println("  - Testi i Vlerësimeve (Rating): OK");
    }

    private static void testImazhiPrimar() {
        Biznes b = new Biznes();
        BiznesImazhe img1 = new BiznesImazhe();
        img1.setUrl("foto1.jpg");
        img1.setPrimary(false);

        BiznesImazhe img2 = new BiznesImazhe();
        img2.setUrl("foto2.jpg");
        img2.setPrimary(true);

        b.addImazh(img1);
        b.addImazh(img2);

        // Duhet të kthejë img2 sepse është primary
        assert b.getPrimaryImage().getUrl().equals("foto2.jpg") : "Gabim: Nuk u gjet imazhi primar i duhur";

        System.out.println("  - Testi i Imazhit Primar: OK");
    }

    private static void testShtimiKategorive() {
        Biznes b = new Biznes();
        Kategori k = new Kategori(1, "Ushqim", "🍴");

        b.addKategori(k);
        b.addKategori(k); // Provo ta shtosh dy herë të njëjtën

        assert b.getKategorite().size() == 1 : "Gabim: Nuk duhen lejuar kategori duplikat";

        System.out.println("  - Testi i Kategorive: OK");
    }
}
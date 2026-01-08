package com.gra.model;

import java.util.List;

public class KategoriTest {

    public static void main(String[] args) {
        System.out.println("🧪 Duke nisur testimin për Kategori.java...");

        testMenaxhimiBizneseve();
        testDisplayNameFormat();
        testKonstruktoriDheSetter();

        System.out.println("\n✅ Të gjitha testet për Kategori.java kaluan me sukses!");
    }

    private static void testMenaxhimiBizneseve() {
        Kategori kat = new Kategori(1, "Restorant", "🍴");
        Biznes b1 = new Biznes(101, "Era", "L12345678A");

        // Testo shtimin e biznesit
        kat.addBiznes(b1);
        assert kat.getBiznesCount() == 1 : "Gabim: Numri i bizneseve duhet të jetë 1";

        // Testo parandalimin e duplikateve
        kat.addBiznes(b1);
        assert kat.getBiznesCount() == 1 : "Gabim: Nuk duhen lejuar biznese duplikat në kategori";

        // Testo nëse biznesi u shtua në listën e kategorisë
        assert kat.getBizneset().get(0).getEmri().equals("Era") : "Gabim: Biznesi i gjetur nuk është i duhuri";

        // Testo heqjen
        kat.removeBiznes(b1);
        assert kat.getBiznesCount() == 0 : "Gabim: Lista duhet të ishte bosh pas heqjes";

        System.out.println("  - Testi i Menaxhimit të Bizneseve: OK");
    }

    private static void testDisplayNameFormat() {
        // Rasti 1: Me ikonë dhe emër
        Kategori k1 = new Kategori(1, "Hotel", "🏨");
        assert k1.getDisplayName().equals("🏨 Hotel") : "Gabim: DisplayName duhet të jetë '🏨 Hotel'";

        // Rasti 2: Vetëm emër (ikona null)
        Kategori k2 = new Kategori();
        k2.setEmri("Service");
        k2.setIkona(null);
        assert k2.getDisplayName().equals("Service") : "Gabim: DisplayName duhet të jetë 'Service' kur ikona mungon";

        // Rasti 3: Ikona string bosh
        k2.setIkona("");
        assert k2.getDisplayName().equals("Service") : "Gabim: DisplayName nuk duhet të ketë hapësirë boshe në fillim";

        System.out.println("  - Testi i Display Name: OK");
    }

    private static void testKonstruktoriDheSetter() {
        Kategori kat = new Kategori();
        kat.setPershkrim("Kategori për testim");

        assert kat.getBizneset() != null : "Gabim: Lista e bizneseve duhet të inicializohet në konstruktor";
        assert kat.getBiznesCount() == 0;

        System.out.println("  - Testi i Inicializimit: OK");
    }
}
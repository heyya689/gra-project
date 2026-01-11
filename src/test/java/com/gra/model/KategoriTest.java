package com.gra.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KategoriTest {

    @Test
    void testMenaxhimiBizneseve() {
        Kategori kat = new Kategori(1, "Restorant", "🍴");
        Biznes b1 = new Biznes(101, "Era", "L12345678A");

        kat.addBiznes(b1);
        assertEquals(1, kat.getBiznesCount(),
                "Numri i bizneseve duhet të jetë 1");

        kat.addBiznes(b1);
        assertEquals(1, kat.getBiznesCount(),
                "Nuk duhen lejuar biznese duplikat në kategori");

        assertEquals("Era",
                kat.getBizneset().get(0).getEmri(),
                "Biznesi i gjetur nuk është i duhuri");

        kat.removeBiznes(b1);
        assertEquals(0, kat.getBiznesCount(),
                "Lista duhet të ishte bosh pas heqjes");
    }

    @Test
    void testDisplayNameFormat() {
        Kategori k1 = new Kategori(1, "Hotel", "🏨");
        assertEquals("🏨 Hotel",
                k1.getDisplayName(),
                "DisplayName duhet të jetë '🏨 Hotel'");

        Kategori k2 = new Kategori();
        k2.setEmri("Service");
        k2.setIkona(null);

        assertEquals("Service",
                k2.getDisplayName(),
                "DisplayName duhet të jetë 'Service' kur ikona mungon");

        k2.setIkona("");
        assertEquals("Service",
                k2.getDisplayName(),
                "DisplayName nuk duhet të ketë hapësirë boshe në fillim");
    }

    @Test
    void testKonstruktoriDheSetter() {
        Kategori kat = new Kategori();
        kat.setPershkrim("Kategori për testim");

        assertNotNull(kat.getBizneset(),
                "Lista e bizneseve duhet të inicializohet në konstruktor");

        assertEquals(0, kat.getBiznesCount(),
                "Numri i bizneseve duhet të jetë 0 në fillim");
    }
}

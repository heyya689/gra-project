package com.gra.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventariTest {

    @Test
    void testMenaxhimiStokut() {
        Inventari item = new Inventari(1, "Kafe Espresso", 10, 150.0);

        item.increaseStock(5);
        assertEquals(15, item.getSasi(),
                "Stoku duhet të ishte 15");

        item.decreaseStock(3);
        assertEquals(12, item.getSasi(),
                "Stoku duhet të ishte 12");

        int sasiaParaGabimit = item.getSasi();
        item.decreaseStock(20);

        assertEquals(sasiaParaGabimit, item.getSasi(),
                "Stoku nuk duhet të ulet nëse sasia është e pamjaftueshme");
    }

    @Test
    void testLlogaritjaVleresDheCmimit() {
        Inventari item = new Inventari(2, "Ujë 0.5L", 100, 50.0);

        assertEquals(5000.0, item.getTotalValue(),
                "Vlera totale duhet të jetë 5000.0");

        item.updatePrice(-10.0);
        assertEquals(50.0, item.getCmimi(),
                "Çmimi nuk duhet të pranojë vlera negative");

        item.updatePrice(60.0);
        assertEquals(60.0, item.getCmimi(),
                "Çmimi duhet të ishte 60.0");
    }

    @Test
    void testDisponueshmeriaDheStatusi() {
        Inventari item = new Inventari();

        item.setSasi(5);
        item.activate();

        assertTrue(item.isAvailable(),
                "Produkti duhet të jetë available");

        item.setSasi(0);
        assertFalse(item.isAvailable(),
                "Produkti nuk duhet të jetë available kur sasia është 0");

        item.setSasi(10);
        item.deactivate();

        assertFalse(item.isAvailable(),
                "Produkti nuk duhet të jetë available kur është inactive");
    }

    @Test
    void testUpdateTimestamps() {
        Inventari item = new Inventari();
        item.setUpdatedAt(null);

        item.updateStock(20);

        assertNotNull(item.getUpdatedAt(),
                "updatedAt duhet të ishte plotësuar pas updateStock");
    }
}

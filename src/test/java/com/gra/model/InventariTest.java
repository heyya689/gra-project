package com.gra.model;

import java.time.LocalDateTime;

public class InventariTest {

    public static void main(String[] args) {
        System.out.println("🧪 Duke nisur testimin për Inventari.java...");

        testMenaxhimiStokut();
        testLlogaritjaVleresDheCmimit();
        testDisponueshmeriaDheStatusi();
        testUpdateTimestamps();

        System.out.println("\n✅ Të gjitha testet për Inventari.java kaluan me sukses!");
    }

    private static void testMenaxhimiStokut() {
        Inventari item = new Inventari(1, "Kafe Espresso", 10, 150.0);

        // Testo rritjen e stokut
        item.increaseStock(5);
        assert item.getSasi() == 15 : "Gabim: Stoku duhet të ishte 15";

        // Testo uljen e stokut
        item.decreaseStock(3);
        assert item.getSasi() == 12 : "Gabim: Stoku duhet të ishte 12";

        // Testo parandalimin e uljes nën zero
        int sasiaParaGabimit = item.getSasi();
        item.decreaseStock(20); // Më shumë se sa ka
        assert item.getSasi() == sasiaParaGabimit : "Gabim: Stoku nuk duhet të ulet nëse sasia është e pamjaftueshme";

        System.out.println("  - Testi i Menaxhimit të Stokut: OK");
    }

    private static void testLlogaritjaVleresDheCmimit() {
        Inventari item = new Inventari(2, "Ujë 0.5L", 100, 50.0);

        // Testo vlerën totale (100 * 50.0)
        assert item.getTotalValue() == 5000.0 : "Gabim: Vlera totale duhet të jetë 5000.0";

        // Testo përditësimin e çmimit me vlerë negative (nuk duhet të lejohet)
        item.updatePrice(-10.0);
        assert item.getCmimi() == 50.0 : "Gabim: Çmimi nuk duhet të pranojë vlera negative";

        item.updatePrice(60.0);
        assert item.getCmimi() == 60.0 : "Gabim: Çmimi duhet të ishte 60.0";

        System.out.println("  - Testi i Vlerës dhe Çmimit: OK");
    }

    private static void testDisponueshmeriaDheStatusi() {
        Inventari item = new Inventari();
        item.setSasi(5);
        item.activate();

        // Testo nëse është available (Active + Sasi > 0)
        assert item.isAvailable() : "Gabim: Produkti duhet të jetë available";

        // Testo kur sasia shkon zero
        item.setSasi(0);
        assert !item.isAvailable() : "Gabim: Produkti nuk duhet të jetë available kur sasia është 0";

        // Testo kur deaktivohet
        item.setSasi(10);
        item.deactivate();
        assert !item.isAvailable() : "Gabim: Produkti nuk duhet të jetë available kur është inactive";

        System.out.println("  - Testi i Disponueshmërisë: OK");
    }

    private static void testUpdateTimestamps() {
        Inventari item = new Inventari();
        item.setUpdatedAt(null);

        // Çdo ndryshim duhet të thërrasë LocalDateTime.now()
        item.updateStock(20);
        assert item.getUpdatedAt() != null : "Gabim: updatedAt duhet të ishte plotësuar pas updateStock";

        System.out.println("  - Testi i Timestamps: OK");
    }
}
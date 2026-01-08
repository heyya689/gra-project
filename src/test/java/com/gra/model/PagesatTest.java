package com.gra.model;

import java.util.List;

public class PagesatTest {

    public static void main(String[] args) {
        System.out.println("🧪 Duke nisur testimin për Pagesat.java...");

        testProcesimiISukesshem();
        testLogjikaERimbursimit();
        testMenaxhimiHistorikut();
        testValidimiGjendjes();

        System.out.println("\n✅ Të gjitha testet për Pagesat.java kaluan me sukses!");
    }

    private static void testProcesimiISukesshem() {
        Pagesat pagesa = new Pagesat(1, 5000.0, "CREDIT_CARD");

        // Fillimisht duhet të jetë PENDING
        assert pagesa.isPending() : "Gabim: Pagesa e re duhet të jetë PENDING";

        boolean rezultati = pagesa.processPayment();

        assert rezultati : "Gabim: Procesimi duhet të kthente true";
        assert pagesa.isCompleted() : "Gabim: Statusi duhet të ishte COMPLETED";
        assert pagesa.getTransactionId() != null : "Gabim: TransactionID duhet të ishte gjeneruar";
        assert pagesa.getPaymentDate() != null : "Gabim: Data e pagesës duhet të ishte regjistruar";

        System.out.println("  - Testi i Procesimit: OK");
    }

    private static void testLogjikaERimbursimit() {
        Pagesat pagesa = new Pagesat(2, 2500.0, "CASH");

        // Nuk mund të bësh refund një pagesë që nuk ka përfunduar (is PENDING)
        assert !pagesa.refund() : "Gabim: Refund nuk duhet të lejohet për statusin PENDING";

        pagesa.processPayment(); // Kalon në COMPLETED
        boolean uRimbursua = pagesa.refund();

        assert uRimbursua : "Gabim: Rimbursimi duhet të ishte i suksesshëm";
        assert pagesa.isRefunded() : "Gabim: Statusi duhet të ishte REFUNDED";

        System.out.println("  - Testi i Rimbursimit (Refund): OK");
    }

    private static void testMenaxhimiHistorikut() {
        Pagesat pagesa = new Pagesat();
        pagesa.addToHistory("INITIALIZED", "Pagesa u krijua në sistem");

        pagesa.processPayment(); // Shton automatikisht historikun e dytë

        List<PagesatHistorik> historia = pagesa.getHistoriku();
        assert historia.size() == 2 : "Gabim: Historiku duhet të kishte 2 hyrje";
        assert historia.get(1).getStatus().equals("COMPLETED") : "Gabim: Hyrja e dytë e historikut duhet të ishte COMPLETED";

        System.out.println("  - Testi i Historikut: OK");
    }

    private static void testValidimiGjendjes() {
        Pagesat pagesa = new Pagesat();
        pagesa.setStatus("FAILED");

        assert pagesa.isFailed() : "Gabim: isFailed() duhet të kthente true";
        assert !pagesa.processPayment() : "Gabim: Një pagesë e dështuar nuk mund të procesohet përsëri pa u resetuar";

        System.out.println("  - Testi i Validimit të Gjendjes: OK");
    }
}
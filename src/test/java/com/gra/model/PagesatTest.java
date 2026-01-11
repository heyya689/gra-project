package com.gra.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testet për Menaxhimin e Pagesave (Pagesat.java)")
class PagesatTest {

    private Pagesat pagesa;

    @BeforeEach
    void setUp() {
        // Inicializojmë një pagesë tipike për çdo test
        pagesa = new Pagesat(1, 5000.0, "CREDIT_CARD");
    }

    @Test
    @DisplayName("Procesimi i një pagese të re")
    void testProcesimiISukesshem() {
        // Kontrolli fillestar
        assertTrue(pagesa.isPending(), "Pagesa e re duhet të ketë statusin PENDING");

        boolean rezultati = pagesa.processPayment();

        assertAll("Verifikimi i rezultateve pas procesimit",
                () -> assertTrue(rezultati, "Metoda processPayment duhet të kthejë true"),
                () -> assertTrue(pagesa.isCompleted(), "Statusi duhet të kalojë në COMPLETED"),
                () -> assertNotNull(pagesa.getTransactionId(), "Duhet të gjenerohet një Transaction ID"),
                () -> assertNotNull(pagesa.getPaymentDate(), "Data e pagesës duhet të regjistrohet")
        );
    }

    @Nested
    @DisplayName("Logjika e Rimbursimit (Refund)")
    class RefundTests {

        @Test
        @DisplayName("Dështimi i rimbursimit për pagesat PENDING")
        void testRefundPending() {
            assertFalse(pagesa.refund(), "Rimbursimi nuk duhet të lejohet nëse statusi është PENDING");
            assertFalse(pagesa.isRefunded());
        }

        @Test
        @DisplayName("Rimbursimi i suksesshëm pas përfundimit të pagesës")
        void testRefundCompleted() {
            pagesa.processPayment(); // Kalon në COMPLETED
            boolean uRimbursua = pagesa.refund();

            assertTrue(uRimbursua, "Rimbursimi duhet të kryhet me sukses për pagesat COMPLETED");
            assertTrue(pagesa.isRefunded(), "Statusi final duhet të jetë REFUNDED");
        }
    }

    @Test
    @DisplayName("Menaxhimi i Historikut të Veprimeve")
    void testMenaxhimiHistorikut() {
        pagesa.addToHistory("INITIALIZED", "Pagesa u krijua në sistem");
        pagesa.processPayment();

        List<PagesatHistorik> historia = pagesa.getHistoriku();

        assertEquals(2, historia.size(), "Historiku duhet të ketë saktësisht 2 regjistrime");
        assertEquals("COMPLETED", historia.get(1).getStatus());
    }

    @Test
    @DisplayName("Validimi i gjendjes kur pagesa dështon")
    void testValidimiGjendjes() {
        pagesa.setStatus("FAILED");

        assertAll("Integriteti i gjendjes FAILED",
                () -> assertTrue(pagesa.isFailed()),
                () -> assertFalse(pagesa.processPayment(), "Një pagesë FAILED nuk duhet të procesohet dot përsëri")
        );
    }
}
package com.gra.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testet për Menaxhimin e Rezervimeve (Rezervim.java)")
class RezervimTest {

    private Rezervim rezervim;

    @BeforeEach
    void setUp() {
        rezervim = new Rezervim();
    }

    @Test
    @DisplayName("Cikli i Jetës së Statusit: PENDING -> CONFIRMED -> COMPLETED")
    void testRrjedhaEStatusit() {
        rezervim.create();
        assertEquals("PENDING", rezervim.getStatus());

        rezervim.confirm();
        assertEquals("CONFIRMED", rezervim.getStatus());

        rezervim.complete();
        assertEquals("COMPLETED", rezervim.getStatus());

        // Siguria: Tentativa për anullim pas përfundimit
        rezervim.cancel();
        assertEquals("COMPLETED", rezervim.getStatus(), "Një rezervim COMPLETED nuk duhet të ndryshojë status");
    }

    @Nested
    @DisplayName("Rregullat e Anullimit (24-orëshi)")
    class CancellationRules {

        @Test
        @DisplayName("Lejimi i anullimit 48 orë para")
        void testAnullimiLejuar() {
            rezervim.setStatus("CONFIRMED");
            rezervim.setData(LocalDateTime.now().plusHours(48));
            assertTrue(rezervim.canBeCancelled(), "Anullimi duhet të jetë i mundur 2 ditë para");
        }

        @Test
        @DisplayName("Ndalimi i anullimit 2 orë para")
        void testAnullimiNaluar() {
            rezervim.setStatus("CONFIRMED");
            rezervim.setData(LocalDateTime.now().plusHours(2));
            assertFalse(rezervim.canBeCancelled(), "Anullimi duhet të bllokohet nëse jemi brenda 24 orëve");
        }
    }

    @Test
    @DisplayName("Integrimi: Anullimi i Rezervimit duhet të rimbursojë Pagesën")
    void testIntegrimiIPageses() {
        // Përgatitja e pagesës
        Pagesat pagesa = new Pagesat(1, 2000.0, "CARD");
        pagesa.processPayment(); // Kalon në COMPLETED

        rezervim.setPagesa(pagesa);
        rezervim.setStatus("CONFIRMED");
        rezervim.setData(LocalDateTime.now().plusHours(30)); // Brenda kohës së lejuar

        // Veprimi
        rezervim.cancel();

        // Verifikimi i dyfishtë (Cross-Object Assertion)
        assertAll("Integriteti i lidhjes Rezervim-Pagesë",
                () -> assertEquals("CANCELLED", rezervim.getStatus()),
                () -> assertEquals("REFUNDED", pagesa.getStatus(), "Pagesa duhet të kalonte automatikisht në REFUNDED")
        );
    }

    @Test
    @DisplayName("Verifikimi i Statusit Aktiv")
    void testStatusetAktive() {
        rezervim.setStatus("PENDING");
        assertTrue(rezervim.isActive());

        rezervim.setStatus("CONFIRMED");
        assertTrue(rezervim.isActive());

        rezervim.setStatus("CANCELLED");
        assertFalse(rezervim.isActive(), "Statusi CANCELLED nuk duhet të konsiderohet aktiv");
    }
}
package com.gra.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testet për Historikun e Pagesave")
class PagesatHistorikTest {

    private PagesatHistorik historik;

    @BeforeEach
    void setUp() {
        historik = new PagesatHistorik();
    }

    @ParameterizedTest
    @ValueSource(strings = {"COMPLETED", "APPROVED"})
    @DisplayName("Statuset që duhet të njihen si SUKSES")
    void testIdentifikimiStatusitSukses(String status) {
        PagesatHistorik h = new PagesatHistorik(status, "Mesazh testimi");
        assertTrue(h.isSuccess(), "Statusi " + status + " duhet të njihet si sukses");
        assertFalse(h.isFailure(), "Statusi i suksesshëm nuk duhet të jetë failure");
    }

    @ParameterizedTest
    @ValueSource(strings = {"FAILED", "DECLINED", "ERROR"})
    @DisplayName("Statuset që duhet të njihen si DËSHTIM")
    void testIdentifikimiStatusitDeshtim(String status) {
        PagesatHistorik h = new PagesatHistorik(status, "Mesazh gabimi");
        assertTrue(h.isFailure(), "Statusi " + status + " duhet të njihet si dështim");
        assertFalse(h.isSuccess(), "Statusi i dështuar nuk duhet të jetë success");
    }

    @Test
    @DisplayName("Verifikimi i inicializimit automatik të datës")
    void testInicializimiIDates() {
        assertNotNull(historik.getData(), "Data duhet të inicializohej automatikisht në konstruktor");

        // Kontrollojmë që data e krijuar është shumë afër kohës aktuale (brenda 2 sekondave)
        LocalDateTime tani = LocalDateTime.now();
        assertTrue(historik.getData().isBefore(tani.plusSeconds(1)), "Data nuk duhet të jetë në të ardhmen");
        assertTrue(historik.getData().isAfter(tani.minusSeconds(2)), "Data është shumë e vjetër");
    }

    @Test
    @DisplayName("Testimi i konstruktorit me parametra")
    void testKonstruktoret() {
        String statusi = "PENDING";
        String mesazhi = "Duke pritur konfirmimin";

        PagesatHistorik h = new PagesatHistorik(statusi, mesazhi);

        assertAll("Verifikimi i atributeve të konstruktorit",
                () -> assertEquals(statusi, h.getStatus()),
                () -> assertEquals(mesazhi, h.getMesazh()),
                () -> assertNotNull(h.getData())
        );
    }
}
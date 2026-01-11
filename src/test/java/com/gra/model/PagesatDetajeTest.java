package com.gra.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testet për Modelin PagesatDetaje")
class PagesatDetajeTest {

    private PagesatDetaje detaje;

    @BeforeEach
    void setUp() {
        // Inicializojmë objektin para çdo testi për të pasur një gjendje të pastër
        detaje = new PagesatDetaje();
    }

    @Test
    @DisplayName("Validimi i Referencës - Raste të ndryshme")
    void testValidimiReferences() {
        // Rasti 1: Referencë shumë e shkurtër
        detaje.setReference("12345");
        assertFalse(detaje.validateReference(), "Referenca duhet të jetë të paktën 10 karaktere");

        // Rasti 2: Referencë e saktë
        detaje.setReference("REF-2024-ALB-99");
        assertTrue(detaje.validateReference(), "Referenca e saktë duhet të pranohet");

        // Rasti 3: Referencë null
        detaje.setReference(null);
        assertFalse(detaje.validateReference(), "Referenca null nuk duhet të jetë e vlefshme");
    }

    @Test
    @DisplayName("Interpretimi i përgjigjeve nga Gateway")
    void testInterpretimiPergjigjesGateway() {
        // Testo SUCCESSFUL
        detaje.setGatewayResponse("Transaction SUCCESSFUL");
        assertTrue(detaje.isResponseSuccessful(), "Duhet të interpretonte SUCCESSFUL si sukses");

        // Testo APPROVED
        detaje.setGatewayResponse("payment approved");
        assertTrue(detaje.isResponseSuccessful(), "Duhet të interpretonte approved si sukses");

        // Testo FAILED
        detaje.setGatewayResponse("ERROR: Insufficient funds");
        assertFalse(detaje.isResponseSuccessful(), "Një gabim nuk duhet të njihet si sukses");
    }

    @Test
    @DisplayName("Verifikimi i Timestamps dhe Atributeve Teknike")
    void testTimestampsDheAtributet() {
        // Verifikojmë që createdAt është null fillimisht
        assertNull(detaje.getCreatedAt());

        // Verifikojmë inicializimin automatik pas përgjigjes
        detaje.setGatewayResponse("OK");
        assertNotNull(detaje.getCreatedAt(), "createdAt duhet të inicializohej automatikisht");

        // Verifikojmë atributet teknike
        detaje.setCardLastFour("4242");
        detaje.setIpAddress("192.168.1.1");

        assertEquals("4242", detaje.getCardLastFour());
        assertEquals("192.168.1.1", detaje.getIpAddress());
    }
}
package com.gra.model;

public class PagesatDetajeTest {

    public static void main(String[] args) {
        System.out.println("🧪 Duke nisur testimin për PagesatDetaje.java...");

        testValidimiReferences();
        testInterpretimiPergjigjesGateway();
        testTimestampsDheAtributet();

        System.out.println("\n✅ Të gjitha testet për PagesatDetaje.java kaluan me sukses!");
    }

    private static void testValidimiReferences() {
        PagesatDetaje detaje = new PagesatDetaje();

        // Rasti 1: Referencë shumë e shkurtër
        detaje.setReference("12345");
        assert !detaje.validateReference() : "Gabim: Referenca duhet të jetë të paktën 10 karaktere";

        // Rasti 2: Referencë e saktë
        detaje.setReference("REF-2024-ALB-99");
        assert detaje.validateReference() : "Gabim: Referenca e saktë nuk u pranua";

        // Rasti 3: Referencë null
        detaje.setReference(null);
        assert !detaje.validateReference() : "Gabim: Referenca null nuk duhet të jetë e vlefshme";

        System.out.println("  - Testi i Validimit të Referencës: OK");
    }

    private static void testInterpretimiPergjigjesGateway() {
        PagesatDetaje detaje = new PagesatDetaje();

        // Testo SUCCESS
        detaje.setGatewayResponse("Transaction SUCCESSFUL");
        assert detaje.isResponseSuccessful() : "Gabim: Duhet të interpretonte SUCCESSFUL si sukses";

        // Testo APPROVED (me shkronja të vogla për të testuar toUpperCase)
        detaje.setGatewayResponse("payment approved");
        assert detaje.isResponseSuccessful() : "Gabim: Duhet të interpretonte approved si sukses";

        // Testo FAILED
        detaje.setGatewayResponse("ERROR: Insufficient funds");
        assert !detaje.isResponseSuccessful() : "Gabim: Një gabim nuk duhet të njihet si sukses";

        System.out.println("  - Testi i Përgjigjes së Gateway: OK");
    }

    private static void testTimestampsDheAtributet() {
        PagesatDetaje detaje = new PagesatDetaje();

        // Verifikojmë që createdAt vendoset kur marrim përgjigjen e parë
        assert detaje.getCreatedAt() == null;
        detaje.setGatewayResponse("OK");
        assert detaje.getCreatedAt() != null : "Gabim: createdAt duhet të inicializohej automatikisht";

        // Verifikojmë atributet teknike
        detaje.setCardLastFour("4242");
        detaje.setIpAddress("192.168.1.1");
        assert detaje.getCardLastFour().equals("4242");

        System.out.println("  - Testi i Atributeve Teknike: OK");
    }
}
package com.gra.model;

import java.time.LocalDateTime;

public class NotifikimeTest {

    public static void main(String[] args) {
        System.out.println("🧪 Duke nisur testimin për Notifikime.java...");

        testStatusiLeximit();
        testPreviewMesazhit();
        testNjoftimetEFundit();
        testKonstruktoriDheDergimi();

        System.out.println("\n✅ Të gjitha testet për Notifikime.java kaluan me sukses!");
    }

    private static void testStatusiLeximit() {
        Notifikime n = new Notifikime();

        // 1. Default duhet të jetë false (i palexuar)
        assert !n.isLexuar() : "Gabim: Njoftimi i ri duhet të jetë i palexuar";

        // 2. Mark as read
        n.markAsRead();
        assert n.isLexuar() : "Gabim: Njoftimi duhet të jetë i lexuar";

        // 3. Mark as unread
        n.markAsUnread();
        assert !n.isLexuar() : "Gabim: Njoftimi duhet të kthehej në i palexuar";

        System.out.println("  - Testi i Statusit të Leximit: OK");
    }

    private static void testPreviewMesazhit() {
        Notifikime n = new Notifikime();

        // Rasti 1: Mesazh i shkurtër (nën 50 karaktere)
        n.setMesazh("Rezervimi juaj u konfirmua.");
        assert n.getPreview().equals("Rezervimi juaj u konfirmua.") : "Gabim: Preview i shkurtër nuk duhet të ndryshojë";

        // Rasti 2: Mesazh i gjatë (mbi 50 karaktere)
        String mesazhIGjate = "Ky është një njoftim shumë i rëndësishëm që kërkon vëmendjen tuaj të menjëhershme.";
        n.setMesazh(mesazhIGjate);

        String preview = n.getPreview();
        assert preview.length() == 50 : "Gabim: Preview duhet të jetë saktësisht 50 karaktere";
        assert preview.endsWith("...") : "Gabim: Preview i gjatë duhet të përfundojë me '...'";

        System.out.println("  - Testi i Preview-t: OK");
    }

    private static void testNjoftimetEFundit() {
        Notifikime n = new Notifikime();

        // Rasti 1: Njoftimi i sapokrijuar duhet të jetë 'recent'
        assert n.isRecent() : "Gabim: Njoftimi i sapokrijuar duhet të jetë recent";

        // Rasti 2: Njoftim i vjetër (p.sh. para 2 ditësh)
        n.setData(LocalDateTime.now().minusDays(2));
        assert !n.isRecent() : "Gabim: Njoftimi i krijuar para 48 orësh nuk duhet të jetë recent";

        System.out.println("  - Testi i Njoftimeve të Fundit (isRecent): OK");
    }

    private static void testKonstruktoriDheDergimi() {
        User user = new User();
        user.setEmail("klienti@gmail.com");

        Notifikime n = new Notifikime(10, user, "Mirësevini", "Llogaria u krijua.");

        assert n.getNjoftimId() == 10;
        assert n.getUser().getEmail().equals("klienti@gmail.com");

        n.send(); // Verifikojmë printimin në konsolë

        System.out.println("  - Testi i Konstruktorit dhe Dërgimit: OK");
    }
}
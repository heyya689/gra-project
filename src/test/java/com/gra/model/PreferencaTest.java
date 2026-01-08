package com.gra.model;

import java.time.LocalDateTime;

public class PreferencaTest {

    public static void main(String[] args) {
        System.out.println("🧪 Duke nisur testimin për Preferenca.java...");

        testVleratFillestare();
        testLogjikaENjoftimeve();
        testNdryshimiGjuhesDheTeme();
        testUpdateTimestamps();

        System.out.println("\n✅ Të gjitha testet për Preferenca.java kaluan me sukses!");
    }

    private static void testVleratFillestare() {
        Preferenca pref = new Preferenca();

        // Verifikojmë vlerat default të përcaktuara në konstruktor
        assert pref.getGjuha().equals("sq") : "Gabim: Gjuha default duhet të jetë 'sq'";
        assert pref.getTema().equals("light") : "Gabim: Tema default duhet të jetë 'light'";
        assert pref.isNjoftimeAktive() : "Gabim: Njoftimet duhet të jenë aktive default";
        assert pref.isEmailNotifications() : "Gabim: Email notifications duhet të jenë true default";
        assert !pref.isSmsNotifications() : "Gabim: SMS notifications duhet të jenë false default";

        System.out.println("  - Testi i Vlerave Fillestare: OK");
    }

    private static void testLogjikaENjoftimeve() {
        Preferenca pref = new Preferenca();

        // Testo disable/enable
        pref.disableNotifications();
        assert !pref.isNjoftimeAktive() : "Gabim: Njoftimet duhet të ishin false";

        pref.enableNotifications();
        assert pref.isNjoftimeAktive() : "Gabim: Njoftimet duhet të ishin true";

        // Testo toggle për Email
        boolean statusiFillestar = pref.isEmailNotifications();
        pref.toggleEmailNotifications();
        assert pref.isEmailNotifications() != statusiFillestar : "Gabim: Toggle Email nuk funksionoi";

        System.out.println("  - Testi i Logjikës së Njoftimeve: OK");
    }

    private static void testNdryshimiGjuhesDheTeme() {
        Preferenca pref = new Preferenca();

        // Testo ndryshimin e gjuhës
        pref.changeLanguage("en");
        assert pref.getGjuha().equals("en") : "Gabim: Gjuha nuk u ndryshua në 'en'";

        // Testo ndryshimin e temës (vetëm light/dark lejohen)
        pref.changeTheme("dark");
        assert pref.getTema().equals("dark") : "Gabim: Tema nuk u ndryshua në 'dark'";

        pref.changeTheme("blue-theme"); // Vlerë e palidhur
        assert pref.getTema().equals("dark") : "Gabim: Tema nuk duhet të ndryshojë me vlerë të jashtme";

        System.out.println("  - Testi i Gjuhës dhe Temës: OK");
    }

    private static void testUpdateTimestamps() {
        Preferenca pref = new Preferenca();
        pref.setUpdatedAt(null);

        pref.toggleSmsNotifications();
        assert pref.getUpdatedAt() != null : "Gabim: updatedAt duhet të ishte plotësuar pas ndryshimit";

        System.out.println("  - Testi i Timestamps: OK");
    }
}
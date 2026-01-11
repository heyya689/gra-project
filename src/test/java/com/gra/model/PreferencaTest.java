package com.gra.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testet për Preferencat e Përdoruesit (Preferenca.java)")
class PreferencaTest {

    private Preferenca pref;

    @BeforeEach
    void setUp() {
        pref = new Preferenca();
    }

    @Test
    @DisplayName("Verifikimi i vlerave fillestare (Default)")
    void testVleratFillestare() {
        assertAll("Vlerat default të sistemit",
                () -> assertEquals("sq", pref.getGjuha(), "Gjuha duhet të jetë shqip (sq)"),
                () -> assertEquals("light", pref.getTema(), "Tema duhet të jetë 'light'"),
                () -> assertTrue(pref.isNjoftimeAktive(), "Njoftimet duhet të jenë aktive"),
                () -> assertTrue(pref.isEmailNotifications(), "Email duhet të jetë aktiv"),
                () -> assertFalse(pref.isSmsNotifications(), "SMS duhet të jetë jo-aktiv")
        );
    }

    @Nested
    @DisplayName("Logjika e Njoftimeve")
    class NotificationTests {

        @Test
        @DisplayName("Aktivizimi dhe çaktivizimi i njoftimeve globale")
        void testStatusiGlobal() {
            pref.disableNotifications();
            assertFalse(pref.isNjoftimeAktive());

            pref.enableNotifications();
            assertTrue(pref.isNjoftimeAktive());
        }

        @Test
        @DisplayName("Toggle për njoftimet me Email")
        void testToggleEmail() {
            boolean statusiFillestar = pref.isEmailNotifications();
            pref.toggleEmailNotifications();
            assertNotEquals(statusiFillestar, pref.isEmailNotifications(), "Statusi i Email duhet të ndryshojë");
        }
    }

    @Test
    @DisplayName("Validimi i ndryshimit të Gjuhës dhe Temës")
    void testNdryshimiGjuhesDheTeme() {
        // Testi i gjuhës
        pref.changeLanguage("en");
        assertEquals("en", pref.getGjuha());

        // Testi i temës (Logjika e validimit)
        pref.changeTheme("dark");
        assertEquals("dark", pref.getTema());

        // Testo që nuk pranohen vlera invalide
        pref.changeTheme("blue-theme");
        assertEquals("dark", pref.getTema(), "Tema nuk duhet të ndryshojë me një vlerë të jashtme (blue-theme)");
    }

    @Test
    @DisplayName("Përditësimi automatik i kohës (updatedAt)")
    void testUpdateTimestamps() {
        pref.setUpdatedAt(null); // Force reset

        pref.toggleSmsNotifications();

        assertNotNull(pref.getUpdatedAt(), "Timestamp 'updatedAt' duhet të mbushet automatikisht pas çdo ndryshimi");
    }
}
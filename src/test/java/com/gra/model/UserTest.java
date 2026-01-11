package com.gra.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testet për Klasën User (Përdoruesi)")
class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        // Krijojmë një përdorues standard para çdo testi
        user = new User(1, "Artan", "artan@example.com", "Fjalekalimi123", new ArrayList<>());
    }

    @ParameterizedTest
    @CsvSource({
            "artan@example.com, Fjalekalimi123, true",
            "artan@example.com, gabim, false",
            "tjeter@example.com, Fjalekalimi123, false",
            "'', '', false"
    })
    @DisplayName("Autentikimi me kombinime të ndryshme")
    void testAutentikimiLogin(String email, String password, boolean rezultatiPritur) {
        assertEquals(rezultatiPritur, user.login(email, password),
                "Login dështoi për kredencialet: " + email + " / " + password);
    }

    @Test
    @DisplayName("Përditësimi i profilit dhe validimi i fushave")
    void testUpdateProfile() {
        // Ndryshimi i rregullt
        user.updateProfile("Blerina K.", "blerina.new@test.com");

        assertAll("Përditësimi i suksesshëm",
                () -> assertEquals("Blerina K.", user.getName()),
                () -> assertEquals("blerina.new@test.com", user.getEmail()),
                () -> assertNotNull(user.getUpdatedAt(), "Timestamp duhet të gjenerohet")
        );

        // Testo mbrojtjen nga overwrite me vlera boshe
        user.updateProfile("", null);
        assertEquals("Blerina K.", user.getName(), "Emri nuk duhet të mbishkruhet me vlerë boshe");
    }

    @Nested
    @DisplayName("Menaxhimi i Roleve (RBAC)")
    class RoleManagement {

        @Test
        @DisplayName("Shtimi dhe kontrolli i roleve (Case-Insensitive)")
        void testAddAndHasRole() {
            Role adminRole = new Role(1, "ADMIN");
            user.addRole(adminRole);

            assertAll("Kontrolli i roleve",
                    () -> assertTrue(user.hasRole("ADMIN")),
                    () -> assertTrue(user.hasRole("admin"), "Duhet të jetë case-insensitive"),
                    () -> assertFalse(user.hasRole("EDITOR"), "Përdoruesi nuk duhet të ketë role që s'i janë dhënë")
            );
        }

        @Test
        @DisplayName("Heqja e rolit nga përdoruesi")
        void testRemoveRole() {
            Role role = new Role(2, "EDITOR");
            user.addRole(role);
            user.removeRole(role);
            assertFalse(user.hasRole("EDITOR"));
        }
    }

    @Test
    @DisplayName("Inicializimi i listave (Null-Safe Check)")
    void testInicializimiListave() {
        User userRi = new User();

        assertAll("Inicializimi i koleksioneve në konstruktor",
                () -> assertNotNull(userRi.getRoles()),
                () -> assertNotNull(userRi.getRezervimet()),
                () -> assertNotNull(userRi.getPagesat()),
                () -> assertNotNull(userRi.getVleresimet()),
                () -> assertNotNull(userRi.getNotifikimet()),
                () -> assertNotNull(userRi.getKontaktet())
        );
    }
}
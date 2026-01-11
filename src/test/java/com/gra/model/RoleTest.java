package com.gra.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testet për Menaxhimin e Roleve (Role.java)")
class RoleTest {

    private Role role;
    private User testUser;

    @BeforeEach
    void setUp() {
        role = new Role(1, "ADMIN");
        testUser = new User();
        testUser.setUserId(101);
        testUser.setEmail("admin@test.com");
    }

    @Test
    @DisplayName("Menaxhimi i Përdoruesve në Rol (Shtim/Heqje/Duplikate)")
    void testMenaxhimiPerdoruesve() {
        // Shtimi i parë
        role.assignToUser(testUser);
        assertEquals(1, role.getUsers().size(), "Roli duhet të ketë 1 përdorues të lidhur");

        // Testi i duplikateve (Idempotency)
        role.assignToUser(testUser);
        assertEquals(1, role.getUsers().size(), "Nuk duhen lejuar përdorues duplikat (Set-like behavior)");

        // Heqja e përdoruesit
        role.removeFromUser(testUser);
        assertTrue(role.getUsers().isEmpty(), "Lista e përdoruesve duhet të mbetet bosh pas heqjes");
    }

    @ParameterizedTest
    @ValueSource(strings = {"POST_CREATE", "POST_EDIT", "POST_DELETE"})
    @DisplayName("Verifikimi i Permissioneve ekzistuese")
    void testKontrolliIPermissionevePozitive(String permission) {
        role.setPermissions("POST_CREATE,POST_EDIT,POST_DELETE");
        assertTrue(role.hasPermission(permission), "Roli duhet të ketë lejen: " + permission);
    }

    @Test
    @DisplayName("Kontrolli i Permissioneve (Negative dhe Edge Cases)")
    void testKontrolliIPermissioneveNegative() {
        role.setPermissions("POST_CREATE,POST_EDIT");

        assertAll("Verifikimi i sigurisë së lejeve",
                () -> assertFalse(role.hasPermission("ADMIN_ACCESS"), "Nuk duhet të ketë leje që nuk i është dhënë"),
                () -> {
                    role.setPermissions(null);
                    assertFalse(role.hasPermission("ANY"), "Nuk duhet të shkaktojë NullPointerException nëse permissions janë null");
                }
        );
    }

    @Test
    @DisplayName("Integriteti i Konstruktorit dhe Inicializimit")
    void testKonstruktoriDheAtributet() {
        Role manager = new Role(2, "MANAGER");
        manager.setDescription("Menaxhon rezervimet");

        assertAll("Atributet bazë të Rolit",
                () -> assertEquals(2, manager.getRoleId()),
                () -> assertEquals("MANAGER", manager.getEmri()),
                () -> assertNotNull(manager.getUsers(), "Lista e përdoruesve duhet të inicializohet automatikisht (jo null)")
        );
    }
}
package com.gra.model;

import java.util.List;

public class RoleTest {

    public static void main(String[] args) {
        System.out.println("🧪 Duke nisur testimin për Role.java...");

        testMenaxhimiPerdoruesve();
        testKontrolliIPermissioneve();
        testKonstruktoriDheAtributet();

        System.out.println("\n✅ Të gjitha testet për Role.java kaluan me sukses!");
    }

    private static void testMenaxhimiPerdoruesve() {
        Role admin = new Role(1, "ADMIN");
        User user1 = new User();
        user1.setUserId(101);
        user1.setEmail("admin@test.com");

        // Testo lidhjen role -> user
        admin.assignToUser(user1);
        assert admin.getUsers().size() == 1 : "Gabim: Roli duhet të kishte 1 përdorues";

        // Testo parandalimin e duplikateve
        admin.assignToUser(user1);
        assert admin.getUsers().size() == 1 : "Gabim: Nuk duhen lejuar përdorues duplikat në rol";

        // Testo heqjen e përdoruesit
        admin.removeFromUser(user1);
        assert admin.getUsers().isEmpty() : "Gabim: Lista e përdoruesve duhet të ishte bosh";

        System.out.println("  - Testi i Menaxhimit të Përdoruesve: OK");
    }

    private static void testKontrolliIPermissioneve() {
        Role editor = new Role();
        editor.setPermissions("POST_CREATE,POST_EDIT,POST_DELETE");

        // Testo permission ekzistues
        assert editor.hasPermission("POST_CREATE") : "Gabim: Duhet të kishte permission POST_CREATE";
        assert editor.hasPermission("POST_DELETE") : "Gabim: Duhet të kishte permission POST_DELETE";

        // Testo permission që nuk ekziston
        assert !editor.hasPermission("ADMIN_ACCESS") : "Gabim: Nuk duhet të kishte permission ADMIN_ACCESS";

        // Testo rastin kur permissions janë null
        Role ghost = new Role();
        ghost.setPermissions(null);
        assert !ghost.hasPermission("ANY") : "Gabim: Nuk duhet të shkaktonte error me null permissions";

        System.out.println("  - Testi i Kontrollit të Permissioneve: OK");
    }

    private static void testKonstruktoriDheAtributet() {
        Role role = new Role(2, "MANAGER");
        role.setDescription("Menaxhon rezervimet");

        assert role.getRoleId() == 2;
        assert role.getEmri().equals("MANAGER");
        assert role.getUsers() != null : "Gabim: Lista e përdoruesve duhet të inicializohej";

        System.out.println("  - Testi i Konstruktorit dhe Atributeve: OK");
    }
}
package com.gra.model;

import java.util.ArrayList;
import java.util.List;

public class UserTest {

    public static void main(String[] args) {
        System.out.println("🧪 Duke nisur testimin për User.java...");

        testAutentikimiLogin();
        testUpdateProfile();
        testMenaxhimiRoleve();
        testInicializimiListave();

        System.out.println("\n✅ Të gjitha testet për User.java kaluan me sukses!");
    }

    private static void testAutentikimiLogin() {
        User user = new User(1, "Artan", "artan@example.com", "Fjalekalimi123", new ArrayList<>());

        // Testo login me kredenciale të sakta
        assert user.login("artan@example.com", "Fjalekalimi123") : "Gabim: Login duhet të ishte i suksesshëm";

        // Testo login me fjalëkalim të gabuar
        assert !user.login("artan@example.com", "gabim") : "Gabim: Login nuk duhet të lejohej me password të gabuar";

        // Testo login me email të gabuar
        assert !user.login("tjeter@example.com", "Fjalekalimi123") : "Gabim: Login nuk duhet të lejohej me email të gabuar";

        System.out.println("  - Testi i Autentikimit: OK");
    }

    private static void testUpdateProfile() {
        User user = new User();
        user.setName("Blerina");
        user.setEmail("blerina@test.com");

        // Ndrysho emrin dhe emailin
        user.updateProfile("Blerina K.", "blerina.new@test.com");

        assert user.getName().equals("Blerina K.") : "Gabim: Emri nuk u përditësua";
        assert user.getEmail().equals("blerina.new@test.com") : "Gabim: Emaili nuk u përditësua";
        assert user.getUpdatedAt() != null : "Gabim: updatedAt duhet të ishte plotësuar";

        // Testo që inputet boshe nuk e mbishkruajnë të dhënën ekzistuese
        user.updateProfile("", null);
        assert user.getName().equals("Blerina K.") : "Gabim: Emri nuk duhet të bëhej bosh";

        System.out.println("  - Testi i Përditësimit të Profilit: OK");
    }

    private static void testMenaxhimiRoleve() {
        User user = new User();
        Role adminRole = new Role(1, "ADMIN");
        Role editorRole = new Role(2, "EDITOR");

        // Shto role
        user.addRole(adminRole);
        user.addRole(editorRole);

        // Testo hasRole (duhet të jetë case-insensitive sipas kodit tënd)
        assert user.hasRole("admin") : "Gabim: Përdoruesi duhet të kishte rolin ADMIN";
        assert user.hasRole("EDITOR") : "Gabim: Përdoruesi duhet të kishte rolin EDITOR";

        // Testo heqjen e rolit
        user.removeRole(adminRole);
        assert !user.hasRole("ADMIN") : "Gabim: Roli ADMIN duhet të ishte hequr";

        System.out.println("  - Testi i Menaxhimit të Roleve: OK");
    }

    private static void testInicializimiListave() {
        User user = new User();

        // Verifikojmë që të gjitha listat janë inicializuar në konstruktor (jo null)
        assert user.getRoles() != null;
        assert user.getRezervimet() != null;
        assert user.getPagesat() != null;
        assert user.getVleresimet() != null;
        assert user.getNotifikimet() != null;
        assert user.getKontaktet() != null;

        System.out.println("  - Testi i Inicializimit të Listave: OK");
    }
}
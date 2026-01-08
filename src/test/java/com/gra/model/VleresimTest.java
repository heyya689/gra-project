package com.gra.model;

import java.time.LocalDateTime;

public class VleresimTest {

    public static void main(String[] args) {
        System.out.println("🧪 Duke nisur testimin për Vleresim.java...");

        testValidimiRating();
        testGjenerimiYjeve();
        testModifikimiDheModerimi();
        testValidimiObjektit();

        System.out.println("\n✅ Të gjitha testet për Vleresim.java kaluan me sukses!");
    }

    private static void testValidimiRating() {
        Vleresim v = new Vleresim();

        // Testo vlerën valide
        v.setRating(4);
        assert v.getRating() == 4 : "Gabim: Rating duhet të ishte 4";

        // Testo vlerat jashtë kufijve (nuk duhet të ndryshojnë rating-un aktual)
        v.setRating(6);
        assert v.getRating() == 4 : "Gabim: Rating nuk duhet të pranojë vlerën 6";

        v.setRating(0);
        assert v.getRating() == 4 : "Gabim: Rating nuk duhet të pranojë vlerën 0";

        System.out.println("  - Testi i Kufijve të Rating: OK");
    }

    private static void testGjenerimiYjeve() {
        Vleresim v = new Vleresim();

        v.setRating(3);
        assert v.getRatingStars().equals("★★★☆☆") : "Gabim: Formatimi i yjeve për 3 nuk është i saktë";

        v.setRating(5);
        assert v.getRatingStars().equals("★★★★★") : "Gabim: Formatimi i yjeve për 5 nuk është i saktë";

        System.out.println("  - Testi i Gjenerimit të Yjeve: OK");
    }

    private static void testModifikimiDheModerimi() {
        Vleresim v = new Vleresim();
        v.setKoment("Shumë mirë");
        v.approve();

        assert v.isApproved() : "Gabim: Vlerësimi duhet të ishte i miratuar";

        // Testo editimin
        v.edit("Shumë mirë, do vij përsëri!", 5);
        assert v.getRating() == 5;
        assert v.getKoment().contains("do vij përsëri");
        assert v.getUpdatedAt() != null : "Gabim: updatedAt duhet të përditësohej";

        // Testo fshirjen logjike
        v.delete();
        assert v.getKoment().equals("[I fshirë]") : "Gabim: Komenti duhet të ishte zëvendësuar";
        assert !v.isApproved() : "Gabim: Vlerësimi i fshirë nuk duhet të jetë i miratuar";

        System.out.println("  - Testi i Moderimit dhe Editimit: OK");
    }

    private static void testValidimiObjektit() {
        Vleresim v = new Vleresim();

        // Pa user dhe biznes nuk duhet të jetë valid
        assert !v.isValid() : "Gabim: Vlerësimi nuk duhet të jetë valid pa User dhe Biznes";

        v.setUser(new User());
        v.setBiznes(new Biznes());
        v.setRating(3);
        v.setKoment("Provë");

        assert v.isValid() : "Gabim: Vlerësimi duhet të ishte valid";

        System.out.println("  - Testi i Validimit të Integritetit: OK");
    }
}
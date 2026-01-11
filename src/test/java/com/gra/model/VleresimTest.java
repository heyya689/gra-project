package com.gra.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testet për Klasën Vlerësim (Ratings & Reviews)")
class VleresimTest {

    private Vleresim vleresim;

    @BeforeEach
    void setUp() {
        vleresim = new Vleresim();
    }

    @Test
    @DisplayName("Validimi i Kufijve të Rating (1-5)")
    void testValidimiRating() {
        vleresim.setRating(4);
        assertEquals(4, vleresim.getRating());

        // Testo vlerat jashtë kufijve - nuk duhet të ndryshojnë vlerën aktuale (4)
        vleresim.setRating(6);
        assertEquals(4, vleresim.getRating(), "Rating nuk duhet të pranojë vlera mbi 5");

        vleresim.setRating(0);
        assertEquals(4, vleresim.getRating(), "Rating nuk duhet të pranojë vlera nën 1");
    }

    @ParameterizedTest
    @CsvSource({
            "3, ★★★☆☆",
            "5, ★★★★★",
            "1, ★☆☆☆☆"
    })
    @DisplayName("Gjenerimi vizual i yjeve sipas rating-ut")
    void testGjenerimiYjeve(int rating, String yjetEPritura) {
        vleresim.setRating(rating);
        assertEquals(yjetEPritura, vleresim.getRatingStars());
    }

    @Test
    @DisplayName("Logjika e Moderimit: Miratimi, Editimi dhe Fshirja")
    void testModifikimiDheModerimi() {
        vleresim.setKoment("Shumë mirë");
        vleresim.approve();

        assertTrue(vleresim.isApproved(), "Vlerësimi duhet të jetë i miratuar");

        // Testo editimin
        vleresim.edit("Shumë mirë, do vij përsëri!", 5);
        assertAll("Verifikimi i editimit",
                () -> assertEquals(5, vleresim.getRating()),
                () -> assertTrue(vleresim.getKoment().contains("do vij përsëri")),
                () -> assertNotNull(vleresim.getUpdatedAt())
        );

        // Testo fshirjen logjike (Soft Delete)
        vleresim.delete();
        assertAll("Verifikimi i fshirjes logjike",
                () -> assertEquals("[I fshirë]", vleresim.getKoment()),
                () -> assertFalse(vleresim.isApproved(), "Vlerësimi i fshirë nuk duhet të jetë i miratuar")
        );
    }

    @Test
    @DisplayName("Integriteti i Objektit (Validimi i lidhjeve)")
    void testValidimiObjektit() {
        // Rasti negativ: pa user dhe biznes
        assertFalse(vleresim.isValid(), "Nuk duhet të jetë valid pa entitetet e lidhura");

        // Rasti pozitiv
        vleresim.setUser(new User());
        vleresim.setBiznes(new Biznes());
        vleresim.setRating(3);
        vleresim.setKoment("Provë");

        assertTrue(vleresim.isValid(), "Vlerësimi duhet të plotësojë kushtet minimale të validimit");
    }
}
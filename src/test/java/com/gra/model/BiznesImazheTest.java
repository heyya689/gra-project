package com.gra.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BiznesImazheTest {

    @Test
    void testFullUrlGeneration() {
        BiznesImazhe img1 = new BiznesImazhe();
        img1.setUrl("hotel.jpg");

        assertEquals(
                "https://gra-system.com/uploads/hotel.jpg",
                img1.getFullUrl(),
                "Gabim në gjenerimin e URL-së relative"
        );

        BiznesImazhe img2 = new BiznesImazhe();
        img2.setUrl("http://external-link.com/photo.png");

        assertEquals(
                "http://external-link.com/photo.png",
                img2.getFullUrl(),
                "Gabim: URL-ja absolute nuk duhet të ndryshojë"
        );
    }

    @Test
    void testIsImageValidation() {
        BiznesImazhe img = new BiznesImazhe();

        img.setUrl("foto.PNG");
        assertTrue(img.isImage(), ".PNG duhet të njihet si imazh");

        img.setUrl("logo.gif");
        assertTrue(img.isImage(), ".gif duhet të njihet si imazh");

        img.setUrl("dokument.pdf");
        assertFalse(img.isImage(), ".pdf NUK është imazh");

        img.setUrl(null);
        assertFalse(img.isImage(), "null nuk duhet të jetë imazh");
    }

    @Test
    void testFileExtensionExtraction() {
        BiznesImazhe img = new BiznesImazhe();

        img.setUrl("pamja_detit.jpeg");
        assertEquals("jpeg", img.getFileExtension());

        img.setUrl("pa-prapashtese");
        assertEquals("", img.getFileExtension());
    }

    @Test
    void testPrimaryStatus() {
        BiznesImazhe img = new BiznesImazhe();

        assertFalse(img.isPrimary(), "Default duhet të jetë false");

        img.setAsPrimary();
        assertTrue(img.isPrimary(), "Duhet të ishte true pas setAsPrimary()");

        img.removeAsPrimary();
        assertFalse(img.isPrimary(), "Duhet të ishte false pas removeAsPrimary()");
    }
}

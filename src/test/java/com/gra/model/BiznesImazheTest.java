package com.gra.model;

public class BiznesImazheTest {

    public static void main(String[] args) {
        System.out.println("🖼️ Duke nisur testimin për BiznesImazhe.java...");

        testFullUrlGeneration();
        testIsImageValidation();
        testFileExtensionExtraction();
        testPrimaryStatus();

        System.out.println("\n✅ Të gjitha testet për BiznesImazhe kaluan me sukses!");
    }

    private static void testFullUrlGeneration() {
        BiznesImazhe img1 = new BiznesImazhe();
        img1.setUrl("hotel.jpg");

        // Testo URL-ne relative
        assert img1.getFullUrl().equals("https://gra-system.com/uploads/hotel.jpg")
                : "Gabim në gjenerimin e URL-së relative";

        // Testo URL-ne absolute
        BiznesImazhe img2 = new BiznesImazhe();
        img2.setUrl("http://external-link.com/photo.png");
        assert img2.getFullUrl().equals("http://external-link.com/photo.png")
                : "Gabim: URL-ja absolute nuk duhet të ndryshojë";

        System.out.println("  - Testi i URL-së: OK");
    }

    private static void testIsImageValidation() {
        BiznesImazhe img = new BiznesImazhe();

        // Formate të sakta
        img.setUrl("foto.PNG");
        assert img.isImage() : "Gabim: .PNG duhet të njihet si imazh";

        img.setUrl("logo.gif");
        assert img.isImage() : "Gabim: .gif duhet të njihet si imazh";

        // Formate të gabuara
        img.setUrl("dokument.pdf");
        assert !img.isImage() : "Gabim: .pdf NUK është imazh";

        img.setUrl(null);
        assert !img.isImage() : "Gabim: null nuk duhet të jetë imazh";

        System.out.println("  - Testi i validimit të formatit: OK");
    }

    private static void testFileExtensionExtraction() {
        BiznesImazhe img = new BiznesImazhe();

        img.setUrl("pamja_detit.jpeg");
        assert img.getFileExtension().equals("jpeg") : "Gabim në nxjerrjen e extension";

        img.setUrl("pa-prapashtese");
        assert img.getFileExtension().equals("") : "Gabim: Duhet të kthente string bosh";

        System.out.println("  - Testi i prapashtesës (Extension): OK");
    }

    private static void testPrimaryStatus() {
        BiznesImazhe img = new BiznesImazhe();

        assert !img.isPrimary() : "Default duhet të jetë false";

        img.setAsPrimary();
        assert img.isPrimary() : "Duhet të ishte true pas setAsPrimary()";

        img.removeAsPrimary();
        assert !img.isPrimary() : "Duhet të ishte false pas removeAsPrimary()";

        System.out.println("  - Testi i statusit primar: OK");
    }
}
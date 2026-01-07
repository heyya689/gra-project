package com.gra.tester;

import com.gra.dao.BiznesDAO;
import com.gra.dao.BiznesImazheDAO;
import com.gra.model.Biznes;
import com.gra.model.BiznesImazhe;

import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BiznesImazheDAOTest extends BaseDAOTest {

    private BiznesImazheDAO imazheDAO;
    private BiznesDAO biznesDAO;

    private int biznesId;
    private int image1Id;
    private int image2Id;

    @BeforeEach
    void setup() throws Exception {
        super.initDatabase();

        biznesDAO = new BiznesDAO();
        imazheDAO = new BiznesImazheDAO();

        // create Biznes (dependency)
        Biznes biznes = new Biznes();
        biznes.setEmri("Image Test Biznes");
        biznes.setPershkrim("Biznes for image testing");
        biznes.setKategori("Test");
        biznes.setNipt("IMG-001-" + System.nanoTime()); // unique NIPT
        biznes.setLicense("LIC-IMG");
        biznes.setTelefon("111111");
        biznes.setEmail("img@test.com");
        biznes.setWebsite("img.com");

        biznesDAO.save(biznes);
        biznesId = biznes.getBiznesId();
        assertTrue(biznesId > 0, "Biznes ID should be set");

        // Create test images
        createTestImages();
    }

    private void createTestImages() throws Exception {
        Biznes biznes = biznesDAO.findById(biznesId);

        BiznesImazhe img1 = new BiznesImazhe();
        img1.setBiznes(biznes);
        img1.setUrl("img1.jpg");
        img1.setPershkrim("Front image");
        img1.setPrimary(true);
        img1.setRenditja(1);

        imazheDAO.save(img1);
        image1Id = img1.getImazhId();

        BiznesImazhe img2 = new BiznesImazhe();
        img2.setBiznes(biznes);
        img2.setUrl("img2.jpg");
        img2.setPershkrim("Side image");
        img2.setPrimary(false);
        img2.setRenditja(2);

        imazheDAO.save(img2);
        image2Id = img2.getImazhId();

        assertTrue(image1Id > 0, "Image 1 ID should be set");
        assertTrue(image2Id > 0, "Image 2 ID should be set");
    }


    @Test
    @Order(1)
    void testSaveAndFindById() throws Exception {
        assertTrue(image1Id > 0);
        assertTrue(image2Id > 0);

        BiznesImazhe img1 = imazheDAO.findById(image1Id);
        assertNotNull(img1);
        assertEquals("img1.jpg", img1.getUrl());
    }

    // findById(int)
    @Test
    @Order(2)
    void testFindById() throws Exception {
        BiznesImazhe img = imazheDAO.findById(image1Id);
        assertNotNull(img);
        assertEquals("img1.jpg", img.getUrl());
        assertEquals("Front image", img.getPershkrim());
    }

    // findByBusinessId(int)
    @Test
    @Order(3)
    void testFindByBusinessId() throws Exception {
        List<BiznesImazhe> images = imazheDAO.findByBusinessId(biznesId);
        assertEquals(2, images.size());
    }

    // findPrimaryImageByBusinessId(int)
    @Test
    @Order(4)
    void testFindPrimaryImage() throws Exception {
        BiznesImazhe primary = imazheDAO.findPrimaryImageByBusinessId(biznesId);
        assertNotNull(primary);
        assertTrue(primary.isPrimary());
        assertEquals(image1Id, primary.getImazhId());
    }

    // update(BiznesImazhe)
    @Test
    @Order(5)
    void testUpdateImage() throws Exception {
        BiznesImazhe img = imazheDAO.findById(image2Id);
        img.setPershkrim("Updated description");
        img.setRenditja(10);

        imazheDAO.update(img);

        BiznesImazhe updated = imazheDAO.findById(image2Id);
        assertEquals("Updated description", updated.getPershkrim());
        assertEquals(10, updated.getRenditja());
    }

    // setAsPrimary(int)
    @Test
    @Order(6)
    void testSetAsPrimary() throws Exception {
        imazheDAO.setAsPrimary(image2Id);

        BiznesImazhe primary = imazheDAO.findPrimaryImageByBusinessId(biznesId);
        assertEquals(image2Id, primary.getImazhId());

        // verify image1 is no longer primary
        BiznesImazhe img1 = imazheDAO.findById(image1Id);
        assertFalse(img1.isPrimary());
    }

    // updateImageOrder(int)
    @Test
    @Order(7)
    void testUpdateImageOrder() throws Exception {
        imazheDAO.updateImageOrder(image1Id, 5);

        BiznesImazhe img = imazheDAO.findById(image1Id);
        assertEquals(5, img.getRenditja());
    }

    // findImagesByDescription(String)
    @Test
    @Order(8)
    void testFindImagesByDescription() throws Exception {
        List<BiznesImazhe> list = imazheDAO.findImagesByDescription("Front");
        assertFalse(list.isEmpty());
        assertTrue(list.stream().anyMatch(img -> img.getImazhId() == image1Id));
    }

    // countImages()
    @Test
    @Order(9)
    void testCountImages() throws Exception {
        int count = imazheDAO.countImages();
        assertTrue(count >= 2, "Should have at least 2 images");
    }

    // countImagesByBusinessId(int)
    @Test
    @Order(10)
    void testCountImagesByBusinessId() throws Exception {
        int count = imazheDAO.countImagesByBusinessId(biznesId);
        assertEquals(2, count);
    }

    // findImagesWithoutDescription()
    @Test
    @Order(11)
    void testFindImagesWithoutDescription() throws Exception {
        Biznes biznes = biznesDAO.findById(biznesId);

        BiznesImazhe img = new BiznesImazhe();
        img.setBiznes(biznes);
        img.setUrl("no-desc.jpg");
        img.setPershkrim(""); // empty description
        img.setPrimary(false);
        img.setRenditja(3);

        imazheDAO.save(img);

        List<BiznesImazhe> list = imazheDAO.findImagesWithoutDescription();
        assertTrue(list.stream().anyMatch(i -> i.getUrl().equals("no-desc.jpg")));
    }

    // delete(int)
    @Test
    @Order(12)
    void testDeleteImage() throws Exception {
        //temporary image
        Biznes biznes = biznesDAO.findById(biznesId);
        BiznesImazhe tempImg = new BiznesImazhe();
        tempImg.setBiznes(biznes);
        tempImg.setUrl("temp.jpg");
        tempImg.setPershkrim("Temporary");
        tempImg.setPrimary(false);
        tempImg.setRenditja(99);

        imazheDAO.save(tempImg);
        int tempId = tempImg.getImazhId();

        imazheDAO.delete(tempId);

        // verify deletion
        BiznesImazhe deleted = imazheDAO.findById(tempId);
        assertNull(deleted);
    }
}
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



}
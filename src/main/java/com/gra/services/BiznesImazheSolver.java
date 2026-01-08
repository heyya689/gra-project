package com.gra.services;

import com.gra.dao.BiznesImazheDAO;
import com.gra.model.BiznesImazhe;

import java.util.List;

public class BiznesImazheSolver {

    private BiznesImazheDAO biznesImazheDAO;

    public BiznesImazheSolver() {
        this.biznesImazheDAO = new BiznesImazheDAO();
    }

    public BiznesImazhe addImage(BiznesImazhe imazh) throws Exception {
        biznesImazheDAO.save(imazh);
        return imazh;
    }

    public BiznesImazhe getImageById(int id) throws Exception {
        return biznesImazheDAO.findById(id);
    }

    public List<BiznesImazhe> getAllImages() throws Exception {
        return biznesImazheDAO.findAll();
    }

    public List<BiznesImazhe> getImagesByBusinessId(int biznesId) throws Exception {
        return biznesImazheDAO.findByBusinessId(biznesId);
    }

    public BiznesImazhe getPrimaryImageByBusinessId(int biznesId) throws Exception {
        return biznesImazheDAO.findPrimaryImageByBusinessId(biznesId);
    }

    public List<BiznesImazhe> searchImagesByDescription(String keyword) throws Exception {
        return biznesImazheDAO.findImagesByDescription(keyword);
    }

    public List<BiznesImazhe> getImagesWithoutDescription() throws Exception {
        return biznesImazheDAO.findImagesWithoutDescription();
    }

    public void updateImage(BiznesImazhe imazh) throws Exception {
        biznesImazheDAO.update(imazh);
    }

    public void setImageAsPrimary(int imazhId) throws Exception {
        biznesImazheDAO.setAsPrimary(imazhId);
    }

    public void updateImageOrder(int imazhId, int newOrder) throws Exception {
        biznesImazheDAO.updateImageOrder(imazhId, newOrder);
    }

    public void deleteImage(int imazhId) throws Exception {
        biznesImazheDAO.delete(imazhId);
    }

    public void deleteImagesByBusinessId(int biznesId) throws Exception {
        biznesImazheDAO.deleteByBusinessId(biznesId);
    }

    public int countAllImages() throws Exception {
        return biznesImazheDAO.countImages();
    }

    public int countImagesByBusinessId(int biznesId) throws Exception {
        return biznesImazheDAO.countImagesByBusinessId(biznesId);
    }
}

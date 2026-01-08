package com.gra.services;

import com.gra.dao.FAQDAO;
import com.gra.model.FAQ;
import com.gra.model.FaqjaKategori;

import java.util.List;

public class FAQSolver {

    private FAQDAO faqDAO;

    public FAQSolver() {
        this.faqDAO = new FAQDAO();
    }

    public FAQ createFAQ(FAQ faq) throws Exception {
        faqDAO.save(faq);
        return faq;
    }

    public FAQ getFAQById(int faqId) throws Exception {
        return faqDAO.findById(faqId);
    }

    public List<FAQ> getAllFAQs() throws Exception {
        return faqDAO.findAll();
    }

    public List<FAQ> getActiveFAQs() throws Exception {
        return faqDAO.findActiveFAQs();
    }

    public List<FAQ> getFAQsByCategory(int kategoriId) throws Exception {
        return faqDAO.findByCategoryId(kategoriId);
    }

    public List<FAQ> searchFAQsByQuestion(String keyword) throws Exception {
        return faqDAO.searchByQuestion(keyword);
    }

    public List<FAQ> searchFAQsByAnswer(String keyword) throws Exception {
        return faqDAO.searchByAnswer(keyword);
    }

    public List<FAQ> getLatestFAQs(int limit) throws Exception {
        return faqDAO.findLatestFAQs(limit);
    }

    public void updateFAQ(FAQ faq) throws Exception {
        faqDAO.update(faq);
    }

    public void updateFAQOrder(int faqId, int newOrder) throws Exception {
        faqDAO.updateOrder(faqId, newOrder);
    }

    public void activateFAQ(int faqId) throws Exception {
        faqDAO.activate(faqId);
    }

    public void deactivateFAQ(int faqId) throws Exception {
        faqDAO.deactivate(faqId);
    }

    public void updateCategories(int faqId, List<FaqjaKategori> kategorite) throws Exception {
        FAQ faq = faqDAO.findById(faqId);
        faq.setKategorite(kategorite);
        faqDAO.update(faq);
    }

    public void deleteFAQ(int faqId) throws Exception {
        faqDAO.delete(faqId);
    }

    public int countAllFAQs() throws Exception {
        return faqDAO.countFAQs();
    }

    public int countActiveFAQs() throws Exception {
        return faqDAO.countActiveFAQs();
    }
}

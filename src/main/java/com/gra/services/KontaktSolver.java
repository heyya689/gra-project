package com.gra.services;

import com.gra.dao.KontaktDAO;
import com.gra.model.Kontakt;

import java.util.List;

public class KontaktSolver {

    private KontaktDAO kontaktDAO;

    public KontaktSolver() {
        this.kontaktDAO = new KontaktDAO();
    }

    public Kontakt getKontaktById(int kontaktId) throws Exception {
        return kontaktDAO.findById(kontaktId);
    }

    public List<Kontakt> getAllKontaktet() throws Exception {
        return kontaktDAO.findAll();
    }

    public List<Kontakt> getKontaktetByUserId(int userId) throws Exception {
        return kontaktDAO.findByUserId(userId);
    }

    public List<Kontakt> getKontaktetByEmail(String email) throws Exception {
        return kontaktDAO.findByEmail(email);
    }

    public List<Kontakt> getKontaktetByStatus(String status) throws Exception {
        return kontaktDAO.findByStatus(status);
    }

    public List<Kontakt> getMesazhetHapura() throws Exception {
        return kontaktDAO.findOpenMessages();
    }

    public List<Kontakt> searchKontaktBySubjekt(String keyword) throws Exception {
        return kontaktDAO.searchBySubject(keyword);
    }

    public List<Kontakt> getMesazhetMeTeFundit(int limit) throws Exception {
        return kontaktDAO.findLatestMessages(limit);
    }

    public void dergoKontakt(Kontakt kontakt) throws Exception {
        kontaktDAO.save(kontakt);
    }

    public void perditesoKontakt(Kontakt kontakt) throws Exception {
        kontaktDAO.update(kontakt);
    }

    public void fshiKontakt(int kontaktId) throws Exception {
        kontaktDAO.delete(kontaktId);
    }

    public void shenoSiLexuar(int kontaktId) throws Exception {
        kontaktDAO.markAsRead(kontaktId);
    }

    public void shenoSiPergjigjur(int kontaktId) throws Exception {
        kontaktDAO.markAsReplied(kontaktId);
    }

    public void mbyllKontakt(int kontaktId) throws Exception {
        kontaktDAO.markAsClosed(kontaktId);
    }

    public int numerTotalMesazhesh() throws Exception {
        return kontaktDAO.countMessages();
    }

    public int numerMesazheshSipasStatusit(String status) throws Exception {
        return kontaktDAO.countMessagesByStatus(status);
    }

    public int numerMesazheshPaLexuara() throws Exception {
        return kontaktDAO.countUnreadMessages();
    }

    public void fshiMesazheTeVjetra(int dite) throws Exception {
        kontaktDAO.deleteOldMessages(dite);
    }
}

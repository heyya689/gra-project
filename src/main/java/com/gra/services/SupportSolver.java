package com.gra.services;

import com.gra.dao.KontaktDAO;
import com.gra.dao.VleresimDAO;
import com.gra.model.Kontakt;
import com.gra.model.Vleresim;
import java.util.List;

public class SupportSolver {
    private KontaktDAO kontaktDAO;
    private VleresimDAO vleresimDAO;

    public SupportSolver() {
        this.kontaktDAO = new KontaktDAO();
        this.vleresimDAO = new VleresimDAO();
    }

    // --- Menaxhimi i Kontakteve ---

    public void dergoMesazh(Kontakt mesazhi) throws Exception {
        // Vendosim statusin fillestar para ruajtjes
        mesazhi.setStatus("PENDING");
        kontaktDAO.save(mesazhi);
    }

    public void mbyllMesazhin(int kontaktId) throws Exception {
        // Përdorim metodën specifike që ke krijuar në DAO
        kontaktDAO.markAsClosed(kontaktId);
    }

    public List<Kontakt> merrMesazhetEPaLexuara() throws Exception {
        return kontaktDAO.findByStatus("PENDING");
    }

    // --- Menaxhimi i Vlerësimeve (Reviews) ---

    public void shtoVleresim(Vleresim vleresim) throws Exception {
        // Rregull business-i: Vlerësimet e reja duhen aprovuar nga admini
        vleresim.setApproved(false);
        vleresimDAO.save(vleresim);
    }

    public void aprovoVleresimin(int vleresimId) throws Exception {
        vleresimDAO.approveReview(vleresimId);
    }

    public List<Vleresim> merrVleresimetPublike(int biznesId) throws Exception {
        // Shfaqim vetëm ato që janë aprovuar për faqen publike
        return vleresimDAO.findByBusinessId(biznesId);
    }

    public double merrRatingMesatar(int biznesId) throws Exception {
        return vleresimDAO.getAverageRatingByBusinessId(biznesId);
    }
}
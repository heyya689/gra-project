package com.gra.services;

import com.gra.dao.PagesatDAO;
import com.gra.dao.PagesatDetajeDAO;
import com.gra.dao.PagesatHistorikDAO;
import com.gra.model.Pagesat;
import com.gra.model.PagesatDetaje;
import com.gra.model.PagesatHistorik;
import java.util.List;

public class FinanceSolver {
    private PagesatDAO pagesatDAO;
    private PagesatDetajeDAO detajeDAO;
    private PagesatHistorikDAO historikDAO;

    public FinanceSolver() {
        this.pagesatDAO = new PagesatDAO();
        this.detajeDAO = new PagesatDetajeDAO();
        this.historikDAO = new PagesatHistorikDAO();
    }

    /**
     * Ky është procesi kryesor i pagesës.
     * Koordinon ruajtjen e pagesës, detajeve teknike dhe log-un në historik.
     */
    public void procesoPagesen(Pagesat pagesa, PagesatDetaje detaje) throws Exception {
        try {
            // 1. Ruajmë pagesën kryesore (merr ID-në nga DB)
            pagesatDAO.save(pagesa);

            // 2. Lidhim detajet dhe historikun me pagesën e sapokrijuar
            detaje.setPagesa(pagesa);

            // 3. Ruajmë detajet teknike (karta, IP, gateway etj.)
            detajeDAO.save(detaje);

            // 4. Regjistrojmë suksesin në Historik
            PagesatHistorik historiSuksesi = new PagesatHistorik("SUCCESS", "Pagesa u procesua me sukses.");
            historiSuksesi.setPagesa(pagesa);
            historikDAO.save(historiSuksesi);

        } catch (Exception e) {
            // Nëse diçka dështon, regjistrojmë gabimin në historik nëse pagesa është krijuar
            if (pagesa.getPagesaId() > 0) {
                PagesatHistorik historiDeshtimi = new PagesatHistorik("FAILED", "Gabim: " + e.getMessage());
                historiDeshtimi.setPagesa(pagesa);
                historikDAO.save(historiDeshtimi);
            }
            throw e; // Ridhënia e error-it për ta kapur në Interface (GUI)
        }
    }

    /**
     * Merr historikun e plotë të një pagese për ta shfaqur te paneli i administratorit.
     */
    public List<PagesatHistorik> merrHistorikunEPageses(int pagesaId) throws Exception {
        return historikDAO.findByPagesaId(pagesaId);
    }

    /**
     * Kontrollon detajet teknike të një pagese (p.sh. për verifikim mashtrimi/fraud).
     */
    public PagesatDetaje merrDetajetTeknike(int pagesaId) throws Exception {
        return detajeDAO.findByPagesaId(pagesaId);
    }
}
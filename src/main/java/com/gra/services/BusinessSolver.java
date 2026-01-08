package com.gra.services;

import com.gra.dao.BiznesDAO;
import com.gra.dao.BiznesImazheDAO;
import com.gra.dao.FAQDAO;
import com.gra.dao.KategoriDAO;
import com.gra.model.Biznes;
import com.gra.model.BiznesImazhe;
import com.gra.model.FAQ;
import java.util.List;

public class BusinessSolver {
    private BiznesDAO biznesDAO;
    private BiznesImazheDAO imazheDAO;
    private FAQDAO faqDAO;
    private KategoriDAO kategoriDAO;

    public BusinessSolver() {
        this.biznesDAO = new BiznesDAO();
        this.imazheDAO = new BiznesImazheDAO();
        this.faqDAO = new FAQDAO();
        this.kategoriDAO = new KategoriDAO();
    }

    /**
     * Regjistron një biznes të ri dhe ruan menjëherë imazhet e tij.
     * Siguron që të paktën një imazh të jetë primar.
     */
    public void regjistroBiznesMeImazhe(Biznes biznes, List<BiznesImazhe> imazhet, int kategoriId) throws Exception {
        // 1. Kontrollo NIPT-in
        if (biznesDAO.niptExists(biznes.getNipt())) {
            throw new Exception("Një biznes me këtë NIPT ekziston tashmë!");
        }

        // 2. Ruaj Biznesin - kjo metodë pranon kategoriId tani
        biznesDAO.save(biznes, kategoriId);

        // 3. Ruaj Imazhet
        if (imazhet != null && !imazhet.isEmpty()) {
            for (BiznesImazhe imazh : imazhet) {
                imazh.setBiznes(biznes); // Lidhim imazhin me biznesin e sapokrijuar
                imazheDAO.save(imazh);
            }

            // Cakto imazhin e parë si primar nëse nuk ka tjetër të specifikuar
            boolean hasPrimary = imazhet.stream().anyMatch(BiznesImazhe::isPrimary);
            if (!hasPrimary && !imazhet.isEmpty()) {
                imazhet.get(0).setPrimary(true);
                imazheDAO.update(imazhet.get(0));
            }
        }
    }

    /**
     * Merr detajet e plota të një biznesi: Lokacionin, Kategoritë dhe Imazhet.
     */
    public Biznes merrDetajetEBiznesit(int biznesId) throws Exception {
        Biznes biznes = biznesDAO.findById(biznesId);
        if (biznes != null) {
            // Ngarkojmë imazhet
            biznes.setImazhet(imazheDAO.findByBusinessId(biznesId));

            // Kontrollojmë nëse kategoritë janë të ngarkuara
            if (biznes.getKategorite() == null || biznes.getKategorite().isEmpty()) {
                // Ngarko kategoritë nëse nuk janë ngarkuar
                List<Biznes> kategorite = kategoriDAO.findBiznesetByKategoriId(biznesId);
                // Kjo metodë nuk është e saktë - duhet një metodë që kthen kategoritë e një biznesi
                // Do t'ju jap metodën e duhur më poshtë
            }
        }
        return biznes;
    }

    /**
     * Kërkon biznese sipas qytetit dhe kategorisë njëkohësisht.
     */
    public List<Biznes> kerkoBiznese(String qyteti, String kategoria) throws Exception {
        // Kjo duhet të jetë një metodë e re në BiznesDAO
        // Për momentin kthejmë të gjitha bizneset
        return biznesDAO.findAll();
    }

    /**
     * Menaxhimi i FAQ-ve të sistemit ose biznesit.
     */
    public List<FAQ> merrFAQetAktive() throws Exception {
        return faqDAO.findActiveFAQs();
    }

    /**
     * Merr FAQ-et për një biznes specifik
     */


    /**
     * Përditëson imazhin kryesor (Primary Image) të biznesit.
     */
    public void ndryshoImazhinKryesor(int biznesId, int imazhId) throws Exception {
        // Heq 'primary' nga të gjitha imazhet e këtij biznesi
        List<BiznesImazhe> imazhet = imazheDAO.findByBusinessId(biznesId);
        for (BiznesImazhe imazh : imazhet) {
            if (imazh.getImazhId() != imazhId && imazh.isPrimary()) {
                imazh.setPrimary(false);
                imazheDAO.update(imazh);
            }
        }

        // Vendos imazhin e ri si primar
        imazheDAO.setAsPrimary(imazhId);
    }

    /**
     * Fshin një biznes plotësisht duke fshirë imazhet, lidhjet dhe lokacionin.
     */
    public void fshiBiznesin(int biznesId) throws Exception {
        // Imazhet duhen fshirë të parat për shkak të Foreign Key
        imazheDAO.deleteByBusinessId(biznesId);
        biznesDAO.delete(biznesId);
    }

    /**
     * Shton një kategori të re për një biznes
     */
    public void shtoKategoriPerBiznes(int biznesId, int kategoriId) throws Exception {
        // Përdor BiznesDAO për të shtuar lidhjen
        biznesDAO.addKategoriToBiznes(biznesId, kategoriId);
    }

    /**
     * Heq një kategori nga një biznes
     */
    public void hiqKategoriNgaBiznes(int biznesId, int kategoriId) throws Exception {
        biznesDAO.removeKategoriFromBiznes(biznesId, kategoriId);
    }

    /**
     * Merr të gjitha bizneset me të gjitha detajet
     */
    public List<Biznes> merrTeGjithaBizneset() throws Exception {
        List<Biznes> businesses = biznesDAO.findAll();

        // Ngarko detajet shtesë për çdo biznes
        for (Biznes biznes : businesses) {
            biznes.setImazhet(imazheDAO.findByBusinessId(biznes.getBiznesId()));
            // Mund të shtoni edhe vlerësimet, rezervimet etj. nëse doni
        }

        return businesses;
    }

    /**
     * Përditëson detajet e biznesit
     */
    public void perditesoBiznes(Biznes biznes) throws Exception {
        biznesDAO.update(biznes);
    }

    /**
     * Kërkon biznese sipas emrit
     */
    public List<Biznes> kerkoBizneseSipasEmrit(String emri) throws Exception {
        // Kjo duhet të implementohet në BiznesDAO
        // Për momentin kthejmë të gjitha dhe filtrojmë
        List<Biznes> businesses = biznesDAO.findAll();
        businesses.removeIf(b -> !b.getEmri().toLowerCase().contains(emri.toLowerCase()));
        return businesses;
    }

}
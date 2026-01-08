package com.gra.services;

import com.gra.dao.RezervimDAO;
import com.gra.dao.InventariDAO;
import com.gra.dao.VleresimDAO;
import com.gra.model.Rezervim;
import com.gra.model.Inventari;
import com.gra.model.Vleresim;
import java.util.List;

public class BookingSolver {
    private RezervimDAO rezervimDAO;
    private InventariDAO inventariDAO;
    private VleresimDAO vleresimDAO;

    public BookingSolver() {
        this.rezervimDAO = new RezervimDAO();
        this.inventariDAO = new InventariDAO();
        this.vleresimDAO = new VleresimDAO();
    }

    /**
     * Proceson një rezervim të ri.
     * Kontrollon nëse ka stok mjaftueshëm para se të lejojë rezervimin.
     */
    public Rezervim krijoRezervimMeKontrollStoku(Rezervim rezervim) throws Exception {
        if (rezervim.getInventar() == null) {
            throw new Exception("Duhet të zgjidhni një artikull/shërbim nga inventari!");
        }

        // 1. Rifreskojmë të dhënat e inventarit nga DB për të pasur sasinë reale
        Inventari item = inventariDAO.findById(rezervim.getInventar().getInventarId());

        // 2. Kontrollojmë disponueshmërinë
        if (item == null || !item.isActive() || item.getSasi() < rezervim.getNumriPersonave()) {
            throw new Exception("Më vjen keq, nuk ka vende/stok mjaftueshëm për këtë kërkesë!");
        }

        // 3. Ruajmë rezervimin (RezervimDAO.save brenda saj tashmë bën decreaseStock)
        rezervimDAO.save(rezervim);

        return rezervim;
    }

    /**
     * Anulon një rezervim dhe rikthen stokun në inventar.
     */
    public void anuloRezervim(int rezervimId) throws Exception {
        Rezervim r = rezervimDAO.findById(rezervimId);
        if (r == null) throw new Exception("Rezervimi nuk u gjet!");

        if (r.getStatus().equals("CANCELLED")) {
            throw new Exception("Ky rezervim është anuluar më parë.");
        }

        // Përditësojmë statusin
        rezervimDAO.updateStatus(rezervimId, "CANCELLED");

        // Rrisim stokun përsëri pasi rezervimi u anulua
        if (r.getInventar() != null) {
            Inventari item = r.getInventar();
            item.increaseStock(r.getNumriPersonave());
            inventariDAO.update(item);
        }
    }

    /**
     * Menaxhon vlerësimet (Reviews).
     * Shfaq vetëm vlerësimet e aprovuara për një biznes.
     */
    public List<Vleresim> merrVleresimetEBiznesit(int biznesId) throws Exception {
        // Këtu mund të shtohej logjikë filtruese ekstra
        return vleresimDAO.findByBusinessId(biznesId);
    }

    public void shtoVleresim(Vleresim vleresim) throws Exception {
        // Kontroll i thjeshtë rregullash: rating 1-5
        if (vleresim.getRating() < 1 || vleresim.getRating() > 5) {
            throw new Exception("Vlerësimi duhet të jetë midis 1 dhe 5 yjeve.");
        }
        vleresimDAO.save(vleresim);
    }

    /**
     * Merr të dhëna për Dashboard-in e biznesit.
     */
    public double merrRatingMesatar(int biznesId) throws Exception {
        return vleresimDAO.getAverageRatingByBusinessId(biznesId);
    }

    public List<Inventari> merrStokunKritik(int biznesId, int threshold) throws Exception {
        // Kthen artikujt që janë drejt mbarimit për këtë biznes
        return inventariDAO.findLowStockItems(threshold);
    }
}
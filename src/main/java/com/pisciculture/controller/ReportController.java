package com.pisciculture.controller;

import com.pisciculture.model.*;
import com.pisciculture.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private PoissonRepository poissonRepository;

    @Autowired
    private EtatNutritionJourRepository etatNutritionJourRepository;

    @Autowired
    private EtangRepository etangRepository;

    @Autowired
    private EtangPoissonRepository etangPoissonRepository;

    @GetMapping("/etangs")
    public String reportEtangs(Model model) {
        List<Etang> etangs = etangRepository.findAll();
        List<Poisson> allPoissons = poissonRepository.findAll();
        List<EtangReportRow> rows = new ArrayList<>();

        // Map pour stocker les calculs par étang
        Map<Long, EtangReportRow> etangMap = new HashMap<>();
        for (Etang etang : etangs) {
            EtangReportRow row = new EtangReportRow();
            row.setEtang(etang);
            row.setRevenu(BigDecimal.ZERO);
            row.setDepense(BigDecimal.ZERO);
            row.setBenefice(BigDecimal.ZERO);
            etangMap.put(etang.getId(), row);
        }

        // Calculer les stats pour chaque poisson et les sommer par étang
        for (Poisson p : allPoissons) {
            // Trouver l'étang actuel du poisson
            Etang currentEtang = etangPoissonRepository.findTopByPoissonOrderByDateDesc(p)
                    .map(EtangPoisson::getEtang)
                    .orElse(null);

            if (currentEtang != null && etangMap.containsKey(currentEtang.getId())) {
                PoissonReportRow poissonStat = calculatePoissonStat(p);
                EtangReportRow etangRow = etangMap.get(currentEtang.getId());
                
                etangRow.setRevenu(etangRow.getRevenu().add(poissonStat.getRevenu()));
                etangRow.setDepense(etangRow.getDepense().add(poissonStat.getCoutAlimentation()));
                etangRow.setBenefice(etangRow.getBenefice().add(poissonStat.getBenefice()));
            }
        }

        model.addAttribute("etangRows", new ArrayList<>(etangMap.values()));
        
        // Ajouter aussi le reporting des poissons pour l'affichage en bas
        model.addAttribute("poissonRows", calculateAllPoissonStats(allPoissons));
        model.addAttribute("title", "Reporting par Étang");
        
        return "report/etangs";
    }

    @GetMapping("/poissons")
    public String reportPoissons(Model model) {
        List<Poisson> poissons = poissonRepository.findAll();
        model.addAttribute("rows", calculateAllPoissonStats(poissons));
        model.addAttribute("title", "Reporting des Poissons");
        return "report/poissons";
    }

    private List<PoissonReportRow> calculateAllPoissonStats(List<Poisson> poissons) {
        List<PoissonReportRow> rows = new ArrayList<>();
        for (Poisson p : poissons) {
            rows.add(calculatePoissonStat(p));
        }
        return rows;
    }

    private PoissonReportRow calculatePoissonStat(Poisson p) {
        Race race = p.getRace();
        BigDecimal poidsInitial = p.getPoidsInitial() != null ? p.getPoidsInitial() : BigDecimal.ZERO;
        
        if (race == null) {
            PoissonReportRow row = new PoissonReportRow();
            row.setPoisson(p);
            row.setPoidsInitial(poidsInitial);
            row.setPoidsAtteint(poidsInitial);
            row.setCoutInitial(BigDecimal.ZERO);
            row.setCoutAlimentation(BigDecimal.ZERO);
            row.setRevenu(BigDecimal.ZERO);
            row.setBenefice(BigDecimal.ZERO);
            return row;
        }

        BigDecimal prixVenteKg = race.getPrixVenteParKg() != null ? race.getPrixVenteParKg() : BigDecimal.ZERO;
        BigDecimal poidsMax = race.getPoidsMax() != null ? race.getPoidsMax() : BigDecimal.ZERO;

        // Chercher le jour où le poisson atteint (ou dépasse) son poidsMax
        BigDecimal poidsCible = poidsMax.compareTo(BigDecimal.ZERO) > 0 ? poidsMax : BigDecimal.ZERO;
        EtatNutritionJour etatCible = null;

        if (poidsCible.compareTo(BigDecimal.ZERO) > 0) {
            etatCible = etatNutritionJourRepository
                    .findFirstByPoissonAndPoidsGreaterThanEqualOrderByDateJourAsc(p, poidsCible)
                    .orElse(null);
        }

        if (etatCible == null) {
            etatCible = etatNutritionJourRepository
                    .findTopByPoissonOrderByDateJourDesc(p)
                    .orElse(null);
        }

        BigDecimal poidsAtteint;
        BigDecimal coutAlimCumule;

        if (etatCible != null) {
            poidsAtteint = etatCible.getPoids() != null ? etatCible.getPoids() : poidsInitial;
            coutAlimCumule = etatCible.getCoutAlimentationCumule() != null
                    ? etatCible.getCoutAlimentationCumule()
                    : BigDecimal.ZERO;
        } else {
            poidsAtteint = poidsInitial;
            coutAlimCumule = BigDecimal.ZERO;
        }

        BigDecimal revenu = poidsMax.multiply(prixVenteKg);
        BigDecimal benefice = revenu.subtract(coutAlimCumule);

        PoissonReportRow row = new PoissonReportRow();
        row.setPoisson(p);
        row.setRace(race);
        row.setPoidsInitial(poidsInitial);
        row.setPoidsAtteint(poidsAtteint);
        row.setCoutInitial(BigDecimal.ZERO);
        row.setCoutAlimentation(coutAlimCumule);
        row.setRevenu(revenu);
        row.setBenefice(benefice);
        return row;
    }

    public static class EtangReportRow {
        private Etang etang;
        private BigDecimal revenu;
        private BigDecimal depense;
        private BigDecimal benefice;

        public Etang getEtang() { return etang; }
        public void setEtang(Etang etang) { this.etang = etang; }

        public BigDecimal getRevenu() { return revenu; }
        public void setRevenu(BigDecimal revenu) { this.revenu = revenu; }

        public BigDecimal getDepense() { return depense; }
        public void setDepense(BigDecimal depense) { this.depense = depense; }

        public BigDecimal getBenefice() { return benefice; }
        public void setBenefice(BigDecimal benefice) { this.benefice = benefice; }
    }

    public static class PoissonReportRow {
        private Poisson poisson;
        private Race race;
        private BigDecimal poidsInitial;
        private BigDecimal poidsAtteint;
        private BigDecimal coutInitial;
        private BigDecimal coutAlimentation;
        private BigDecimal revenu;
        private BigDecimal benefice;

        public Poisson getPoisson() { return poisson; }
        public void setPoisson(Poisson poisson) { this.poisson = poisson; }

        public Race getRace() { return race; }
        public void setRace(Race race) { this.race = race; }

        public BigDecimal getPoidsInitial() { return poidsInitial; }
        public void setPoidsInitial(BigDecimal poidsInitial) { this.poidsInitial = poidsInitial; }

        public BigDecimal getPoidsAtteint() { return poidsAtteint; }
        public void setPoidsAtteint(BigDecimal poidsAtteint) { this.poidsAtteint = poidsAtteint; }

        public BigDecimal getCoutInitial() { return coutInitial; }
        public void setCoutInitial(BigDecimal coutInitial) { this.coutInitial = coutInitial; }

        public BigDecimal getCoutAlimentation() { return coutAlimentation; }
        public void setCoutAlimentation(BigDecimal coutAlimentation) { this.coutAlimentation = coutAlimentation; }

        public BigDecimal getRevenu() { return revenu; }
        public void setRevenu(BigDecimal revenu) { this.revenu = revenu; }

        public BigDecimal getBenefice() { return benefice; }
        public void setBenefice(BigDecimal benefice) { this.benefice = benefice; }
    }
}

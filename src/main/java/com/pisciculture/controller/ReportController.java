package com.pisciculture.controller;

import com.pisciculture.model.EtatNutritionJour;
import com.pisciculture.model.Poisson;
import com.pisciculture.model.Race;
import com.pisciculture.repository.EtatNutritionJourRepository;
import com.pisciculture.repository.PoissonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private PoissonRepository poissonRepository;

    @Autowired
    private EtatNutritionJourRepository etatNutritionJourRepository;

    @GetMapping("/poissons")
    public String reportPoissons(Model model) {
        List<Poisson> poissons = poissonRepository.findAll();
        List<PoissonReportRow> rows = new ArrayList<>();

        for (Poisson p : poissons) {
            Race race = p.getRace();
            if (race == null) {
                continue;
            }

            BigDecimal poidsInitial = p.getPoidsInitial() != null ? p.getPoidsInitial() : BigDecimal.ZERO;
            BigDecimal prixAchatKg = race.getPrixAchatParKg() != null ? race.getPrixAchatParKg() : BigDecimal.ZERO;
            BigDecimal prixVenteKg = race.getPrixVenteParKg() != null ? race.getPrixVenteParKg() : BigDecimal.ZERO;
            BigDecimal poidsMax = race.getPoidsMax() != null ? race.getPoidsMax() : BigDecimal.ZERO;

            // Le coût initial n'est plus pris en compte dans le calcul du bénéfice
            BigDecimal coutInitial = BigDecimal.ZERO;

            // Chercher le jour où le poisson atteint (ou dépasse) son poidsMax
            BigDecimal poidsCible = poidsMax.compareTo(BigDecimal.ZERO) > 0 ? poidsMax : BigDecimal.ZERO;
            EtatNutritionJour etatCible = null;

            if (poidsCible.compareTo(BigDecimal.ZERO) > 0) {
                etatCible = etatNutritionJourRepository
                        .findFirstByPoissonAndPoidsGreaterThanEqualOrderByDateJourAsc(p, poidsCible)
                        .orElse(null);
            }

            // Si le poisson n'a jamais atteint poidsMax, on prend le dernier état disponible (s'il existe)
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

            // Revenu théorique basé sur poidsMax (objectif)
            BigDecimal revenu = poidsMax.multiply(prixVenteKg);

            // Coût total = uniquement l'alimentation cumulée
            BigDecimal coutTotal = coutAlimCumule;

            // Bénéfice = revenu - coût alimentation
            BigDecimal benefice = revenu.subtract(coutTotal);

            PoissonReportRow row = new PoissonReportRow();
            row.setPoisson(p);
            row.setRace(race);
            row.setPoidsInitial(poidsInitial);
            row.setPoidsAtteint(poidsAtteint);
            row.setCoutInitial(coutInitial);
            row.setCoutAlimentation(coutAlimCumule);
            row.setRevenu(revenu);
            row.setBenefice(benefice);

            rows.add(row);
        }

        model.addAttribute("rows", rows);
        model.addAttribute("title", "Reporting des Poissons");

        return "report/poissons";
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

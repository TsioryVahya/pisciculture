package com.pisciculture.controller;

import com.pisciculture.model.*;
import com.pisciculture.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/alimentations")
public class AlimentationController {

    @Autowired
    private AlimentationRepository alimentationRepository;

    @Autowired
    private AlimentationDetailleRepository alimentationDetailleRepository;

    @Autowired
    private EtangRepository etangRepository;

    @Autowired
    private NourritureRepository nourritureRepository;

    @Autowired
    private EtangPoissonRepository etangPoissonRepository;

   
    @Autowired
    private PoissonStatutRepository poissonStatutRepository;

    @Autowired
    private EtatNutritionJourRepository etatNutritionJourRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("alimentations", alimentationRepository.findAll(Sort.by(Sort.Direction.DESC, "dateHeure")));
        model.addAttribute("title", "Suivi de l'Alimentation");
        return "alimentations/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("alimentation", new Alimentation());
        model.addAttribute("etangs", etangRepository.findAll());
        model.addAttribute("nourritures", nourritureRepository.findAll());
        model.addAttribute("title", "Nouvelle Alimentation");
        return "alimentations/form";
    }

    @PostMapping("/save")
    public String save(@RequestParam("etangId") Long etangId,
                      @RequestParam("nourritureIds") List<Long> nourritureIds,
                      @RequestParam("quantites") List<BigDecimal> quantites) {
        
        Etang etang = etangRepository.findById(etangId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid etang Id:" + etangId));

        Alimentation alimentation = new Alimentation();
        alimentation.setEtang(etang);
        alimentation.setDateHeure(LocalDateTime.now());
        
        Alimentation savedAlimentation = alimentationRepository.save(alimentation);

        BigDecimal totalProteine = BigDecimal.ZERO;
        BigDecimal totalGlucide = BigDecimal.ZERO;
        BigDecimal coutTotalAlimentation = BigDecimal.ZERO;

        for (int i = 0; i < nourritureIds.size(); i++) {
            Long nId = nourritureIds.get(i);
            BigDecimal quantite = quantites.get(i);

            if (quantite != null && quantite.compareTo(BigDecimal.ZERO) > 0) {
                Nourriture nourriture = nourritureRepository.findById(nId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid nourriture Id:" + nId));

                AlimentationDetaille detail = new AlimentationDetaille();
                detail.setAlimentation(savedAlimentation);
                detail.setNourriture(nourriture);
                detail.setQuantiteKg(quantite);
                detail.setPrixKiloNourriture(nourriture.getPrixAchatParKg());
                
                alimentationDetailleRepository.save(detail);

                // Calcul des nutriments totaux (quantité en kg * pourcentage / 100 * 1000 pour avoir en grammes)
                // Correction : Le pourcentage est déjà une valeur entre 0 et 100 (ex: 0.20 pour 0.20%)
                BigDecimal prot = quantite.multiply(nourriture.getPourcentageApportProteine())
                                         .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP)
                                         .multiply(new BigDecimal("1000"));
                BigDecimal gluc = quantite.multiply(nourriture.getPourcentageApportGlucide())
                                         .divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP)
                                         .multiply(new BigDecimal("1000"));

                // Coût total de cette ligne d'alimentation (kg * prix/kg)
                BigDecimal coutLigne = quantite.multiply(nourriture.getPrixAchatParKg());

                totalProteine = totalProteine.add(prot);
                totalGlucide = totalGlucide.add(gluc);
                coutTotalAlimentation = coutTotalAlimentation.add(coutLigne);
            }
        }

        // Distribuer aux poissons de l'étang de manière équitable
        List<EtangPoisson> assignments = etangPoissonRepository.findByEtang(etang);
        
        // 1. Filtrer d'abord pour avoir uniquement les poissons valides (Vivant et affectation actuelle)
        java.util.List<EtangPoisson> validAssignments = new java.util.ArrayList<>();
        for (EtangPoisson ep : assignments) {
            Poisson poisson = ep.getPoisson();
            
            // Vérifier affectation actuelle : l'ID de l'EtangPoisson passé doit être le plus récent pour ce poisson
            Optional<EtangPoisson> lastAssignment = etangPoissonRepository.findTopByPoissonOrderByDateDesc(poisson);
            
            // Log pour debug
            System.out.println("Checking poisson: " + poisson.getNom());
            if (lastAssignment.isPresent()) {
                System.out.println("Last assignment ID: " + lastAssignment.get().getId() + " (Etang ID: " + lastAssignment.get().getEtang().getId() + ")");
                System.out.println("Current EP ID: " + ep.getId() + " (Etang ID: " + ep.getEtang().getId() + ")");
            }

            if (!lastAssignment.isPresent() || !lastAssignment.get().getId().equals(ep.getId())) {
                System.out.println("Skipping: Not the last assignment");
                continue;
            }
            
            // Vérifier statut Vivant
            Optional<PoissonStatut> lastStatut = poissonStatutRepository.findTopByPoissonOrderByDateChangementDesc(poisson);
            if (lastStatut.isPresent()) {
                System.out.println("Status: " + lastStatut.get().getStatut().getNom());
            }

            if (!lastStatut.isPresent() || !lastStatut.get().getStatut().getNom().equalsIgnoreCase("Vivant")) {
                System.out.println("Skipping: Not living");
                continue;
            }
            
            validAssignments.add(ep);
        }

        if (!validAssignments.isEmpty()) {
            // Calculer la part par poisson (division équitable)
            BigDecimal nbPoissons = new BigDecimal(validAssignments.size());
            BigDecimal protParPoisson = totalProteine.divide(nbPoissons, 4, BigDecimal.ROUND_HALF_UP);
            BigDecimal glucParPoisson = totalGlucide.divide(nbPoissons, 4, BigDecimal.ROUND_HALF_UP);
            BigDecimal coutParPoisson = coutTotalAlimentation.divide(nbPoissons, 2, BigDecimal.ROUND_HALF_UP);

            for (EtangPoisson ep : validAssignments) {
                Poisson poisson = ep.getPoisson();
                Race race = poisson.getRace();

                // État nutritionnel du jour pour ce poisson
                LocalDate today = LocalDate.now();
                EtatNutritionJour etat = etatNutritionJourRepository
                        .findByPoissonAndDateJour(poisson, today)
                        .orElseGet(() -> {
                            EtatNutritionJour e = new EtatNutritionJour();
                            e.setPoisson(poisson);
                            e.setDateJour(today);
                            // Poids de départ : poids initial du poisson
                            e.setPoids(poisson.getPoidsInitial());
                            e.setProtStock(BigDecimal.ZERO);
                            e.setGlucStock(BigDecimal.ZERO);
                            e.setCyclesComplets(0);
                            e.setDemiCycles(0);
                            e.setCoutAlimentationCumule(BigDecimal.ZERO);
                            return e;
                        });

                BigDecimal protStock = etat.getProtStock() != null ? etat.getProtStock() : BigDecimal.ZERO;
                BigDecimal glucStock = etat.getGlucStock() != null ? etat.getGlucStock() : BigDecimal.ZERO;
                BigDecimal coutAlimCumule = etat.getCoutAlimentationCumule() != null ? etat.getCoutAlimentationCumule() : BigDecimal.ZERO;

                // Ajouter ce repas au stock (protParPoisson / glucParPoisson sont déjà en grammes)
                protStock = protStock.add(protParPoisson);
                glucStock = glucStock.add(glucParPoisson);

                // Ajouter le coût de ce repas pour ce poisson
                coutAlimCumule = coutAlimCumule.add(coutParPoisson);

                BigDecimal needsProt = race.getBesoinProteine() != null ? race.getBesoinProteine() : BigDecimal.ZERO;
                BigDecimal needsGluc = race.getBesoinGlucide() != null ? race.getBesoinGlucide() : BigDecimal.ZERO;
                BigDecimal cap = race.getCapaciteAugmentationPoids() != null ? race.getCapaciteAugmentationPoids() : BigDecimal.ZERO;

                // Calcul des cycles complets (100%) possibles avec le stock actuel
                int nbCyclesProt = BigDecimal.ZERO.compareTo(needsProt) < 0
                        ? protStock.divideToIntegralValue(needsProt).intValue()
                        : 0;
                int nbCyclesGluc = BigDecimal.ZERO.compareTo(needsGluc) < 0
                        ? glucStock.divideToIntegralValue(needsGluc).intValue()
                        : 0;
                int nbCycles = Math.min(nbCyclesProt, nbCyclesGluc);

                BigDecimal nouveauPoids = etat.getPoids() != null ? etat.getPoids() : poisson.getPoidsInitial();

                if (nbCycles > 0) {
                    // Consommer les besoins pour ces cycles
                    protStock = protStock.subtract(needsProt.multiply(new BigDecimal(nbCycles)));
                    glucStock = glucStock.subtract(needsGluc.multiply(new BigDecimal(nbCycles)));

                    BigDecimal augmentation100 = cap.multiply(new BigDecimal(nbCycles)); // en g
                    BigDecimal augmentationKg = augmentation100
                            .divide(new BigDecimal("1000"), 6, BigDecimal.ROUND_HALF_UP);

                    nouveauPoids = nouveauPoids.add(augmentationKg);
                    int cyclesExistants = etat.getCyclesComplets() != null ? etat.getCyclesComplets() : 0;
                    etat.setCyclesComplets(cyclesExistants + nbCycles);
                }

                // Demi-cycles (50%) : autant que le stock le permet (par jour et par poisson)
                int nbDemiCycles = 0;
                while (
                        (needsProt.compareTo(BigDecimal.ZERO) > 0 && protStock.compareTo(needsProt) >= 0) ||
                        (needsGluc.compareTo(BigDecimal.ZERO) > 0 && glucStock.compareTo(needsGluc) >= 0)
                ) {
                    // Consommer un besoin pour ce demi-cycle
                    if (needsProt.compareTo(BigDecimal.ZERO) > 0 && protStock.compareTo(needsProt) >= 0) {
                        protStock = protStock.subtract(needsProt);
                    } else if (needsGluc.compareTo(BigDecimal.ZERO) > 0 && glucStock.compareTo(needsGluc) >= 0) {
                        glucStock = glucStock.subtract(needsGluc);
                    } else {
                        break;
                    }

                    nbDemiCycles++;
                }

                if (nbDemiCycles > 0) {
                    BigDecimal augmentation50 = cap.multiply(new BigDecimal("0.5")).multiply(new BigDecimal(nbDemiCycles));
                    BigDecimal augmentationKg50 = augmentation50
                            .divide(new BigDecimal("1000"), 6, BigDecimal.ROUND_HALF_UP);

                    nouveauPoids = nouveauPoids.add(augmentationKg50);

                    Integer demiCyclesExistants = etat.getDemiCycles() != null ? etat.getDemiCycles() : 0;
                    etat.setDemiCycles(demiCyclesExistants + nbDemiCycles);
                }

                // Respecter le poids max de la race si défini
                BigDecimal poidsMax = race.getPoidsMax() != null ? race.getPoidsMax() : new BigDecimal("999999");
                if (nouveauPoids.compareTo(poidsMax) > 0) {
                    nouveauPoids = poidsMax;
                }

                // Mettre à jour l'état journalier
                etat.setProtStock(protStock);
                etat.setGlucStock(glucStock);
                etat.setPoids(nouveauPoids);
                etat.setCoutAlimentationCumule(coutAlimCumule);
                etatNutritionJourRepository.save(etat);

            }
        }

        return "redirect:/alimentations";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        alimentationRepository.deleteById(id);
        return "redirect:/alimentations";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        Alimentation alimentation = alimentationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid alimentation Id:" + id));

        List<AlimentationDetaille> details = alimentationDetailleRepository.findByAlimentation(alimentation);

        // Récupérer l'état nutritionnel journalier des poissons pour le jour de cette alimentation
        java.time.LocalDate jour = alimentation.getDateHeure().toLocalDate();
        List<EtatNutritionJour> etatsJour = etatNutritionJourRepository.findByDateJour(jour);

        model.addAttribute("alimentation", alimentation);
        model.addAttribute("details", details);
        model.addAttribute("etatsJour", etatsJour);
        model.addAttribute("title", "Détails de l'Alimentation #" + id);
        
        return "alimentations/details";
    }
}

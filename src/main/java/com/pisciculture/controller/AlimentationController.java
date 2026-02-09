package com.pisciculture.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pisciculture.model.Alimentation;
import com.pisciculture.model.AlimentationDetaille;
import com.pisciculture.model.Etang;
import com.pisciculture.model.EtangPoisson;
import com.pisciculture.model.EtatNutritionJour;
import com.pisciculture.model.Nourriture;
import com.pisciculture.model.NourritureNutriment;
import com.pisciculture.model.Nutriment;
import com.pisciculture.model.Poisson;
import com.pisciculture.model.PoissonNutrimentStock;
import com.pisciculture.model.PoissonStatut;
import com.pisciculture.model.Race;
import com.pisciculture.model.RaceNutriment;
import com.pisciculture.repository.AlimentationDetailleRepository;
import com.pisciculture.repository.AlimentationRepository;
import com.pisciculture.repository.EtangPoissonRepository;
import com.pisciculture.repository.EtangRepository;
import com.pisciculture.repository.EtatNutritionJourRepository;
import com.pisciculture.repository.NourritureRepository;
import com.pisciculture.repository.NutrimentRepository;
import com.pisciculture.repository.PoissonNutrimentStockRepository;
import com.pisciculture.repository.PoissonStatutRepository;

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

    @Autowired
    private NutrimentRepository nutrimentRepository;

    @Autowired
    private PoissonNutrimentStockRepository poissonNutrimentStockRepository;

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
                      @RequestParam("dateAlimentation") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateAlimentation,
                      @RequestParam("nourritureIds") List<Long> nourritureIds,
                      @RequestParam("quantites") List<BigDecimal> quantites) {
        
        Etang etang = etangRepository.findById(etangId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid etang Id:" + etangId));

        Alimentation alimentation = new Alimentation();
        alimentation.setEtang(etang);
        alimentation.setDateHeure(dateAlimentation);
        
        Alimentation savedAlimentation = alimentationRepository.save(alimentation);

        // Map pour stocker les apports totaux par nutriment pour cet étang
        java.util.Map<Long, BigDecimal> apportsNutrimentsEtang = new java.util.HashMap<>();
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

                // Calcul des nutriments apportés par cette nourriture
                if (nourriture.getNutriments() != null) {
                    for (NourritureNutriment nn : nourriture.getNutriments()) {
                        Nutriment nutriment = nn.getNutriment();
                        BigDecimal pourcentage = nn.getPourcentageApportNutriment();
                        
                        // Quantité en grammes : kg * pourcentage / 100 * 1000
                        BigDecimal apportG = quantite.multiply(pourcentage)
                                                     .divide(new BigDecimal("100"), 6, BigDecimal.ROUND_HALF_UP)
                                                     .multiply(new BigDecimal("1000"));
                        
                        apportsNutrimentsEtang.put(nutriment.getId(), 
                            apportsNutrimentsEtang.getOrDefault(nutriment.getId(), BigDecimal.ZERO).add(apportG));
                    }
                }

                // Coût total de cette ligne d'alimentation (kg * prix/kg)
                BigDecimal coutLigne = quantite.multiply(nourriture.getPrixAchatParKg());
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
            
            if (!lastAssignment.isPresent() || !lastAssignment.get().getId().equals(ep.getId())) {
                continue;
            }
            
            // Vérifier statut Vivant
            Optional<PoissonStatut> lastStatut = poissonStatutRepository.findTopByPoissonOrderByDateChangementDesc(poisson);

            if (!lastStatut.isPresent() || !lastStatut.get().getStatut().getNom().equalsIgnoreCase("Vivant")) {
                continue;
            }
            
            validAssignments.add(ep);
        }

        if (!validAssignments.isEmpty()) {
            // Calculer la part par poisson (division équitable)
            BigDecimal nbPoissons = new BigDecimal(validAssignments.size());
            BigDecimal coutParPoisson = coutTotalAlimentation.divide(nbPoissons, 2, BigDecimal.ROUND_HALF_UP);
            
            // Apports par nutriment par poisson
            java.util.Map<Long, BigDecimal> apportsNutrimentsParPoisson = new java.util.HashMap<>();
            for (java.util.Map.Entry<Long, BigDecimal> entry : apportsNutrimentsEtang.entrySet()) {
                apportsNutrimentsParPoisson.put(entry.getKey(), entry.getValue().divide(nbPoissons, 6, BigDecimal.ROUND_HALF_UP));
            }

            for (EtangPoisson ep : validAssignments) {
                Poisson poisson = ep.getPoisson();
                Race race = poisson.getRace();

                // État nutritionnel du jour pour ce poisson
                LocalDate feedingDate = dateAlimentation.toLocalDate();
                EtatNutritionJour etat = etatNutritionJourRepository
                        .findByPoissonAndDateJour(poisson, feedingDate)
                        .orElseGet(() -> {
                            EtatNutritionJour e = new EtatNutritionJour();
                            e.setPoisson(poisson);
                            e.setDateJour(feedingDate);
                            
                            // Récupérer le dernier poids connu AVANT ou à la date de feedingDate
                            BigDecimal dernierPoids = etatNutritionJourRepository
                                    .findTopByPoissonOrderByDateJourDesc(poisson)
                                    .map(EtatNutritionJour::getPoids)
                                    .orElse(poisson.getPoidsInitial());
                                    
                            e.setPoids(dernierPoids);
                            e.setCyclesComplets(0);
                            e.setDemiCycles(0);
                            e.setCoutAlimentationCumule(BigDecimal.ZERO);
                            
                            // On initialise le coût cumulé à partir du dernier état s'il existe
                            etatNutritionJourRepository.findTopByPoissonAndDateJourBeforeOrderByDateJourDesc(poisson, feedingDate)
                                .ifPresent(prev -> e.setCoutAlimentationCumule(prev.getCoutAlimentationCumule()));
                                
                            return e;
                        });

                BigDecimal coutAlimCumule = etat.getCoutAlimentationCumule() != null ? etat.getCoutAlimentationCumule() : BigDecimal.ZERO;
                coutAlimCumule = coutAlimCumule.add(coutParPoisson);

                // Gérer les stocks de nutriments dynamiques
                List<Nutriment> allNutriments = nutrimentRepository.findAll();
                java.util.Map<Long, BigDecimal> stocksNutriments = new java.util.HashMap<>();
                
                for (Nutriment n : allNutriments) {
                    PoissonNutrimentStock stockEntry = poissonNutrimentStockRepository
                        .findByPoissonAndNutrimentAndDateJour(poisson, n, feedingDate)
                        .orElseGet(() -> {
                            PoissonNutrimentStock ns = new PoissonNutrimentStock();
                            ns.setPoisson(poisson);
                            ns.setNutriment(n);
                            ns.setDateJour(feedingDate);
                            
                            // Récupérer le stock de la veille
                            BigDecimal lastStock = poissonNutrimentStockRepository
                                .findTopByPoissonAndNutrimentOrderByDateJourDesc(poisson, n)
                                .map(PoissonNutrimentStock::getStock)
                                .orElse(BigDecimal.ZERO);
                            ns.setStock(lastStock);
                            return ns;
                        });
                    
                    BigDecimal currentStock = stockEntry.getStock();
                    BigDecimal apport = apportsNutrimentsParPoisson.getOrDefault(n.getId(), BigDecimal.ZERO);
                    currentStock = currentStock.add(apport);
                    
                    stockEntry.setStock(currentStock);
                    stocksNutriments.put(n.getId(), currentStock);
                    poissonNutrimentStockRepository.save(stockEntry);
                }

                // Besoins de la race
                List<RaceNutriment> besoinsRace = race.getNutriments();
                BigDecimal cap = race.getCapaciteAugmentationPoids() != null ? race.getCapaciteAugmentationPoids() : BigDecimal.ZERO;

                // Calcul du nombre de cycles complets possibles (le minimum parmi tous les nutriments requis)
                int nbCycles = -1;
                if (besoinsRace != null && !besoinsRace.isEmpty()) {
                    for (RaceNutriment rn : besoinsRace) {
                        BigDecimal besoin = rn.getBesoinNutriment();
                        if (besoin.compareTo(BigDecimal.ZERO) > 0) {
                            BigDecimal stock = stocksNutriments.getOrDefault(rn.getNutriment().getId(), BigDecimal.ZERO);
                            int cyclesPossible = stock.divideToIntegralValue(besoin).intValue();
                            if (nbCycles == -1 || cyclesPossible < nbCycles) {
                                nbCycles = cyclesPossible;
                            }
                        }
                    }
                } else {
                    nbCycles = 0;
                }
                
                if (nbCycles < 0) nbCycles = 0;

                BigDecimal nouveauPoids = etat.getPoids() != null ? etat.getPoids() : poisson.getPoidsInitial();

                if (nbCycles > 0) {
                    // Consommer les besoins pour ces cycles
                    for (RaceNutriment rn : besoinsRace) {
                        BigDecimal besoinTotal = rn.getBesoinNutriment().multiply(new BigDecimal(nbCycles));
                        Long nutrimentId = rn.getNutriment().getId();
                        BigDecimal currentStock = stocksNutriments.get(nutrimentId);
                        BigDecimal newStock = currentStock.subtract(besoinTotal);
                        
                        stocksNutriments.put(nutrimentId, newStock);
                        
                        // Mettre à jour en base
                        poissonNutrimentStockRepository.findByPoissonAndNutrimentAndDateJour(poisson, rn.getNutriment(), feedingDate)
                            .ifPresent(s -> {
                                s.setStock(newStock);
                                poissonNutrimentStockRepository.save(s);
                            });
                    }

                    BigDecimal augmentation100 = cap.multiply(new BigDecimal(nbCycles)); // en g
                    BigDecimal augmentationKg = augmentation100
                            .divide(new BigDecimal("1000"), 6, BigDecimal.ROUND_HALF_UP);

                    nouveauPoids = nouveauPoids.add(augmentationKg);
                    int cyclesExistants = etat.getCyclesComplets() != null ? etat.getCyclesComplets() : 0;
                    etat.setCyclesComplets(cyclesExistants + nbCycles);
                }

                // Demi-cycles (Règle du tiers de poids par nutriment satisfait)
                int nbDemiCycles = 0;
                boolean canDoMoreDemiCycles = true;
                
                // On compte le nombre total de nutriments dont la race a besoin
                long totalNutrimentsRequis = besoinsRace != null ? besoinsRace.stream()
                        .filter(rn -> rn.getBesoinNutriment().compareTo(BigDecimal.ZERO) > 0)
                        .count() : 0;

                while (canDoMoreDemiCycles && totalNutrimentsRequis > 0) {
                    canDoMoreDemiCycles = false;
                    if (besoinsRace != null) {
                        for (RaceNutriment rn : besoinsRace) {
                            BigDecimal besoin = rn.getBesoinNutriment();
                            Long nutrimentId = rn.getNutriment().getId();
                            BigDecimal stock = stocksNutriments.getOrDefault(nutrimentId, BigDecimal.ZERO);
                            
                            if (besoin.compareTo(BigDecimal.ZERO) > 0 && stock.compareTo(besoin) >= 0) {
                                // Consommer le nutriment
                                BigDecimal newStock = stock.subtract(besoin);
                                stocksNutriments.put(nutrimentId, newStock);
                                
                                poissonNutrimentStockRepository.findByPoissonAndNutrimentAndDateJour(poisson, rn.getNutriment(), feedingDate)
                                    .ifPresent(s -> {
                                        s.setStock(newStock);
                                        poissonNutrimentStockRepository.save(s);
                                    });
                                
                                // Calcul de l'augmentation : (1 / totalNutrimentsRequis) de la capacité
                                BigDecimal fraction = BigDecimal.ONE.divide(new BigDecimal(totalNutrimentsRequis), 6, BigDecimal.ROUND_HALF_UP);
                                BigDecimal augmentationG = cap.multiply(fraction);
                                BigDecimal augmentationKg = augmentationG.divide(new BigDecimal("1000"), 6, BigDecimal.ROUND_HALF_UP);
                                
                                nouveauPoids = nouveauPoids.add(augmentationKg);
                                nbDemiCycles++;
                                canDoMoreDemiCycles = true;
                                
                                // On ne fait qu'une consommation à la fois dans la boucle while pour réévaluer
                                break; 
                            }
                        }
                    }
                }

                if (nbDemiCycles > 0) {
                    Integer demiCyclesExistants = etat.getDemiCycles() != null ? etat.getDemiCycles() : 0;
                    etat.setDemiCycles(demiCyclesExistants + nbDemiCycles);
                }

                // Respecter le poids max de la race si défini
                BigDecimal poidsMax = race.getPoidsMax() != null ? race.getPoidsMax() : new BigDecimal("999999");
                if (nouveauPoids.compareTo(poidsMax) > 0) {
                    nouveauPoids = poidsMax;
                }

                // Mettre à jour l'état journalier
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

        // Récupérer l'état nutritionnel journalier des poissons de cet étang pour le jour de cette alimentation
        java.time.LocalDate jour = alimentation.getDateHeure().toLocalDate();
        List<EtatNutritionJour> allEtatsJour = etatNutritionJourRepository.findByDateJour(jour);
        
        // Filtrer pour ne garder que les poissons qui étaient dans cet étang AU MOMENT de l'alimentation
        List<EtatNutritionJour> filteredEtats = new java.util.ArrayList<>();
        for (EtatNutritionJour ej : allEtatsJour) {
            Poisson p = ej.getPoisson();
            Optional<EtangPoisson> lastAssignment = etangPoissonRepository.findTopByPoissonOrderByDateDesc(p);
            if (lastAssignment.isPresent() && lastAssignment.get().getEtang().getId().equals(alimentation.getEtang().getId())) {
                filteredEtats.add(ej);
            }
        }

        // Pour chaque état, récupérer ses stocks de nutriments
        java.util.Map<Long, List<PoissonNutrimentStock>> stocksParEtat = new java.util.HashMap<>();
        for (EtatNutritionJour ej : filteredEtats) {
            stocksParEtat.put(ej.getId(), poissonNutrimentStockRepository.findByPoissonAndDateJour(ej.getPoisson(), jour));
        }

        model.addAttribute("alimentation", alimentation);
        model.addAttribute("details", details);
        model.addAttribute("etatsJour", filteredEtats);
        model.addAttribute("stocksParEtat", stocksParEtat);
        model.addAttribute("title", "Détails de l'Alimentation #" + id);
        
        return "alimentations/details";
    }
}

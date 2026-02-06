package com.pisciculture.controller;

import com.pisciculture.model.*;
import com.pisciculture.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/poissons")
public class PoissonController {

    @Autowired
    private PoissonRepository poissonRepository;

    @Autowired
    private RaceRepository raceRepository;

    @Autowired
    private StatutRepository statutRepository;

    @Autowired
    private PoissonStatutRepository poissonStatutRepository;

    @Autowired
    private EtangRepository etangRepository;

    @Autowired
    private EtangPoissonRepository etangPoissonRepository;

    @Autowired
    private EtatNutritionJourRepository etatNutritionJourRepository;

    @Autowired
    private HistoriquePoidsRepository historiquePoidsRepository;

    @GetMapping
    public String list(Model model) {
        List<Poisson> poissons = poissonRepository.findAll();
        for (Poisson p : poissons) {
            poissonStatutRepository.findTopByPoissonOrderByDateChangementDesc(p)
                    .ifPresent(ps -> p.setCurrentStatut(ps.getStatut()));
            etangPoissonRepository.findTopByPoissonOrderByDateDesc(p)
                    .ifPresent(ep -> p.setCurrentEtang(ep.getEtang()));
            
            // Récupérer le poids actuel à partir de l'état nutritionnel journalier (si existe),
            // sinon retomber sur le poids initial.
            etatNutritionJourRepository.findTopByPoissonOrderByDateJourDesc(p)
                    .ifPresentOrElse(
                            etat -> p.setCurrentPoids(etat.getPoids()),
                            () -> p.setCurrentPoids(p.getPoidsInitial())
                    );
        }
        model.addAttribute("poissons", poissons);
        model.addAttribute("title", "Liste des Poissons");
        return "poissons/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("poisson", new Poisson());
        model.addAttribute("races", raceRepository.findAll());
        model.addAttribute("statuts", statutRepository.findAll());
        model.addAttribute("etangs", etangRepository.findAll());
        model.addAttribute("title", "Nouveau Poisson");
        return "poissons/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Poisson poisson = poissonRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid poisson Id:" + id));
        poissonStatutRepository.findTopByPoissonOrderByDateChangementDesc(poisson)
                .ifPresent(ps -> poisson.setCurrentStatut(ps.getStatut()));
        etangPoissonRepository.findTopByPoissonOrderByDateDesc(poisson)
                .ifPresent(ep -> poisson.setCurrentEtang(ep.getEtang()));
        
        model.addAttribute("poisson", poisson);
        model.addAttribute("races", raceRepository.findAll());
        model.addAttribute("statuts", statutRepository.findAll());
        model.addAttribute("etangs", etangRepository.findAll());
        model.addAttribute("title", "Modifier le Poisson");
        return "poissons/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Poisson poisson, 
                      @RequestParam("statutId") Long statutId,
                      @RequestParam(value = "etangId", required = false) Long etangId) {
        Poisson savedPoisson = poissonRepository.save(poisson);
        
        // Gestion du statut
        Statut statut = statutRepository.findById(statutId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid statut Id:" + statutId));
        
        boolean shouldSaveStatut = true;
        if (poisson.getId() != null) {
            var currentPs = poissonStatutRepository.findTopByPoissonOrderByDateChangementDesc(savedPoisson);
            if (currentPs.isPresent() && currentPs.get().getStatut().getId().equals(statutId)) {
                shouldSaveStatut = false;
            }
        }
        
        if (shouldSaveStatut) {
            PoissonStatut poissonStatut = new PoissonStatut(savedPoisson, statut);
            poissonStatutRepository.save(poissonStatut);
        }

        // Gestion de l'étang
        if (etangId != null) {
            Etang etang = etangRepository.findById(etangId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid etang Id:" + etangId));
            
            boolean shouldSaveEtang = true;
            if (poisson.getId() != null) {
                var currentEp = etangPoissonRepository.findTopByPoissonOrderByDateDesc(savedPoisson);
                if (currentEp.isPresent() && currentEp.get().getEtang().getId().equals(etangId)) {
                    shouldSaveEtang = false;
                }
            }
            
            if (shouldSaveEtang) {
                EtangPoisson etangPoisson = new EtangPoisson(savedPoisson, etang);
                etangPoissonRepository.save(etangPoisson);
            }
        }
        
        return "redirect:/poissons";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        poissonRepository.deleteById(id);
        return "redirect:/poissons";
    }

    @GetMapping("/history/{id}")
    public String history(@PathVariable Long id, 
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate searchDate,
                         Model model) {
        Poisson poisson = poissonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid poisson Id:" + id));

        // Charger l'état actuel
        poissonStatutRepository.findTopByPoissonOrderByDateChangementDesc(poisson)
                .ifPresent(ps -> poisson.setCurrentStatut(ps.getStatut()));
        etangPoissonRepository.findTopByPoissonOrderByDateDesc(poisson)
                .ifPresent(ep -> poisson.setCurrentEtang(ep.getEtang()));

        // Poids courant basé sur l'état nutritionnel journalier s'il existe, sinon poids initial
        etatNutritionJourRepository.findTopByPoissonOrderByDateJourDesc(poisson)
                .ifPresentOrElse(
                        etat -> poisson.setCurrentPoids(etat.getPoids()),
                        () -> poisson.setCurrentPoids(poisson.getPoidsInitial())
                );
        etangPoissonRepository.findByPoissonOrderByDateDesc(poisson);

        model.addAttribute("poisson", poisson);
        model.addAttribute("statutHistory", poissonStatutRepository.findByPoissonOrderByDateChangementDesc(poisson));
        model.addAttribute("etangHistory", etangPoissonRepository.findByPoissonOrderByDateDesc(poisson));
        
        // Récupérer l'historique de l'évolution du poids depuis etat_nutrition_jour
        List<EtatNutritionJour> evolutionHistory;
        if (searchDate != null) {
            evolutionHistory = etatNutritionJourRepository.findTopByPoissonAndDateJourLessThanEqualOrderByDateJourDesc(poisson, searchDate)
                    .map(List::of)
                    .orElse(List.of());
            model.addAttribute("searchDate", searchDate);
        } else {
            evolutionHistory = etatNutritionJourRepository.findByPoissonOrderByDateJourDesc(poisson);
        }
        model.addAttribute("evolutionHistory", evolutionHistory);
        
        model.addAttribute("title", "Historique du Poisson : " + poisson.getNom());
        
        return "poissons/history";
    }

    @PostMapping("/weight/save")
    public String saveWeight(@RequestParam("poissonId") Long poissonId,
                            @RequestParam("dateMesure") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateMesure,
                            @RequestParam("poids") BigDecimal poids) {
        Poisson poisson = poissonRepository.findById(poissonId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid poisson Id:" + poissonId));
        
        HistoriquePoids hp = new HistoriquePoids(poisson, dateMesure, poids);
        historiquePoidsRepository.save(hp);
        
        return "redirect:/poissons/history/" + poissonId;
    }
}

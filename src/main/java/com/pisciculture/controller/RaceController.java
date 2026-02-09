package com.pisciculture.controller;

import com.pisciculture.model.*;
import com.pisciculture.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/races")
public class RaceController {

    @Autowired
    private RaceRepository raceRepository;

    @Autowired
    private TarifPoissonRepository tarifPoissonRepository;

    @Autowired
    private TypePrixRepository typePrixRepository;

    @Autowired
    private NutrimentRepository nutrimentRepository;

    @Autowired
    private RaceNutrimentRepository raceNutrimentRepository;

    @GetMapping
    public String list(Model model) {
        List<Race> races = raceRepository.findAll();
        model.addAttribute("races", races);
        model.addAttribute("title", "Liste des Races");
        return "races/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("race", new Race());
        model.addAttribute("nutriments", nutrimentRepository.findAll());
        model.addAttribute("title", "Nouvelle Race");
        return "races/form";
    }

    @PostMapping("/save")
    @Transactional
    public String save(@ModelAttribute Race race, @RequestParam Map<String, String> allParams) {
        boolean isNew = race.getId() == null;
        Race existingRace = null;
        if (!isNew) {
            existingRace = raceRepository.findById(race.getId()).orElse(null);
            if (existingRace != null) {
                // On met à jour l'objet existant pour ne pas perdre la collection gérée par Hibernate
                existingRace.setNom(race.getNom());
                existingRace.setPrixAchatParKg(race.getPrixAchatParKg());
                existingRace.setPrixVenteParKg(race.getPrixVenteParKg());
                existingRace.setPoidsMax(race.getPoidsMax());
                existingRace.setCapaciteAugmentationPoids(race.getCapaciteAugmentationPoids());
                race = existingRace;
            }
        }

        Race savedRace = raceRepository.save(race);

        // Historisation du prix d'achat
        if (isNew || (existingRace != null && existingRace.getPrixAchatParKg().compareTo(race.getPrixAchatParKg()) != 0)) {
            TypePrix typeAchat = typePrixRepository.findByNom("achat")
                    .orElseGet(() -> {
                        TypePrix tp = new TypePrix();
                        tp.setNom("achat");
                        return typePrixRepository.save(tp);
                    });
            TarifPoisson tarif = new TarifPoisson();
            tarif.setRace(savedRace);
            tarif.setTypePrix(typeAchat);
            tarif.setMontant(race.getPrixAchatParKg());
            tarifPoissonRepository.save(tarif);
        }

        // Historisation du prix de vente
        if (isNew || (existingRace != null && existingRace.getPrixVenteParKg().compareTo(race.getPrixVenteParKg()) != 0)) {
            TypePrix typeVente = typePrixRepository.findByNom("vente")
                    .orElseGet(() -> {
                        TypePrix tp = new TypePrix();
                        tp.setNom("vente");
                        return typePrixRepository.save(tp);
                    });
            TarifPoisson tarif = new TarifPoisson();
            tarif.setRace(savedRace);
            tarif.setTypePrix(typeVente);
            tarif.setMontant(race.getPrixVenteParKg());
            tarifPoissonRepository.save(tarif);
        }

        // Gestion des nutriments modulables
        raceNutrimentRepository.deleteByRace(savedRace);
        if (savedRace.getNutriments() != null) {
            savedRace.getNutriments().clear();
        }
        
        List<Nutriment> allNutriments = nutrimentRepository.findAll();
        for (Nutriment n : allNutriments) {
            String val = allParams.get("nutriment_" + n.getId());
            if (val != null && !val.isEmpty()) {
                RaceNutriment rn = new RaceNutriment();
                rn.setRace(savedRace);
                rn.setNutriment(n);
                rn.setBesoinNutriment(new BigDecimal(val));
                raceNutrimentRepository.save(rn);
            }
        }

        return "redirect:/races";
    }

    @GetMapping("/history/{id}")
    public String history(@PathVariable Long id, Model model) {
        Race race = raceRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid race Id:" + id));
        List<TarifPoisson> history = tarifPoissonRepository.findByRaceIdOrderByDateDesc(id);
        model.addAttribute("race", race);
        model.addAttribute("history", history);
        model.addAttribute("title", "Historique des tarifs - " + race.getNom());
        return "races/history";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Race race = raceRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid race Id:" + id));
        List<RaceNutriment> currentNutriments = raceNutrimentRepository.findByRace(race);
        Map<Long, BigDecimal> nutrientValues = currentNutriments.stream()
                .collect(Collectors.toMap(rn -> rn.getNutriment().getId(), RaceNutriment::getBesoinNutriment));
        
        model.addAttribute("race", race);
        model.addAttribute("nutriments", nutrimentRepository.findAll());
        model.addAttribute("nutrientValues", nutrientValues);
        model.addAttribute("title", "Modifier la Race");
        return "races/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        raceRepository.deleteById(id);
        return "redirect:/races";
    }
}

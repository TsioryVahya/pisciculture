package com.pisciculture.controller;

import com.pisciculture.model.Race;
import com.pisciculture.model.TarifPoisson;
import com.pisciculture.model.TypePrix;
import com.pisciculture.repository.RaceRepository;
import com.pisciculture.repository.TarifPoissonRepository;
import com.pisciculture.repository.TypePrixRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Controller
@RequestMapping("/races")
public class RaceController {

    @Autowired
    private RaceRepository raceRepository;

    @Autowired
    private TarifPoissonRepository tarifPoissonRepository;

    @Autowired
    private TypePrixRepository typePrixRepository;

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
        model.addAttribute("title", "Nouvelle Race");
        return "races/form";
    }

    @PostMapping("/save")
    @Transactional
    public String save(@ModelAttribute Race race) {
        boolean isNew = race.getId() == null;
        Race oldRace = null;
        if (!isNew) {
            oldRace = raceRepository.findById(race.getId()).orElse(null);
        }

        Race savedRace = raceRepository.save(race);

        // Historisation du prix d'achat
        if (isNew || (oldRace != null && oldRace.getPrixAchatParKg().compareTo(race.getPrixAchatParKg()) != 0)) {
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
        if (isNew || (oldRace != null && oldRace.getPrixVenteParKg().compareTo(race.getPrixVenteParKg()) != 0)) {
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
        model.addAttribute("race", race);
        model.addAttribute("title", "Modifier la Race");
        return "races/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        raceRepository.deleteById(id);
        return "redirect:/races";
    }
}

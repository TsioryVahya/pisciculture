package com.pisciculture.controller;

import com.pisciculture.model.*;
import com.pisciculture.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/nourritures")
public class NourritureController {

    @Autowired
    private NourritureRepository nourritureRepository;

    @Autowired
    private NutrimentRepository nutrimentRepository;

    @Autowired
    private NourritureNutrimentRepository nourritureNutrimentRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("nourritures", nourritureRepository.findAll());
        model.addAttribute("title", "Liste des Nourritures");
        return "nourritures/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("nourriture", new Nourriture());
        model.addAttribute("nutriments", nutrimentRepository.findAll());
        model.addAttribute("title", "Nouvelle Nourriture");
        return "nourritures/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Nourriture nourriture = nourritureRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid nourriture Id:" + id));
        List<NourritureNutriment> currentNutriments = nourritureNutrimentRepository.findByNourriture(nourriture);
        Map<Long, BigDecimal> nutrientValues = currentNutriments.stream()
                .collect(Collectors.toMap(nn -> nn.getNutriment().getId(), NourritureNutriment::getPourcentageApportNutriment));

        model.addAttribute("nourriture", nourriture);
        model.addAttribute("nutriments", nutrimentRepository.findAll());
        model.addAttribute("nutrientValues", nutrientValues);
        model.addAttribute("title", "Modifier la Nourriture");
        return "nourritures/form";
    }

    @PostMapping("/save")
    @Transactional
    public String save(@ModelAttribute Nourriture nourriture, @RequestParam Map<String, String> allParams) {
        // Si c'est une mise à jour, on récupère l'objet existant pour préserver sa collection
        if (nourriture.getId() != null) {
            Nourriture existing = nourritureRepository.findById(nourriture.getId()).orElse(null);
            if (existing != null) {
                existing.setNom(nourriture.getNom());
                existing.setPrixAchatParKg(nourriture.getPrixAchatParKg());
                nourriture = existing;
            }
        }
        
        Nourriture savedNourriture = nourritureRepository.save(nourriture);

        // Gestion des nutriments modulables
        nourritureNutrimentRepository.deleteByNourriture(savedNourriture);
        if (savedNourriture.getNutriments() != null) {
            savedNourriture.getNutriments().clear();
        }
        
        List<Nutriment> allNutriments = nutrimentRepository.findAll();
        for (Nutriment n : allNutriments) {
            String val = allParams.get("nutriment_" + n.getId());
            if (val != null && !val.isEmpty()) {
                NourritureNutriment nn = new NourritureNutriment();
                nn.setNourriture(savedNourriture);
                nn.setNutriment(n);
                nn.setPourcentageApportNutriment(new BigDecimal(val));
                nourritureNutrimentRepository.save(nn);
            }
        }

        return "redirect:/nourritures";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        nourritureRepository.deleteById(id);
        return "redirect:/nourritures";
    }
}

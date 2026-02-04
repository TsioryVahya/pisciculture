package com.pisciculture.controller;

import com.pisciculture.model.Etang;
import com.pisciculture.model.EtangPoisson;
import com.pisciculture.model.Poisson;
import com.pisciculture.repository.EtangPoissonRepository;
import com.pisciculture.repository.EtangRepository;
import com.pisciculture.repository.PoissonRepository;
import com.pisciculture.repository.PoissonStatutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/etangs")
public class EtangController {

    @Autowired
    private EtangRepository etangRepository;

    @Autowired
    private PoissonRepository poissonRepository;

    @Autowired
    private EtangPoissonRepository etangPoissonRepository;

    @Autowired
    private PoissonStatutRepository poissonStatutRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("etangs", etangRepository.findAll());
        model.addAttribute("title", "Liste des Étangs");
        return "etangs/list";
    }

    @GetMapping("/affecter/{id}")
    public String affecterForm(@PathVariable Long id, Model model) {
        Etang etang = etangRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid etang Id:" + id));
        
        List<Poisson> allPoissons = poissonRepository.findAll();
        List<Poisson> availablePoissons = new ArrayList<>();

        for (Poisson p : allPoissons) {
            // Récupérer le dernier statut
            var lastStatut = poissonStatutRepository.findTopByPoissonOrderByDateChangementDesc(p);
            String statutNom = lastStatut.isPresent() ? lastStatut.get().getStatut().getNom() : "Inconnu";

            // Récupérer l'étang actuel
            var lastEtang = etangPoissonRepository.findTopByPoissonOrderByDateDesc(p);
            boolean isAssigned = lastEtang.isPresent();

            // Filtrer : Vivant ET non assigné
            if ("Vivant".equals(statutNom) && !isAssigned) {
                availablePoissons.add(p);
            }
        }
        
        model.addAttribute("etang", etang);
        model.addAttribute("poissons", availablePoissons);
        model.addAttribute("title", "Affecter des Poissons à l'Étang #" + etang.getId());
        return "etangs/affecter";
    }

    @PostMapping("/affecter/save")
    public String saveAffectations(@RequestParam("etangId") Long etangId,
                                   @RequestParam("poissonIds") List<Long> poissonIds) {
        Etang etang = etangRepository.findById(etangId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid etang Id:" + etangId));
        
        for (Long poissonId : poissonIds) {
            Poisson poisson = poissonRepository.findById(poissonId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid poisson Id:" + poissonId));
            
            // On vérifie si le poisson est déjà dans cet étang (dernier état)
            boolean shouldSave = true;
            var currentEp = etangPoissonRepository.findTopByPoissonOrderByDateDesc(poisson);
            if (currentEp.isPresent() && currentEp.get().getEtang().getId().equals(etangId)) {
                shouldSave = false;
            }

            if (shouldSave) {
                EtangPoisson etangPoisson = new EtangPoisson(poisson, etang);
                etangPoissonRepository.save(etangPoisson);
            }
        }
        
        return "redirect:/etangs";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("etang", new Etang());
        model.addAttribute("title", "Nouvel Étang");
        return "etangs/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Etang etang = etangRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid etang Id:" + id));
        model.addAttribute("etang", etang);
        model.addAttribute("title", "Modifier l'Étang");
        return "etangs/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Etang etang) {
        etangRepository.save(etang);
        return "redirect:/etangs";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        etangRepository.deleteById(id);
        return "redirect:/etangs";
    }
}

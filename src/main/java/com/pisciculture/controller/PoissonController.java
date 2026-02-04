package com.pisciculture.controller;

import com.pisciculture.model.Poisson;
import com.pisciculture.model.PoissonStatut;
import com.pisciculture.model.Statut;
import com.pisciculture.repository.PoissonRepository;
import com.pisciculture.repository.PoissonStatutRepository;
import com.pisciculture.repository.RaceRepository;
import com.pisciculture.repository.StatutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public String list(Model model) {
        List<Poisson> poissons = poissonRepository.findAll();
        for (Poisson p : poissons) {
            poissonStatutRepository.findTopByPoissonOrderByDateChangementDesc(p)
                    .ifPresent(ps -> p.setCurrentStatut(ps.getStatut()));
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
        model.addAttribute("title", "Nouveau Poisson");
        return "poissons/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Poisson poisson = poissonRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid poisson Id:" + id));
        poissonStatutRepository.findTopByPoissonOrderByDateChangementDesc(poisson)
                .ifPresent(ps -> poisson.setCurrentStatut(ps.getStatut()));
        
        model.addAttribute("poisson", poisson);
        model.addAttribute("races", raceRepository.findAll());
        model.addAttribute("statuts", statutRepository.findAll());
        model.addAttribute("title", "Modifier le Poisson");
        return "poissons/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Poisson poisson, @RequestParam("statutId") Long statutId) {
        Poisson savedPoisson = poissonRepository.save(poisson);
        
        Statut statut = statutRepository.findById(statutId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid statut Id:" + statutId));
        
        // Vérifier si le statut a changé avant de l'ajouter à l'historique
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
        
        return "redirect:/poissons";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        poissonRepository.deleteById(id);
        return "redirect:/poissons";
    }
}

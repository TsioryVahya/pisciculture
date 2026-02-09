package com.pisciculture.controller;

import com.pisciculture.model.Nutriment;
import com.pisciculture.repository.NutrimentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/nutriments")
public class NutrimentController {

    @Autowired
    private NutrimentRepository nutrimentRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("nutriments", nutrimentRepository.findAll());
        model.addAttribute("title", "Gestion des Nutriments");
        return "nutriments/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("nutriment", new Nutriment());
        model.addAttribute("title", "Ajouter un Nutriment");
        return "nutriments/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Nutriment nutriment) {
        nutrimentRepository.save(nutriment);
        return "redirect:/nutriments";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Nutriment nutriment = nutrimentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid nutriment Id:" + id));
        model.addAttribute("nutriment", nutriment);
        model.addAttribute("title", "Modifier le Nutriment");
        return "nutriments/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        nutrimentRepository.deleteById(id);
        return "redirect:/nutriments";
    }
}

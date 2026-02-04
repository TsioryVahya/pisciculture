package com.pisciculture.controller;

import com.pisciculture.model.Nourriture;
import com.pisciculture.repository.NourritureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/nourritures")
public class NourritureController {

    @Autowired
    private NourritureRepository nourritureRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("nourritures", nourritureRepository.findAll());
        model.addAttribute("title", "Liste des Nourritures");
        return "nourritures/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("nourriture", new Nourriture());
        model.addAttribute("title", "Nouvelle Nourriture");
        return "nourritures/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Nourriture nourriture = nourritureRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid nourriture Id:" + id));
        model.addAttribute("nourriture", nourriture);
        model.addAttribute("title", "Modifier la Nourriture");
        return "nourritures/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Nourriture nourriture) {
        nourritureRepository.save(nourriture);
        return "redirect:/nourritures";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        nourritureRepository.deleteById(id);
        return "redirect:/nourritures";
    }
}

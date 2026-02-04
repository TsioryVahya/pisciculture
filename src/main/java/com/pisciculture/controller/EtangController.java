package com.pisciculture.controller;

import com.pisciculture.model.Etang;
import com.pisciculture.repository.EtangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/etangs")
public class EtangController {

    @Autowired
    private EtangRepository etangRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("etangs", etangRepository.findAll());
        model.addAttribute("title", "Liste des Étangs");
        return "etangs/list";
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

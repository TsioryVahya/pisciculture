package com.pisciculture.controller;

import com.pisciculture.model.EtangPoisson;
import com.pisciculture.repository.EtangPoissonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/affectations")
public class AffectationController {

    @Autowired
    private EtangPoissonRepository etangPoissonRepository;

    @GetMapping
    public String list(Model model) {
        List<EtangPoisson> affectations = etangPoissonRepository.findAll(Sort.by(Sort.Direction.DESC, "date", "id"));
        model.addAttribute("affectations", affectations);
        model.addAttribute("title", "Historique des Affectations");
        return "affectations/list";
    }
}

package com.pisciculture.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "nourriture")
public class Nourriture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(name = "prix_achat_par_kg")
    private BigDecimal prixAchatParKg;

    @OneToMany(mappedBy = "nourriture", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NourritureNutriment> nutriments;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public BigDecimal getPrixAchatParKg() { return prixAchatParKg; }
    public void setPrixAchatParKg(BigDecimal prixAchatParKg) { this.prixAchatParKg = prixAchatParKg; }

    public List<NourritureNutriment> getNutriments() { return nutriments; }
    public void setNutriments(List<NourritureNutriment> nutriments) { this.nutriments = nutriments; }
}
